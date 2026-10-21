package co.edu.sena.controlparkiq.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Datos para generar la factura de un visitante (POST /api/facturas).
 *
 * <p>Ejemplo: {@code {"sesionId": 2}}. El valor lo calcula el sistema.</p>
 *
 * @author Carlos Eduardo Valero
 */
public class FacturaDTO {

    @NotNull(message = "La sesión (sesionId) es obligatoria")
    private Long sesionId;

    public Long getSesionId() {
        return sesionId;
    }

    public void setSesionId(Long sesionId) {
        this.sesionId = sesionId;
    }
}
