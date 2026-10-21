package co.edu.sena.controlparkiq.service;

import co.edu.sena.controlparkiq.dto.RegistroDTO;
import co.edu.sena.controlparkiq.dto.UsuarioActualizarDTO;
import co.edu.sena.controlparkiq.exception.ConflictoException;
import co.edu.sena.controlparkiq.exception.RecursoNoEncontradoException;
import co.edu.sena.controlparkiq.model.Rol;
import co.edu.sena.controlparkiq.model.Usuario;
import co.edu.sena.controlparkiq.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Gestión de usuarios por parte del administrador (HU-02).
 *
 * <p>El registro reutiliza el {@link AuthService} de la evidencia EV01
 * (validaciones y contraseña protegida con BCrypt). Regla adicional: el sistema
 * no puede quedarse sin un administrador activo (409).</p>
 *
 * @author Carlos Eduardo Valero
 */
@Service
public class UsuarioService {

    private static final String RECURSO = "el usuario";

    private final UsuarioRepository usuarioRepository;
    private final AuthService authService;

    public UsuarioService(UsuarioRepository usuarioRepository, AuthService authService) {
        this.usuarioRepository = usuarioRepository;
        this.authService = authService;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException(RECURSO, id));
    }

    /**
     * Crea un usuario con las mismas reglas del registro de la EV01.
     *
     * @param datos usuario, contraseña y rol
     * @return el usuario creado (sin la contraseña en el JSON)
     */
    @Transactional
    public Usuario crear(RegistroDTO datos) {
        if (!authService.registrar(datos)) {
            throw new ConflictoException("El usuario ya existe");
        }
        return usuarioRepository.findByNombreUsuario(datos.getUsuario().trim().toLowerCase()).orElseThrow();
    }

    /**
     * Cambia el rol o activa/inactiva un usuario.
     *
     * @param id    id del usuario
     * @param datos rol y estado
     * @return el usuario actualizado
     */
    @Transactional
    public Usuario actualizar(Long id, UsuarioActualizarDTO datos) {
        Usuario usuario = buscar(id);
        boolean dejaDeSerAdmin = usuario.getRol() == Rol.ADMINISTRADOR
                && (datos.getRol() != Rol.ADMINISTRADOR || !datos.getActivo());
        if (dejaDeSerAdmin) {
            validarQueQuedeOtroAdministrador();
        }
        usuario.setRol(datos.getRol());
        usuario.setActivo(datos.getActivo());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = buscar(id);
        if (usuario.getRol() == Rol.ADMINISTRADOR) {
            validarQueQuedeOtroAdministrador();
        }
        usuarioRepository.delete(usuario);
    }

    private void validarQueQuedeOtroAdministrador() {
        if (usuarioRepository.countByRol(Rol.ADMINISTRADOR) <= 1) {
            throw new ConflictoException("No se puede dejar el sistema sin administrador");
        }
    }
}
