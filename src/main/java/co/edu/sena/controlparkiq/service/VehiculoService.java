package co.edu.sena.controlparkiq.service;

import co.edu.sena.controlparkiq.dto.VehiculoDTO;
import co.edu.sena.controlparkiq.exception.ConflictoException;
import co.edu.sena.controlparkiq.exception.RecursoNoEncontradoException;
import co.edu.sena.controlparkiq.exception.ReglaNegocioException;
import co.edu.sena.controlparkiq.model.TipoVehiculo;
import co.edu.sena.controlparkiq.model.Vehiculo;
import co.edu.sena.controlparkiq.repository.SesionParqueoRepository;
import co.edu.sena.controlparkiq.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

/**
 * Lógica de los vehículos (HU-04).
 *
 * <p>Reglas del negocio:</p>
 * <ul>
 *   <li>La placa no se puede repetir (409).</li>
 *   <li>La placa debe corresponder al tipo: carro ABC123, moto ABC12D (400).</li>
 *   <li>El propietario debe existir (404).</li>
 *   <li>No se puede eliminar un vehículo con historial de parqueo (409).</li>
 * </ul>
 *
 * @author Carlos Eduardo Valero
 */
@Service
public class VehiculoService {

    private static final String RECURSO = "el vehículo";
    private static final String PATRON_CARRO = "^[A-Z]{3}\\d{3}$";
    private static final String PATRON_MOTO = "^[A-Z]{3}\\d{2}[A-Z]$";

    private final VehiculoRepository vehiculoRepository;
    private final SesionParqueoRepository sesionRepository;
    private final PersonaService personaService;

    public VehiculoService(VehiculoRepository vehiculoRepository, SesionParqueoRepository sesionRepository,
                           PersonaService personaService) {
        this.vehiculoRepository = vehiculoRepository;
        this.sesionRepository = sesionRepository;
        this.personaService = personaService;
    }

    /**
     * @param propietarioId filtro opcional por dueño
     * @return lista de vehículos
     */
    public List<Vehiculo> listar(Long propietarioId) {
        return propietarioId == null ? vehiculoRepository.findAll() : vehiculoRepository.findByPropietarioId(propietarioId);
    }

    public Vehiculo buscar(Long id) {
        return vehiculoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException(RECURSO, id));
    }

    @Transactional
    public Vehiculo crear(VehiculoDTO datos) {
        String placa = normalizarPlaca(datos.getPlaca());
        if (vehiculoRepository.existsByPlaca(placa)) {
            throw new ConflictoException("Ya existe un vehículo con la placa " + placa);
        }
        Vehiculo vehiculo = new Vehiculo();
        copiarDatos(datos, placa, vehiculo);
        return vehiculoRepository.save(vehiculo);
    }

    @Transactional
    public Vehiculo actualizar(Long id, VehiculoDTO datos) {
        Vehiculo vehiculo = buscar(id);
        String placa = normalizarPlaca(datos.getPlaca());
        if (vehiculoRepository.existsByPlacaAndIdNot(placa, id)) {
            throw new ConflictoException("Ya existe otro vehículo con la placa " + placa);
        }
        copiarDatos(datos, placa, vehiculo);
        return vehiculoRepository.save(vehiculo);
    }

    @Transactional
    public void eliminar(Long id) {
        Vehiculo vehiculo = buscar(id);
        if (sesionRepository.existsByVehiculoId(id)) {
            throw new ConflictoException("No se puede eliminar el vehículo: tiene sesiones de parqueo registradas");
        }
        vehiculoRepository.delete(vehiculo);
    }

    private void copiarDatos(VehiculoDTO datos, String placa, Vehiculo vehiculo) {
        validarPlacaSegunTipo(placa, datos.getTipo());
        vehiculo.setPlaca(placa);
        vehiculo.setTipo(datos.getTipo());
        vehiculo.setMarca(datos.getMarca());
        vehiculo.setColor(datos.getColor());
        vehiculo.setPropietario(personaService.buscar(datos.getPropietarioId()));
    }

    /** Carro: ABC123 · Moto: ABC12D. */
    private void validarPlacaSegunTipo(String placa, TipoVehiculo tipo) {
        String patron = tipo == TipoVehiculo.CARRO ? PATRON_CARRO : PATRON_MOTO;
        if (!placa.matches(patron)) {
            throw new ReglaNegocioException(tipo == TipoVehiculo.CARRO
                    ? "La placa de un carro debe tener tres letras y tres números (ABC123)"
                    : "La placa de una moto debe tener tres letras, dos números y una letra (ABC12D)");
        }
    }

    /** Quita espacios y pasa a mayúsculas: " klm482 " → "KLM482". */
    private String normalizarPlaca(String placa) {
        return placa.trim().toUpperCase(Locale.ROOT);
    }
}
