package co.edu.sena.controlparkiq.repository;

import co.edu.sena.controlparkiq.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Acceso a la tabla "vehiculo".
 *
 * @author Carlos Eduardo Valero
 */
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    boolean existsByPlaca(String placa);

    boolean existsByPlacaAndIdNot(String placa, Long id);

    /** @return true si la persona tiene al menos un vehículo */
    boolean existsByPropietarioId(Long propietarioId);

    /** @return vehículos de una persona */
    List<Vehiculo> findByPropietarioId(Long propietarioId);
}
