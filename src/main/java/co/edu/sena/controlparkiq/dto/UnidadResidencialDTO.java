package co.edu.sena.controlparkiq.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear o actualizar una unidad residencial.
 *
 * <p>Ejemplo: {@code {"torre": "1", "apartamento": "302", "cuposParqueo": 1}}</p>
 *
 * @author Carlos Eduardo Valero
 */
public class UnidadResidencialDTO {

    @NotBlank(message = "La torre es obligatoria")
    @Size(max = 10, message = "La torre admite máximo 10 caracteres")
    private String torre;

    @NotBlank(message = "El apartamento es obligatorio")
    @Size(max = 10, message = "El apartamento admite máximo 10 caracteres")
    private String apartamento;

    @NotNull(message = "Los cupos de parqueo son obligatorios")
    @Min(value = 0, message = "Los cupos de parqueo no pueden ser negativos")
    @Max(value = 4, message = "Una unidad puede tener máximo 4 cupos de parqueo")
    private Integer cuposParqueo;

    public String getTorre() {
        return torre;
    }

    public void setTorre(String torre) {
        this.torre = torre;
    }

    public String getApartamento() {
        return apartamento;
    }

    public void setApartamento(String apartamento) {
        this.apartamento = apartamento;
    }

    public Integer getCuposParqueo() {
        return cuposParqueo;
    }

    public void setCuposParqueo(Integer cuposParqueo) {
        this.cuposParqueo = cuposParqueo;
    }
}
