package co.edu.sena.controlparkiq.service;

import co.edu.sena.controlparkiq.dto.EspacioParqueoDTO;
import co.edu.sena.controlparkiq.exception.ConflictoException;
import co.edu.sena.controlparkiq.exception.RecursoNoEncontradoException;
import co.edu.sena.controlparkiq.exception.ReglaNegocioException;
import co.edu.sena.controlparkiq.model.EspacioParqueo;
import co.edu.sena.controlparkiq.model.EstadoEspacio;
import co.edu.sena.controlparkiq.model.TipoVehiculo;
import co.edu.sena.controlparkiq.repository.EspacioParqueoRepository;
import co.edu.sena.controlparkiq.repository.SesionParqueoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

/**
 * Lógica de los espacios de parqueo (HU-05 y HU-06).
 *
 * <p>Reglas del negocio:</p>
 * <ul>
 *   <li>El código no se repite (409) y la zona debe existir (404).</li>
 *   <li>El estado OCUPADO solo lo asigna el sistema al registrar una entrada (400).</li>
 *   <li>No se puede modificar ni eliminar un espacio ocupado (409).</li>
 *   <li>No se puede eliminar un espacio con historial de parqueo (409).</li>
 * </ul>
 *
 * @author Carlos Eduardo Valero
 */
@Service
public class EspacioParqueoService {

    private static final String RECURSO = "el espacio de parqueo";

    private final EspacioParqueoRepository espacioRepository;
    private final SesionParqueoRepository sesionRepository;
    private final ZonaParqueoService zonaService;

    public EspacioParqueoService(EspacioParqueoRepository espacioRepository, SesionParqueoRepository sesionRepository,
                                 ZonaParqueoService zonaService) {
        this.espacioRepository = espacioRepository;
        this.sesionRepository = sesionRepository;
        this.zonaService = zonaService;
    }

    /**
     * Lista los espacios con filtros opcionales. Portería usa
     * {@code ?estado=DISPONIBLE&tipoVehiculo=CARRO} para asignar un espacio (HU-06).
     *
     * @param estado       filtro opcional
     * @param tipoVehiculo filtro opcional
     * @return lista de espacios
     */
    public List<EspacioParqueo> listar(EstadoEspacio estado, TipoVehiculo tipoVehiculo) {
        if (estado != null && tipoVehiculo != null) {
            return espacioRepository.findByEstadoAndTipoVehiculo(estado, tipoVehiculo);
        }
        if (estado != null) {
            return espacioRepository.findByEstado(estado);
        }
        if (tipoVehiculo != null) {
            return espacioRepository.findByTipoVehiculo(tipoVehiculo);
        }
        return espacioRepository.findAll();
    }

    public EspacioParqueo buscar(Long id) {
        return espacioRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException(RECURSO, id));
    }

    @Transactional
    public EspacioParqueo crear(EspacioParqueoDTO datos) {
        String codigo = normalizarCodigo(datos.getCodigo());
        if (espacioRepository.existsByCodigo(codigo)) {
            throw new ConflictoException("Ya existe un espacio con el código " + codigo);
        }
        EspacioParqueo espacio = new EspacioParqueo();
        copiarDatos(datos, codigo, espacio);
        return espacioRepository.save(espacio);
    }

    @Transactional
    public EspacioParqueo actualizar(Long id, EspacioParqueoDTO datos) {
        EspacioParqueo espacio = buscar(id);
        if (espacio.getEstado() == EstadoEspacio.OCUPADO) {
            throw new ConflictoException("No se puede modificar el espacio " + espacio.getCodigo() + ": está ocupado");
        }
        String codigo = normalizarCodigo(datos.getCodigo());
        if (espacioRepository.existsByCodigoAndIdNot(codigo, id)) {
            throw new ConflictoException("Ya existe otro espacio con el código " + codigo);
        }
        copiarDatos(datos, codigo, espacio);
        return espacioRepository.save(espacio);
    }

    @Transactional
    public void eliminar(Long id) {
        EspacioParqueo espacio = buscar(id);
        if (espacio.getEstado() == EstadoEspacio.OCUPADO) {
            throw new ConflictoException("No se puede eliminar el espacio " + espacio.getCodigo() + ": está ocupado");
        }
        if (sesionRepository.existsByEspacioId(id)) {
            throw new ConflictoException("No se puede eliminar el espacio " + espacio.getCodigo()
                    + ": tiene historial de parqueo (puede inhabilitarlo)");
        }
        espacioRepository.delete(espacio);
    }

    /**
     * Cambia el estado de un espacio. Lo usan las sesiones de parqueo
     * (OCUPADO al entrar, DISPONIBLE al salir).
     *
     * @param espacio espacio a modificar
     * @param estado  nuevo estado
     */
    @Transactional
    public void cambiarEstado(EspacioParqueo espacio, EstadoEspacio estado) {
        espacio.setEstado(estado);
        espacioRepository.save(espacio);
    }

    private void copiarDatos(EspacioParqueoDTO datos, String codigo, EspacioParqueo espacio) {
        if (datos.getEstado() == EstadoEspacio.OCUPADO) {
            throw new ReglaNegocioException("El estado OCUPADO lo asigna el sistema al registrar la entrada de un vehículo");
        }
        espacio.setCodigo(codigo);
        espacio.setTipoVehiculo(datos.getTipoVehiculo());
        espacio.setEstado(datos.getEstado() == null ? EstadoEspacio.DISPONIBLE : datos.getEstado());
        espacio.setZona(zonaService.buscar(datos.getZonaId()));
    }

    private String normalizarCodigo(String codigo) {
        return codigo.trim().toUpperCase(Locale.ROOT);
    }
}
