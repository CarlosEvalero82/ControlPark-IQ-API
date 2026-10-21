package co.edu.sena.controlparkiq.service;

import co.edu.sena.controlparkiq.dto.EntradaDTO;
import co.edu.sena.controlparkiq.dto.SalidaDTO;
import co.edu.sena.controlparkiq.exception.ConflictoException;
import co.edu.sena.controlparkiq.exception.RecursoNoEncontradoException;
import co.edu.sena.controlparkiq.exception.ReglaNegocioException;
import co.edu.sena.controlparkiq.model.EspacioParqueo;
import co.edu.sena.controlparkiq.model.EstadoEspacio;
import co.edu.sena.controlparkiq.model.EstadoSesion;
import co.edu.sena.controlparkiq.model.SesionParqueo;
import co.edu.sena.controlparkiq.model.TipoPersona;
import co.edu.sena.controlparkiq.model.TipoZona;
import co.edu.sena.controlparkiq.model.Vehiculo;
import co.edu.sena.controlparkiq.repository.FacturaVisitanteRepository;
import co.edu.sena.controlparkiq.repository.SesionParqueoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Lógica de las sesiones de parqueo: entrada y salida de vehículos (HU-08 y HU-10).
 *
 * <p>Reglas del negocio:</p>
 * <ul>
 *   <li>El vehículo y el espacio deben existir (404).</li>
 *   <li>El espacio debe estar DISPONIBLE (409) y ser del mismo tipo que el vehículo (400).</li>
 *   <li>Residentes en zonas de residentes y visitantes en zonas de visitantes (400).</li>
 *   <li>Un vehículo no puede tener dos sesiones abiertas (409).</li>
 *   <li>La salida no puede ser anterior a la entrada (400) y una sesión cerrada no se vuelve a cerrar (409).</li>
 *   <li>Al entrar el espacio queda OCUPADO; al salir vuelve a DISPONIBLE.</li>
 * </ul>
 *
 * @author Carlos Eduardo Valero
 */
@Service
public class SesionParqueoService {

    private static final String RECURSO = "la sesión de parqueo";

    private final SesionParqueoRepository sesionRepository;
    private final FacturaVisitanteRepository facturaRepository;
    private final VehiculoService vehiculoService;
    private final EspacioParqueoService espacioService;

    public SesionParqueoService(SesionParqueoRepository sesionRepository, FacturaVisitanteRepository facturaRepository,
                                VehiculoService vehiculoService, EspacioParqueoService espacioService) {
        this.sesionRepository = sesionRepository;
        this.facturaRepository = facturaRepository;
        this.vehiculoService = vehiculoService;
        this.espacioService = espacioService;
    }

    /**
     * Lista las sesiones. Portería usa {@code ?fecha=2026-10-20} para ver el historial
     * del día y {@code ?estado=ABIERTA} para ver qué vehículos están adentro.
     *
     * @param estado filtro opcional
     * @param fecha  filtro opcional por día de entrada
     * @return lista de sesiones
     */
    public List<SesionParqueo> listar(EstadoSesion estado, LocalDate fecha) {
        List<SesionParqueo> sesiones = fecha == null
                ? (estado == null ? sesionRepository.findAll() : sesionRepository.findByEstado(estado))
                : sesionRepository.findByHoraEntradaBetween(fecha.atStartOfDay(), fecha.plusDays(1).atStartOfDay());
        if (fecha != null && estado != null) {
            sesiones = sesiones.stream().filter(s -> s.getEstado() == estado).toList();
        }
        return sesiones;
    }

    public SesionParqueo buscar(Long id) {
        return sesionRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException(RECURSO, id));
    }

    /**
     * Registra la entrada de un vehículo y ocupa el espacio.
     *
     * @param datos vehículo, espacio y hora de entrada opcional
     * @return la sesión ABIERTA
     */
    @Transactional
    public SesionParqueo registrarEntrada(EntradaDTO datos) {
        Vehiculo vehiculo = vehiculoService.buscar(datos.getVehiculoId());
        EspacioParqueo espacio = espacioService.buscar(datos.getEspacioId());

        if (sesionRepository.existsByVehiculoIdAndEstado(vehiculo.getId(), EstadoSesion.ABIERTA)) {
            throw new ConflictoException("El vehículo " + vehiculo.getPlaca() + " ya está dentro del parqueadero");
        }
        if (espacio.getEstado() != EstadoEspacio.DISPONIBLE) {
            throw new ConflictoException("El espacio " + espacio.getCodigo() + " no está disponible (" + espacio.getEstado() + ")");
        }
        if (espacio.getTipoVehiculo() != vehiculo.getTipo()) {
            throw new ReglaNegocioException("El espacio " + espacio.getCodigo() + " es para " + espacio.getTipoVehiculo()
                    + " y el vehículo es " + vehiculo.getTipo());
        }
        TipoZona zonaEsperada = vehiculo.getPropietario().getTipo() == TipoPersona.RESIDENTE
                ? TipoZona.RESIDENTES : TipoZona.VISITANTES;
        if (espacio.getZona().getTipo() != zonaEsperada) {
            throw new ReglaNegocioException("Un " + vehiculo.getPropietario().getTipo().name().toLowerCase()
                    + " debe parquear en una zona de " + zonaEsperada.name().toLowerCase());
        }

        SesionParqueo sesion = new SesionParqueo();
        sesion.setVehiculo(vehiculo);
        sesion.setEspacio(espacio);
        sesion.setHoraEntrada(datos.getHoraEntrada() == null ? LocalDateTime.now() : datos.getHoraEntrada());
        sesion.setEstado(EstadoSesion.ABIERTA);
        espacioService.cambiarEstado(espacio, EstadoEspacio.OCUPADO);
        return sesionRepository.save(sesion);
    }

    /**
     * Registra la salida del vehículo, cierra la sesión y libera el espacio.
     *
     * @param id    id de la sesión
     * @param datos hora de salida opcional
     * @return la sesión CERRADA
     */
    @Transactional
    public SesionParqueo registrarSalida(Long id, SalidaDTO datos) {
        SesionParqueo sesion = buscar(id);
        if (sesion.getEstado() == EstadoSesion.CERRADA) {
            throw new ConflictoException("La sesión " + id + " ya está cerrada: el vehículo ya salió");
        }
        LocalDateTime salida = datos == null || datos.getHoraSalida() == null ? LocalDateTime.now() : datos.getHoraSalida();
        if (salida.isBefore(sesion.getHoraEntrada())) {
            throw new ReglaNegocioException("La hora de salida no puede ser anterior a la hora de entrada ("
                    + sesion.getHoraEntrada() + ")");
        }
        sesion.setHoraSalida(salida);
        sesion.setEstado(EstadoSesion.CERRADA);
        espacioService.cambiarEstado(sesion.getEspacio(), EstadoEspacio.DISPONIBLE);
        return sesionRepository.save(sesion);
    }

    /**
     * Elimina una sesión registrada por error. Si estaba abierta, libera el espacio.
     * No se permite si ya tiene factura (409).
     *
     * @param id id de la sesión
     */
    @Transactional
    public void eliminar(Long id) {
        SesionParqueo sesion = buscar(id);
        if (facturaRepository.existsBySesionId(id)) {
            throw new ConflictoException("No se puede eliminar la sesión " + id + ": ya tiene factura");
        }
        if (sesion.getEstado() == EstadoSesion.ABIERTA) {
            espacioService.cambiarEstado(sesion.getEspacio(), EstadoEspacio.DISPONIBLE);
        }
        sesionRepository.delete(sesion);
    }
}
