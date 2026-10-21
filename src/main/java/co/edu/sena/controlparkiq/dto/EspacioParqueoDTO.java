package co.edu.sena.controlparkiq.dto;

import co.edu.sena.controlparkiq.model.EstadoEspacio;
import co.edu.sena.controlparkiq.model.TipoVehiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Datos para crear o actualizar un espacio de parqueo.
 *
 * <p>Ejemplo: {@code {"codigo": "R-01", "tipoVehiculo": "CARRO", "zonaId": 1}}.
 * El estado es opcional: si no se envía, el espacio queda DISPONIBLE.</p>
 *
 * @author Carlos Eduardo Valero
 */
public class EspacioParqueoDTO {

    @NotBlank(message = "El código del espacio es obligatorio")
    @Pattern(regexp = "^[A-Za-z]{1,3}-\\d{1,3}$", message = "El código debe tener el formato LETRAS-NÚMERO, por ejemplo R-01")
    private String codigo;

    @NotNull(message = "El tipo de vehículo es obligatorio: CARRO o MOTO")
    private TipoVehiculo tipoVehiculo;

    /** Opcional: DISPONIBLE, RESERVADO o INHABILITADO (OCUPADO lo asigna el sistema). */
    private EstadoEspacio estado;

    @NotNull(message = "La zona (zonaId) es obligatoria")
    private Long zonaId;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public EstadoEspacio getEstado() {
        return estado;
    }

    public void setEstado(EstadoEspacio estado) {
        this.estado = estado;
    }

    public Long getZonaId() {
        return zonaId;
    }

    public void setZonaId(Long zonaId) {
        this.zonaId = zonaId;
    }
}
