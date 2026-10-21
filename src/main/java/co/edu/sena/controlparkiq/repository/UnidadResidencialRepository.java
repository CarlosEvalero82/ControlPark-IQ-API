package co.edu.sena.controlparkiq.repository;

import co.edu.sena.controlparkiq.model.UnidadResidencial;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a la tabla "unidad_residencial".
 *
 * @author Carlos Eduardo Valero
 */
public interface UnidadResidencialRepository extends JpaRepository<UnidadResidencial, Long> {

    /** @return true si ya existe esa torre + apartamento */
    boolean existsByTorreIgnoreCaseAndApartamentoIgnoreCase(String torre, String apartamento);

    /** @return true si existe esa torre + apartamento en otra unidad distinta de {@code id} */
    boolean existsByTorreIgnoreCaseAndApartamentoIgnoreCaseAndIdNot(String torre, String apartamento, Long id);
}
