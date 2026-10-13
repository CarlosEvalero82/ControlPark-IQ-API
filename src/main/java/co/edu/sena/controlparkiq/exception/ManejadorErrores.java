package co.edu.sena.controlparkiq.exception;

import co.edu.sena.controlparkiq.controller.AuthController;
import co.edu.sena.controlparkiq.dto.RespuestaDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Manejo centralizado de errores de la API.
 *
 * <p>Captura las excepciones y las convierte en respuestas JSON claras,
 * evitando que el cliente reciba errores técnicos o que el servicio se detenga.
 * Los demás casos (415 tipo de contenido, 404 ruta inexistente, 500) los
 * responde Spring Boot con su manejo por defecto.</p>
 *
 * @author Carlos Eduardo Valero
 */
@RestControllerAdvice
public class ManejadorErrores {

    /**
     * 400 · Los datos no cumplen las reglas de validación (por ejemplo, usuario vacío).
     *
     * @param ex excepción lanzada por @Valid
     * @return respuesta con la lista de errores
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaDTO> manejarDatosInvalidos(MethodArgumentNotValidException ex) {
        List<String> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .sorted()
                .toList();
        return ResponseEntity.badRequest().body(new RespuestaDTO(false, "Datos inválidos", errores));
    }

    /**
     * 400 · El cuerpo no es un JSON válido o trae un rol inexistente.
     *
     * @param ex excepción de lectura del JSON
     * @return respuesta con el mensaje de error
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RespuestaDTO> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new RespuestaDTO(false,
                "El cuerpo de la petición no es un JSON válido o tiene valores no permitidos"));
    }

    /**
     * 409 · Dos registros simultáneos del mismo usuario (lo detecta el índice único de MySQL).
     *
     * @param ex excepción de integridad de datos
     * @return respuesta "El usuario ya existe"
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RespuestaDTO> manejarDuplicado(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new RespuestaDTO(false, AuthController.MENSAJE_USUARIO_EXISTE));
    }
}
