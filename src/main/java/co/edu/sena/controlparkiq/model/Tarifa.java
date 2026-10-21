package co.edu.sena.controlparkiq.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * Entidad de la tabla "tarifa": valores de cobro por tipo de vehículo.
 *
 * <p>Regla de cobro de ControlPark-IQ: los residentes pagan una mensualidad según
 * el tipo de vehículo; los visitantes pagan al salir según el tipo de vehículo y
 * las horas de permanencia. Los valores son en pesos colombianos (DECIMAL(10,2)).</p>
 *
 * @author Carlos Eduardo Valero
 */
@Entity
@Table(name = "tarifa")
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Una sola tarifa por tipo de vehículo. */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vehiculo", nullable = false, unique = true, length = 5)
    private TipoVehiculo tipoVehiculo;

    /** Valor por hora o fracción para visitantes. */
    @Column(name = "valor_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorHora;

    /** Valor de la mensualidad para residentes. */
    @Column(name = "valor_mensualidad", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorMensualidad;

    // ---------------- Getters y setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public BigDecimal getValorHora() {
        return valorHora;
    }

    public void setValorHora(BigDecimal valorHora) {
        this.valorHora = valorHora;
    }

    public BigDecimal getValorMensualidad() {
        return valorMensualidad;
    }

    public void setValorMensualidad(BigDecimal valorMensualidad) {
        this.valorMensualidad = valorMensualidad;
    }
}
