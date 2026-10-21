package co.edu.sena.controlparkiq.service;

import co.edu.sena.controlparkiq.dto.PersonaDTO;
import co.edu.sena.controlparkiq.exception.ConflictoException;
import co.edu.sena.controlparkiq.exception.RecursoNoEncontradoException;
import co.edu.sena.controlparkiq.exception.ReglaNegocioException;
import co.edu.sena.controlparkiq.model.Persona;
import co.edu.sena.controlparkiq.model.TipoPersona;
import co.edu.sena.controlparkiq.repository.PersonaRepository;
import co.edu.sena.controlparkiq.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de las personas (residentes y visitantes).
 *
 * <p>Reglas del negocio:</p>
 * <ul>
 *   <li>El documento no se puede repetir (409).</li>
 *   <li>Un residente debe tener una unidad residencial que exista (400 / 404).</li>
 *   <li>Un visitante no se asocia a ninguna unidad.</li>
 *   <li>No se puede eliminar una persona que tenga vehículos registrados (409).</li>
 * </ul>
 *
 * @author Carlos Eduardo Valero
 */
@Service
public class PersonaService {

    private static final String RECURSO = "la persona";

    private final PersonaRepository personaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final UnidadResidencialService unidadService;

    public PersonaService(PersonaRepository personaRepository, VehiculoRepository vehiculoRepository,
                          UnidadResidencialService unidadService) {
        this.personaRepository = personaRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.unidadService = unidadService;
    }

    /**
     * @param tipo filtro opcional (RESIDENTE o VISITANTE); si es null trae todas
     * @return lista de personas
     */
    public List<Persona> listar(TipoPersona tipo) {
        return tipo == null ? personaRepository.findAll() : personaRepository.findByTipo(tipo);
    }

    public Persona buscar(Long id) {
        return personaRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException(RECURSO, id));
    }

    @Transactional
    public Persona crear(PersonaDTO datos) {
        if (personaRepository.existsByDocumento(datos.getDocumento())) {
            throw new ConflictoException("Ya existe una persona con el documento " + datos.getDocumento());
        }
        Persona persona = new Persona();
        copiarDatos(datos, persona);
        return personaRepository.save(persona);
    }

    @Transactional
    public Persona actualizar(Long id, PersonaDTO datos) {
        Persona persona = buscar(id);
        if (personaRepository.existsByDocumentoAndIdNot(datos.getDocumento(), id)) {
            throw new ConflictoException("Ya existe otra persona con el documento " + datos.getDocumento());
        }
        copiarDatos(datos, persona);
        return personaRepository.save(persona);
    }

    @Transactional
    public void eliminar(Long id) {
        Persona persona = buscar(id);
        if (vehiculoRepository.existsByPropietarioId(id)) {
            throw new ConflictoException("No se puede eliminar la persona: tiene vehículos registrados");
        }
        personaRepository.delete(persona);
    }

    /** Pasa los datos del DTO a la entidad aplicando las reglas de la unidad. */
    private void copiarDatos(PersonaDTO datos, Persona persona) {
        persona.setDocumento(datos.getDocumento());
        persona.setNombres(datos.getNombres().trim());
        persona.setApellidos(datos.getApellidos().trim());
        persona.setTelefono(datos.getTelefono());
        persona.setCorreo(datos.getCorreo() == null ? null : datos.getCorreo().trim().toLowerCase());
        persona.setTipo(datos.getTipo());
        if (datos.getTipo() == TipoPersona.RESIDENTE) {
            if (datos.getUnidadId() == null) {
                throw new ReglaNegocioException("Un residente debe tener una unidad residencial (unidadId)");
            }
            persona.setUnidad(unidadService.buscar(datos.getUnidadId()));
        } else {
            persona.setUnidad(null);
        }
    }
}
