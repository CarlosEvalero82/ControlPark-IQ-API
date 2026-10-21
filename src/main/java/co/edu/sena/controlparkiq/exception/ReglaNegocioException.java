package co.edu.sena.controlparkiq.exception;

/**
 * Se lanza cuando los datos tienen la forma correcta pero no cumplen una regla
 * del negocio de ControlPark-IQ (por ejemplo, una salida anterior a la entrada,
 * o una placa de carro registrada como moto).
 * El {@link ManejadorErrores} la convierte en una respuesta 400 (Bad Request).
 *
 * @author Carlos Eduardo Valero
 */
public class ReglaNegocioException extends RuntimeException {

    /**
     * @param mensaje regla que no se cumplió, explicada para el usuario
     */
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
