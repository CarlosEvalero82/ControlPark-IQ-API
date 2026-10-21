package co.edu.sena.controlparkiq.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entidad de la tabla "sesion_parqueo": cada entrada y salida de un vehículo.
 *
 * <p>Se crea ABIERTA cuando portería registra la entrada y pasa a CERRADA
 * cuando registra la salida.</p>
 *
 * @author Carlos Eduardo Valero
 */
@Entity
@Table(name = "sesion_parqueo")
public class SesionParqueo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "espacio_id", nullable = false)
    private EspacioParqueo espacio;

    @Column(name = "hora_entrada", nullable = false)
    private LocalDateTime horaEntrada;

    /** Vacía mientras el vehículo sigue en el parqueadero. */
    @Column(name = "hora_salida")
    private LocalDateTime horaSalida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private EstadoSesion estado;

    // ---------------- Getters y setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public EspacioParqueo getEspacio() {
        return espacio;
    }

    public void setEspacio(EspacioParqueo espacio) {
        this.espacio = espacio;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public EstadoSesion getEstado() {
        return estado;
    }

    public void setEstado(EstadoSesion estado) {
        this.estado = estado;
    }
}
