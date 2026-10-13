package co.edu.sena.controlparkiq.model;

/**
 * Roles de los usuarios de ControlPark-IQ.
 *
 * @author Carlos Eduardo Valero
 */
public enum Rol {

    /** Administración del conjunto: gestiona usuarios, zonas, espacios y tarifas. */
    ADMINISTRADOR,

    /** Personal de portería: registra entradas y salidas de vehículos. */
    PORTERIA,

    /** Residente: consulta sus vehículos y paga la mensualidad. */
    RESIDENTE
}
