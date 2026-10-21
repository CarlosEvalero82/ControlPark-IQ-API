package co.edu.sena.controlparkiq.repository;

import co.edu.sena.controlparkiq.model.Persona;
import co.edu.sena.controlparkiq.model.TipoPersona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Acceso a la tabla "persona".
 *
 * @author Carlos Eduardo Valero
 */
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    boolean existsByDocumento(String documento);

    boolean existsByDocumentoAndIdNot(String documento, Long id);

    /** @return true si alguna persona vive en esa unidad */
    boolean existsByUnidadId(Long unidadId);

    /** @return personas de un tipo (residentes o visitantes) */
    List<Persona> findByTipo(TipoPersona tipo);
}
