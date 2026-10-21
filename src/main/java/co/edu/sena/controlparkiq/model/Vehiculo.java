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
 * Entidad de la tabla "vehiculo": carros y motos de residentes y visitantes.
 *
 * @author Carlos Eduardo Valero
 */
@Entity
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Placa en mayúsculas, sin espacios ni guiones (única). */
    @Column(nullable = false, unique = true, length = 6)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 5)
    private TipoVehiculo tipo;

    @Column(length = 30)
    private String marca;

    @Column(length = 20)
    private String color;

    /** Persona dueña del vehículo. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Persona propietario;

    // ---------------- Getters y setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Persona getPropietario() {
        return propietario;
    }

    public void setPropietario(Persona propietario) {
        this.propietario = propietario;
    }
}
