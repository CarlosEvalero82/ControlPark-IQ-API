package co.edu.sena.controlparkiq.dto;

import co.edu.sena.controlparkiq.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos que envía el cliente para registrarse (body de POST /api/auth/registro).
 *
 * <p>Las anotaciones de validación se revisan automáticamente gracias a
 * {@code @Valid} en el controlador. Si alguna regla no se cumple, la API
 * responde 400 (Bad Request) con la lista de errores.</p>
 *
 * @author Carlos Eduardo Valero
 */
public class RegistroDTO {

    /** Nombre de usuario: 4 a 20 caracteres (letras, números, punto o guion bajo). */
    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 4, max = 20, message = "El usuario debe tener entre 4 y 20 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9._]*$", message = "El usuario solo admite letras, números, punto o guion bajo")
    private String usuario;

    /** Contraseña: 8 a 64 caracteres con al menos una letra y un número. */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).*$", message = "La contraseña debe incluir al menos una letra y un número")
    private String contrasena;

    /** Rol opcional (ADMINISTRADOR, PORTERIA o RESIDENTE). Si no llega, queda RESIDENTE. */
    private Rol rol;

    // ---------------- Getters y setters ----------------

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
