package co.edu.sena.controlparkiq.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Entidad de la tabla "unidad_residencial": cada apartamento o casa del conjunto.
 *
 * <p>La combinación torre + apartamento no se puede repetir.</p>
 *
 * @author Carlos Eduardo Valero
 */
@Entity
@Table(name = "unidad_residencial",
        uniqueConstraints = @UniqueConstraint(name = "uk_unidad_torre_apto", columnNames = {"torre", "apartamento"}))
public class UnidadResidencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Torre, bloque o interior (por ejemplo "1" o "B"). */
    @Column(nullable = false, length = 10)
    private String torre;

    /** Número del apartamento o casa (por ejemplo "302"). */
    @Column(nullable = false, length = 10)
    private String apartamento;

    /** Cupos de parqueo que le corresponden a la unidad. */
    @Column(name = "cupos_parqueo", nullable = false)
    private Integer cuposParqueo;

    // ---------------- Getters y setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTorre() {
        return torre;
    }

    public void setTorre(String torre) {
        this.torre = torre;
    }

    public String getApartamento() {
        return apartamento;
    }

    public void setApartamento(String apartamento) {
        this.apartamento = apartamento;
    }

    public Integer getCuposParqueo() {
        return cuposParqueo;
    }

    public void setCuposParqueo(Integer cuposParqueo) {
        this.cuposParqueo = cuposParqueo;
    }
}
