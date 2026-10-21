package co.edu.sena.controlparkiq.exception;

/**
 * Se lanza cuando la petición choca con datos que ya existen: un dato repetido
 * (placa, documento, código de espacio), un espacio ocupado o un registro que
 * no se puede borrar porque otros datos dependen de él.
 * El {@link ManejadorErrores} la convierte en una respuesta 409 (Conflict).
 *
 * @author Carlos Eduardo Valero
 */
public class ConflictoException extends RuntimeException {

    /**
     * @param mensaje explicación del conflicto para el usuario
     */
    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
