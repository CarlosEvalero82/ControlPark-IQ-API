package co.edu.sena.controlparkiq.model;

/**
 * Estados de una sesión de parqueo.
 *
 * @author Carlos Eduardo Valero
 */
public enum EstadoSesion {

    /** El vehículo entró y todavía está en el parqueadero. */
    ABIERTA,

    /** El vehículo ya salió (se registró la hora de salida). */
    CERRADA
}
