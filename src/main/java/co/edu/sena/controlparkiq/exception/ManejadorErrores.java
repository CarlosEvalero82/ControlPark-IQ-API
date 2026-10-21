package co.edu.sena.controlparkiq.exception;

import co.edu.sena.controlparkiq.dto.RespuestaDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

/**
 * Manejo centralizado de errores de la API.
 *
 * <p>Captura las excepciones de todos los controladores y las convierte en
 * respuestas JSON claras con el formato {@code {"exito": false, "mensaje": "..."}},
 * evitando que el cliente reciba errores técnicos o que el servicio se detenga.</p>
 *
 * <pre>
 * 400 · datos inválidos, JSON mal escrito o regla del negocio no cumplida
 * 404 · el registro buscado no existe
 * 409 · dato repetido o registro en uso
 * 415 · el cuerpo no se envió como JSON
 * </pre>
 *
 * @author Carlos Eduardo Valero
 */
@RestControllerAdvice
public class ManejadorErrores {

    /** Mensaje cuando la base de datos rechaza un dato repetido o en uso. */
    public static final String MENSAJE_INTEGRIDAD =
            "El registro ya existe o está relacionado con otros datos";

    /**
     * 400 · Los datos no cumplen las reglas de validación del DTO (@NotBlank, @Size, @Pattern...).
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
     * 400 · El cuerpo no es un JSON válido o trae un valor no permitido
     * (por ejemplo, un tipo de vehículo que no existe).
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
     * 400 · Un parámetro de la ruta o de la URL tiene un formato incorrecto
     * (por ejemplo /api/vehiculos/abc o ?estado=LLENO).
     *
     * @param ex excepción de conversión del parámetro
     * @return respuesta con el nombre del parámetro
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RespuestaDTO> manejarParametroInvalido(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(new RespuestaDTO(false,
                "El valor '" + ex.getValue() + "' no es válido para el parámetro '" + ex.getName() + "'"));
    }

    /**
     * 400 · Los datos tienen la forma correcta pero no cumplen una regla del negocio.
     *
     * @param ex excepción lanzada por un servicio
     * @return respuesta con la regla que no se cumplió
     */
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<RespuestaDTO> manejarReglaNegocio(ReglaNegocioException ex) {
        return ResponseEntity.badRequest().body(new RespuestaDTO(false, ex.getMessage()));
    }

    /**
     * 404 · El registro buscado no existe.
     *
     * @param ex excepción lanzada por un servicio
     * @return respuesta "No existe ... con id ..."
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<RespuestaDTO> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RespuestaDTO(false, ex.getMessage()));
    }

    /**
     * 409 · Dato repetido, espacio ocupado o registro que no se puede borrar.
     *
     * @param ex excepción lanzada por un servicio
     * @return respuesta con la explicación del conflicto
     */
    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<RespuestaDTO> manejarConflicto(ConflictoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new RespuestaDTO(false, ex.getMessage()));
    }

    /**
     * 409 · Respaldo: la base de datos rechazó la operación por un índice único
     * o una llave foránea (por ejemplo, dos registros simultáneos del mismo dato).
     *
     * @param ex excepción de integridad de datos
     * @return respuesta con un mensaje general
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RespuestaDTO> manejarIntegridad(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new RespuestaDTO(false, MENSAJE_INTEGRIDAD));
    }

    /**
     * 415 · El cuerpo se envió en un formato distinto de JSON (por ejemplo, texto plano).
     *
     * @param ex excepción de tipo de contenido
     * @return respuesta indicando que debe enviarse JSON
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<RespuestaDTO> manejarTipoContenido(HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new RespuestaDTO(false,
                "El cuerpo debe enviarse en formato JSON (Content-Type: application/json)"));
    }
}
