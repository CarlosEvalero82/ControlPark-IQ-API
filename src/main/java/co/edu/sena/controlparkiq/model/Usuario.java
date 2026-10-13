package co.edu.sena.controlparkiq.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entidad que representa la tabla "usuarios" de la base de datos.
 *
 * <p>La contraseña nunca se guarda en texto plano: el servicio la protege
 * con BCrypt antes de asignarla a esta entidad.</p>
 *
 * @author Carlos Eduardo Valero
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    /** Identificador autoincremental. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre con el que el usuario inicia sesión (único, en minúsculas). */
    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 20)
    private String nombreUsuario;

    /** Contraseña protegida con BCrypt (60 caracteres). */
    @Column(nullable = false, length = 60)
    private String contrasena;

    /** Rol dentro de ControlPark-IQ. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Rol rol;

    /** Indica si el usuario puede iniciar sesión (por defecto, activo). */
    @Column(nullable = false)
    private boolean activo = true;

    /** Fecha y hora en que se registró el usuario. */
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    /** Fecha y hora del último inicio de sesión exitoso. */
    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    /**
     * Asigna valores por defecto justo antes de insertar el registro.
     */
    @PrePersist
    protected void asignarValoresPorDefecto() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.rol == null) {
            this.rol = Rol.RESIDENTE;
        }
    }

    // ---------------- Getters y setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
}
