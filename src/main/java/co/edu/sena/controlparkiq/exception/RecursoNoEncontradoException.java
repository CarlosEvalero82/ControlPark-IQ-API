package co.edu.sena.controlparkiq.exception;

/**
 * Se lanza cuando se busca un registro por su id y no existe.
 * El {@link ManejadorErrores} la convierte en una respuesta 404 (Not Found).
 *
 * @author Carlos Eduardo Valero
 */
public class RecursoNoEncontradoException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje como "No existe el vehículo con id 999".
     *
     * @param recurso nombre del recurso (por ejemplo "el vehículo")
     * @param id      identificador buscado
     */
    public RecursoNoEncontradoException(String recurso, Long id) {
        super("No existe " + recurso + " con id " + id);
    }
}
