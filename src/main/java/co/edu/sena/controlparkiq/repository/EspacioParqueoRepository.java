package co.edu.sena.controlparkiq.repository;

import co.edu.sena.controlparkiq.model.EspacioParqueo;
import co.edu.sena.controlparkiq.model.EstadoEspacio;
import co.edu.sena.controlparkiq.model.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Acceso a la tabla "espacio_parqueo".
 *
 * @author Carlos Eduardo Valero
 */
public interface EspacioParqueoRepository extends JpaRepository<EspacioParqueo, Long> {

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Long id);

    /** @return true si la zona tiene espacios */
    boolean existsByZonaId(Long zonaId);

    List<EspacioParqueo> findByEstado(EstadoEspacio estado);

    List<EspacioParqueo> findByTipoVehiculo(TipoVehiculo tipoVehiculo);

    List<EspacioParqueo> findByEstadoAndTipoVehiculo(EstadoEspacio estado, TipoVehiculo tipoVehiculo);
}
