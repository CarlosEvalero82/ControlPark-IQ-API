package co.edu.sena.controlparkiq.service;

import co.edu.sena.controlparkiq.dto.UnidadResidencialDTO;
import co.edu.sena.controlparkiq.exception.ConflictoException;
import co.edu.sena.controlparkiq.exception.RecursoNoEncontradoException;
import co.edu.sena.controlparkiq.model.UnidadResidencial;
import co.edu.sena.controlparkiq.repository.PersonaRepository;
import co.edu.sena.controlparkiq.repository.UnidadResidencialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de las unidades residenciales (apartamentos o casas del conjunto).
 *
 * @author Carlos Eduardo Valero
 */
@Service
public class UnidadResidencialService {

    private static final String RECURSO = "la unidad residencial";

    private final UnidadResidencialRepository unidadRepository;
    private final PersonaRepository personaRepository;

    public UnidadResidencialService(UnidadResidencialRepository unidadRepository, PersonaRepository personaRepository) {
        this.unidadRepository = unidadRepository;
        this.personaRepository = personaRepository;
    }

    /** @return todas las unidades */
    public List<UnidadResidencial> listar() {
        return unidadRepository.findAll();
    }

    /**
     * @param id identificador
     * @return la unidad encontrada
     * @throws RecursoNoEncontradoException si no existe (404)
     */
    public UnidadResidencial buscar(Long id) {
        return unidadRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException(RECURSO, id));
    }

    /**
     * Crea una unidad. La torre + apartamento no se puede repetir (409).
     *
     * @param datos datos validados
     * @return la unidad creada
     */
    @Transactional
    public UnidadResidencial crear(UnidadResidencialDTO datos) {
        if (unidadRepository.existsByTorreIgnoreCaseAndApartamentoIgnoreCase(datos.getTorre().trim(), datos.getApartamento().trim())) {
            throw new ConflictoException(mensajeRepetida(datos));
        }
        UnidadResidencial unidad = new UnidadResidencial();
        copiarDatos(datos, unidad);
        return unidadRepository.save(unidad);
    }

    /**
     * Actualiza una unidad existente.
     *
     * @param id    identificador
     * @param datos datos validados
     * @return la unidad actualizada
     */
    @Transactional
    public UnidadResidencial actualizar(Long id, UnidadResidencialDTO datos) {
        UnidadResidencial unidad = buscar(id);
        if (unidadRepository.existsByTorreIgnoreCaseAndApartamentoIgnoreCaseAndIdNot(datos.getTorre().trim(), datos.getApartamento().trim(), id)) {
            throw new ConflictoException(mensajeRepetida(datos));
        }
        copiarDatos(datos, unidad);
        return unidadRepository.save(unidad);
    }

    /**
     * Elimina una unidad. No se permite si tiene residentes asignados (409).
     *
     * @param id identificador
     */
    @Transactional
    public void eliminar(Long id) {
        UnidadResidencial unidad = buscar(id);
        if (personaRepository.existsByUnidadId(id)) {
            throw new ConflictoException("No se puede eliminar la unidad: tiene residentes asignados");
        }
        unidadRepository.delete(unidad);
    }

    private void copiarDatos(UnidadResidencialDTO datos, UnidadResidencial unidad) {
        unidad.setTorre(datos.getTorre().trim().toUpperCase());
        unidad.setApartamento(datos.getApartamento().trim().toUpperCase());
        unidad.setCuposParqueo(datos.getCuposParqueo());
    }

    private String mensajeRepetida(UnidadResidencialDTO datos) {
        return "Ya existe la unidad torre " + datos.getTorre().trim() + " apartamento " + datos.getApartamento().trim();
    }
}
