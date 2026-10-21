package co.edu.sena.controlparkiq.dto;

import co.edu.sena.controlparkiq.model.TipoVehiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos para registrar o actualizar un vehículo.
 *
 * <p>Ejemplo: {@code {"placa": "KLM482", "tipo": "CARRO", "marca": "Mazda",
 * "color": "Gris", "propietarioId": 1}}</p>
 *
 * @author Carlos Eduardo Valero
 */
public class VehiculoDTO {

    /** Formato colombiano: ABC123 (carro) o ABC12D (moto). Se aceptan minúsculas. */
    @NotBlank(message = "La placa es obligatoria")
    @Pattern(regexp = "^[A-Za-z]{3}\\d{2}[A-Za-z0-9]$",
            message = "La placa debe tener el formato ABC123 (carro) o ABC12D (moto)")
    private String placa;

    @NotNull(message = "El tipo es obligatorio: CARRO o MOTO")
    private TipoVehiculo tipo;

    @Size(max = 30, message = "La marca admite máximo 30 caracteres")
    private String marca;

    @Size(max = 20, message = "El color admite máximo 20 caracteres")
    private String color;

    @NotNull(message = "El propietario (propietarioId) es obligatorio")
    private Long propietarioId;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(Long propietarioId) {
        this.propietarioId = propietarioId;
    }
}
