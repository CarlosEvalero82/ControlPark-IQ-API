package co.edu.sena.controlparkiq.service;

import co.edu.sena.controlparkiq.dto.ZonaParqueoDTO;
import co.edu.sena.controlparkiq.exception.ConflictoException;
import co.edu.sena.controlparkiq.exception.RecursoNoEncontradoException;
import co.edu.sena.controlparkiq.model.ZonaParqueo;
import co.edu.sena.controlparkiq.repository.EspacioParqueoRepository;
import co.edu.sena.controlparkiq.repository.ZonaParqueoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de las zonas de parqueo (HU-05).
 *
 * <p>Reglas: el nombre no se repite (409) y no se puede eliminar una zona
 * que tenga espacios (409).</p>
 *
 * @author Carlos Eduardo Valero
 */
@Service
public class ZonaParqueoService {

    private static final String RECURSO = "la zona de parqueo";

    private final ZonaParqueoRepository zonaRepository;
    private final EspacioParqueoRepository espacioRepository;

    public ZonaParqueoService(ZonaParqueoRepository zonaRepository, EspacioParqueoRepository espacioRepository) {
        this.zonaRepository = zonaRepository;
        this.espacioRepository = espacioRepository;
    }

    public List<ZonaParqueo> listar() {
        return zonaRepository.findAll();
    }

    public ZonaParqueo buscar(Long id) {
        return zonaRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException(RECURSO, id));
    }

    @Transactional
    public ZonaParqueo crear(ZonaParqueoDTO datos) {
        if (zonaRepository.existsByNombreIgnoreCase(datos.getNombre().trim())) {
            throw new ConflictoException("Ya existe una zona llamada " + datos.getNombre().trim());
        }
        ZonaParqueo zona = new ZonaParqueo();
        copiarDatos(datos, zona);
        return zonaRepository.save(zona);
    }

    @Transactional
    public ZonaParqueo actualizar(Long id, ZonaParqueoDTO datos) {
        ZonaParqueo zona = buscar(id);
        if (zonaRepository.existsByNombreIgnoreCaseAndIdNot(datos.getNombre().trim(), id)) {
            throw new ConflictoException("Ya existe otra zona llamada " + datos.getNombre().trim());
        }
        copiarDatos(datos, zona);
        return zonaRepository.save(zona);
    }

    @Transactional
    public void eliminar(Long id) {
        ZonaParqueo zona = buscar(id);
        if (espacioRepository.existsByZonaId(id)) {
            throw new ConflictoException("No se puede eliminar la zona: tiene espacios de parqueo");
        }
        zonaRepository.delete(zona);
    }

    private void copiarDatos(ZonaParqueoDTO datos, ZonaParqueo zona) {
        zona.setNombre(datos.getNombre().trim());
        zona.setTipo(datos.getTipo());
        zona.setDescripcion(datos.getDescripcion());
    }
}
