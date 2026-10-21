package co.edu.sena.controlparkiq.repository;

import co.edu.sena.controlparkiq.model.FacturaVisitante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Acceso a la tabla "factura_visitante".
 *
 * @author Carlos Eduardo Valero
 */
public interface FacturaVisitanteRepository extends JpaRepository<FacturaVisitante, Long> {

    boolean existsBySesionId(Long sesionId);

    List<FacturaVisitante> findByPagada(boolean pagada);
}
