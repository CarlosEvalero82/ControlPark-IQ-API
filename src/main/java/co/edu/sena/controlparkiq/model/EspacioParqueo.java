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

/**
 * Entidad de la tabla "espacio_parqueo": cada cupo físico del parqueadero.
 *
 * @author Carlos Eduardo Valero
 */
@Entity
@Table(name = "espacio_parqueo")
public class EspacioParqueo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código visible en el piso (por ejemplo "R-01" o "V-03"), único. */
    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    /** Tipo de vehículo que cabe en el espacio. */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vehiculo", nullable = false, length = 5)
    private TipoVehiculo tipoVehiculo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private EstadoEspacio estado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "zona_id", nullable = false)
    private ZonaParqueo zona;

    // ---------------- Getters y setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public EstadoEspacio getEstado() {
        return estado;
    }

    public void setEstado(EstadoEspacio estado) {
        this.estado = estado;
    }

    public ZonaParqueo getZona() {
        return zona;
    }

    public void setZona(ZonaParqueo zona) {
        this.zona = zona;
    }
}
