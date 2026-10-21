package co.edu.sena.controlparkiq.repository;

import co.edu.sena.controlparkiq.model.ZonaParqueo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a la tabla "zona_parqueo".
 *
 * @author Carlos Eduardo Valero
 */
public interface ZonaParqueoRepository extends JpaRepository<ZonaParqueo, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
