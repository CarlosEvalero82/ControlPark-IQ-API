package co.edu.sena.controlparkiq.repository;

import co.edu.sena.controlparkiq.model.Tarifa;
import co.edu.sena.controlparkiq.model.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a la tabla "tarifa".
 *
 * @author Carlos Eduardo Valero
 */
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {

    boolean existsByTipoVehiculo(TipoVehiculo tipoVehiculo);

    boolean existsByTipoVehiculoAndIdNot(TipoVehiculo tipoVehiculo, Long id);

    Optional<Tarifa> findByTipoVehiculo(TipoVehiculo tipoVehiculo);
}
