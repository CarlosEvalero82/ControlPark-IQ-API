package co.edu.sena.controlparkiq.repository;

import co.edu.sena.controlparkiq.model.Rol;
import co.edu.sena.controlparkiq.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a la tabla "usuarios".
 *
 * <p>Spring Data JPA crea las consultas automáticamente a partir del
 * nombre de cada método (no hay que escribir SQL).</p>
 *
 * @author Carlos Eduardo Valero
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Indica si ya existe un usuario con ese nombre.
     *
     * @param nombreUsuario nombre a buscar
     * @return true si ya está registrado
     */
    boolean existsByNombreUsuario(String nombreUsuario);

    /**
     * Busca un usuario por su nombre.
     *
     * @param nombreUsuario nombre a buscar
     * @return el usuario, o vacío si no existe
     */
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    /**
     * Cuenta los usuarios de un rol (se usa para no dejar el sistema sin administrador).
     *
     * @param rol rol a contar
     * @return cantidad de usuarios con ese rol
     */
    long countByRol(Rol rol);
}
