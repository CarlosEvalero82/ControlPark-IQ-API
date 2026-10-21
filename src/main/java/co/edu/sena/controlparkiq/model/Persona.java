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
 * Entidad de la tabla "persona": residentes y visitantes del conjunto,
 * propietarios de los vehículos.
 *
 * @author Carlos Eduardo Valero
 */
@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Número de documento de identidad (único). */
    @Column(nullable = false, unique = true, length = 10)
    private String documento;

    @Column(nullable = false, length = 60)
    private String nombres;

    @Column(nullable = false, length = 60)
    private String apellidos;

    /** Celular de 10 dígitos (opcional). */
    @Column(length = 10)
    private String telefono;

    /** Correo electrónico (opcional). */
    @Column(length = 100)
    private String correo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoPersona tipo;

    /** Unidad donde vive. Obligatoria para residentes; vacía para visitantes. */
    @ManyToOne
    @JoinColumn(name = "unidad_id")
    private UnidadResidencial unidad;

    // ---------------- Getters y setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public TipoPersona getTipo() {
        return tipo;
    }

    public void setTipo(TipoPersona tipo) {
        this.tipo = tipo;
    }

    public UnidadResidencial getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadResidencial unidad) {
        this.unidad = unidad;
    }
}
