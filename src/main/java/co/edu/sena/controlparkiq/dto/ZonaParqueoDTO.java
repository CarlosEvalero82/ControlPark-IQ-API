package co.edu.sena.controlparkiq.dto;

import co.edu.sena.controlparkiq.model.TipoZona;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear o actualizar una zona de parqueo.
 *
 * <p>Ejemplo: {@code {"nombre": "Sótano 1", "tipo": "RESIDENTES", "descripcion": "Torres 1 y 2"}}</p>
 *
 * @author Carlos Eduardo Valero
 */
public class ZonaParqueoDTO {

    @NotBlank(message = "El nombre de la zona es obligatorio")
    @Size(min = 3, max = 40, message = "El nombre debe tener entre 3 y 40 caracteres")
    private String nombre;

    @NotNull(message = "El tipo es obligatorio: RESIDENTES o VISITANTES")
    private TipoZona tipo;

    @Size(max = 150, message = "La descripción admite máximo 150 caracteres")
    private String descripcion;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoZona getTipo() {
        return tipo;
    }

    public void setTipo(TipoZona tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
