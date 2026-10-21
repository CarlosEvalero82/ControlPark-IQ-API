package co.edu.sena.controlparkiq.dto;

import co.edu.sena.controlparkiq.model.TipoVehiculo;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Datos para crear o actualizar una tarifa.
 *
 * <p>Ejemplo: {@code {"tipoVehiculo": "CARRO", "valorHora": 3000, "valorMensualidad": 80000}}</p>
 *
 * @author Carlos Eduardo Valero
 */
public class TarifaDTO {

    @NotNull(message = "El tipo de vehículo es obligatorio: CARRO o MOTO")
    private TipoVehiculo tipoVehiculo;

    @NotNull(message = "El valor por hora es obligatorio")
    @Positive(message = "El valor por hora debe ser mayor que 0")
    @Digits(integer = 8, fraction = 2, message = "El valor por hora admite máximo 8 enteros y 2 decimales")
    private BigDecimal valorHora;

    @NotNull(message = "El valor de la mensualidad es obligatorio")
    @Positive(message = "El valor de la mensualidad debe ser mayor que 0")
    @Digits(integer = 8, fraction = 2, message = "La mensualidad admite máximo 8 enteros y 2 decimales")
    private BigDecimal valorMensualidad;

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public BigDecimal getValorHora() {
        return valorHora;
    }

    public void setValorHora(BigDecimal valorHora) {
        this.valorHora = valorHora;
    }

    public BigDecimal getValorMensualidad() {
        return valorMensualidad;
    }

    public void setValorMensualidad(BigDecimal valorMensualidad) {
        this.valorMensualidad = valorMensualidad;
    }
}
