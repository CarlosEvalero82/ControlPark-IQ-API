package co.edu.sena.controlparkiq.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Respuesta JSON que devuelve la API en todos los casos.
 *
 * <p>Ejemplo: {@code {"exito": true, "mensaje": "Autenticación satisfactoria"}}.
 * El campo "errores" solo aparece cuando hay errores de validación.</p>
 *
 * @author Carlos Eduardo Valero
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespuestaDTO {

    /** true si la operación salió bien. */
    private final boolean exito;

    /** Mensaje para el usuario. */
    private final String mensaje;

    /** Lista de errores de validación (puede ser null). */
    private final List<String> errores;

    /**
     * Crea una respuesta sin lista de errores.
     *
     * @param exito   resultado de la operación
     * @param mensaje mensaje para el usuario
     */
    public RespuestaDTO(boolean exito, String mensaje) {
        this(exito, mensaje, null);
    }

    /**
     * Crea una respuesta con lista de errores.
     *
     * @param exito   resultado de la operación
     * @param mensaje mensaje para el usuario
     * @param errores detalle de los errores de validación
     */
    public RespuestaDTO(boolean exito, String mensaje, List<String> errores) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.errores = errores;
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public List<String> getErrores() {
        return errores;
    }
}
