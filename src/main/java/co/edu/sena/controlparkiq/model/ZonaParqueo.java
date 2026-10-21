package co.edu.sena.controlparkiq.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad de la tabla "zona_parqueo": sectores del parqueadero
 * (por ejemplo "Sótano 1 - Residentes" o "Bahía visitantes").
 *
 * @author Carlos Eduardo Valero
 */
@Entity
@Table(name = "zona_parqueo")
public class ZonaParqueo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre único de la zona. */
    @Column(nullable = false, unique = true, length = 40)
    private String nombre;

    /** Si la zona es para residentes o para visitantes. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoZona tipo;

    @Column(length = 150)
    private String descripcion;

    // ---------------- Getters y setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoZona getTipo() {
        return tipo;
    }

    public void setTipo(TipoZona tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
