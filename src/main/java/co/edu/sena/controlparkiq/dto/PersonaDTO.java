package co.edu.sena.controlparkiq.dto;

import co.edu.sena.controlparkiq.model.TipoPersona;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear o actualizar una persona (residente o visitante).
 *
 * <p>Ejemplo: {@code {"documento": "1012345678", "nombres": "Laura", "apellidos": "Gómez",
 * "telefono": "3001234567", "correo": "laura@correo.com", "tipo": "RESIDENTE", "unidadId": 1}}</p>
 *
 * @author Carlos Eduardo Valero
 */
public class PersonaDTO {

    @NotBlank(message = "El documento es obligatorio")
    @Pattern(regexp = "^\\d{6,10}$", message = "El documento debe tener entre 6 y 10 dígitos")
    private String documento;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 2, max = 60, message = "Los nombres deben tener entre 2 y 60 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 60, message = "Los apellidos deben tener entre 2 y 60 caracteres")
    private String apellidos;

    @Pattern(regexp = "^3\\d{9}$", message = "El teléfono debe ser un celular de 10 dígitos que empiece por 3")
    private String telefono;

    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 100, message = "El correo admite máximo 100 caracteres")
    private String correo;

    @NotNull(message = "El tipo es obligatorio: RESIDENTE o VISITANTE")
    private TipoPersona tipo;

    /** Id de la unidad residencial (obligatorio si es RESIDENTE). */
    private Long unidadId;

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public TipoPersona getTipo() {
        return tipo;
    }

    public void setTipo(TipoPersona tipo) {
        this.tipo = tipo;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }
}
