package co.edu.sena.controlparkiq.repository;

import co.edu.sena.controlparkiq.model.EstadoSesion;
import co.edu.sena.controlparkiq.model.SesionParqueo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Acceso a la tabla "sesion_parqueo".
 *
 * @author Carlos Eduardo Valero
 */
public interface SesionParqueoRepository extends JpaRepository<SesionParqueo, Long> {

    boolean existsByVehiculoId(Long vehiculoId);

    boolean existsByEspacioId(Long espacioId);

    /** @return true si el vehículo ya está dentro del parqueadero */
    boolean existsByVehiculoIdAndEstado(Long vehiculoId, EstadoSesion estado);

    List<SesionParqueo> findByEstado(EstadoSesion estado);

    /** @return sesiones cuya entrada está entre las dos fechas (historial del día) */
    List<SesionParqueo> findByHoraEntradaBetween(LocalDateTime desde, LocalDateTime hasta);
}
