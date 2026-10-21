package co.edu.sena.controlparkiq.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

/**
 * Datos para registrar la entrada de un vehículo (POST /api/sesiones).
 *
 * <p>Ejemplo: {@code {"vehiculoId": 2, "espacioId": 3}}. La hora de entrada es
 * opcional: si no se envía, se toma la hora actual.</p>
 *
 * @author Carlos Eduardo Valero
 */
public class EntradaDTO {

    @NotNull(message = "El vehículo (vehiculoId) es obligatorio")
    private Long vehiculoId;

    @NotNull(message = "El espacio (espacioId) es obligatorio")
    private Long espacioId;

    @PastOrPresent(message = "La hora de entrada no puede ser una fecha futura")
    private LocalDateTime horaEntrada;

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Long vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public Long getEspacioId() {
        return espacioId;
    }

    public void setEspacioId(Long espacioId) {
        this.espacioId = espacioId;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }
}
