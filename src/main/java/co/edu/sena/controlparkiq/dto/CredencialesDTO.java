package co.edu.sena.controlparkiq.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Datos que envía el cliente para iniciar sesión (body de POST /api/auth/login).
 *
 * <p>En el inicio de sesión solo se exige que lleguen los dos campos;
 * la verificación real se hace contra la base de datos.</p>
 *
 * @author Carlos Eduardo Valero
 */
public class CredencialesDTO {

    /** Nombre de usuario registrado. */
    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    /** Contraseña escrita por el usuario. */
    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

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
}
