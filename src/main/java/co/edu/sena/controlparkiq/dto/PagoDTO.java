package co.edu.sena.controlparkiq.dto;

import co.edu.sena.controlparkiq.model.MetodoPago;
import jakarta.validation.constraints.NotNull;

/**
 * Datos para registrar el pago de una factura (PUT /api/facturas/{id}).
 *
 * <p>Ejemplo: {@code {"metodoPago": "EFECTIVO"}}</p>
 *
 * @author Carlos Eduardo Valero
 */
public class PagoDTO {

    @NotNull(message = "El método de pago es obligatorio: EFECTIVO, TARJETA o TRANSFERENCIA")
    private MetodoPago metodoPago;

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }
}
