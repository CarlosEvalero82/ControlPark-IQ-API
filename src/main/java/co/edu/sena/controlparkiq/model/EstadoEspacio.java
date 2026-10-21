package co.edu.sena.controlparkiq.model;

/**
 * Estados posibles de un espacio de parqueo.
 *
 * @author Carlos Eduardo Valero
 */
public enum EstadoEspacio {

    /** Libre: se puede asignar a un vehículo. */
    DISPONIBLE,

    /** Tiene un vehículo parqueado (lo asigna el sistema al registrar la entrada). */
    OCUPADO,

    /** Apartado para un residente o visitante. */
    RESERVADO,

    /** Fuera de servicio (mantenimiento, obra, etc.). */
    INHABILITADO
}
