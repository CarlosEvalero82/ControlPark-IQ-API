package co.edu.sena.controlparkiq.dto;

import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

/**
 * Datos para registrar la salida de un vehículo (PUT /api/sesiones/{id}).
 *
 * <p>Ejemplo: {@code {"horaSalida": "2026-10-20T10:30:00"}}. Si se envía {@code {}},
 * se toma la hora actual.</p>
 *
 * @author Carlos Eduardo Valero
 */
public class SalidaDTO {

    @PastOrPresent(message = "La hora de salida no puede ser una fecha futura")
    private LocalDateTime horaSalida;

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }
}
