package co.edu.sena.controlparkiq.dto;

import co.edu.sena.controlparkiq.model.Rol;
import jakarta.validation.constraints.NotNull;

/**
 * Datos que el administrador puede cambiar de un usuario (PUT /api/usuarios/{id}).
 *
 * <p>Ejemplo: {@code {"rol": "PORTERIA", "activo": false}}. El nombre de usuario
 * y la contraseña no se cambian por este servicio.</p>
 *
 * @author Carlos Eduardo Valero
 */
public class UsuarioActualizarDTO {

    @NotNull(message = "El rol es obligatorio: ADMINISTRADOR, PORTERIA o RESIDENTE")
    private Rol rol;

    @NotNull(message = "El campo activo es obligatorio (true o false)")
    private Boolean activo;

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
