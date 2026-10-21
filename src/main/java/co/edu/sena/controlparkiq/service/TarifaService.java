package co.edu.sena.controlparkiq.service;

import co.edu.sena.controlparkiq.dto.TarifaDTO;
import co.edu.sena.controlparkiq.exception.ConflictoException;
import co.edu.sena.controlparkiq.exception.RecursoNoEncontradoException;
import co.edu.sena.controlparkiq.exception.ReglaNegocioException;
import co.edu.sena.controlparkiq.model.Tarifa;
import co.edu.sena.controlparkiq.model.TipoVehiculo;
import co.edu.sena.controlparkiq.repository.TarifaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de las tarifas (HU-07).
 *
 * <p>Reglas: una sola tarifa por tipo de vehículo (409) y valores mayores que 0 (400).</p>
 *
 * @author Carlos Eduardo Valero
 */
@Service
public class TarifaService {

    private static final String RECURSO = "la tarifa";

    private final TarifaRepository tarifaRepository;

    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    public List<Tarifa> listar() {
        return tarifaRepository.findAll();
    }

    public Tarifa buscar(Long id) {
        return tarifaRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException(RECURSO, id));
    }

    /**
     * Busca la tarifa vigente para un tipo de vehículo (la usa la facturación).
     *
     * @param tipoVehiculo CARRO o MOTO
     * @return la tarifa
     * @throws ReglaNegocioException si no hay tarifa configurada (400)
     */
    public Tarifa buscarPorTipo(TipoVehiculo tipoVehiculo) {
        return tarifaRepository.findByTipoVehiculo(tipoVehiculo).orElseThrow(() ->
                new ReglaNegocioException("No hay una tarifa configurada para el tipo de vehículo " + tipoVehiculo));
    }

    @Transactional
    public Tarifa crear(TarifaDTO datos) {
        if (tarifaRepository.existsByTipoVehiculo(datos.getTipoVehiculo())) {
            throw new ConflictoException("Ya existe una tarifa para " + datos.getTipoVehiculo()
                    + "; use PUT para modificarla");
        }
        Tarifa tarifa = new Tarifa();
        copiarDatos(datos, tarifa);
        return tarifaRepository.save(tarifa);
    }

    @Transactional
    public Tarifa actualizar(Long id, TarifaDTO datos) {
        Tarifa tarifa = buscar(id);
        if (tarifaRepository.existsByTipoVehiculoAndIdNot(datos.getTipoVehiculo(), id)) {
            throw new ConflictoException("Ya existe otra tarifa para " + datos.getTipoVehiculo());
        }
        copiarDatos(datos, tarifa);
        return tarifaRepository.save(tarifa);
    }

    @Transactional
    public void eliminar(Long id) {
        tarifaRepository.delete(buscar(id));
    }

    private void copiarDatos(TarifaDTO datos, Tarifa tarifa) {
        tarifa.setTipoVehiculo(datos.getTipoVehiculo());
        tarifa.setValorHora(datos.getValorHora());
        tarifa.setValorMensualidad(datos.getValorMensualidad());
    }
}
