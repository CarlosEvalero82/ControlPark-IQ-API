package co.edu.sena.controlparkiq.service;

import co.edu.sena.controlparkiq.dto.CredencialesDTO;
import co.edu.sena.controlparkiq.dto.RegistroDTO;
import co.edu.sena.controlparkiq.model.Usuario;
import co.edu.sena.controlparkiq.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

/**
 * Lógica de registro e inicio de sesión (métodos reutilizables).
 *
 * <p>Es la capa "chef": recibe los datos ya validados desde el controlador,
 * protege las contraseñas con BCrypt y consulta la base de datos a través
 * del repositorio.</p>
 *
 * @author Carlos Eduardo Valero
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder codificador;

    /**
     * Constructor: Spring inyecta el repositorio y el codificador BCrypt.
     *
     * @param usuarioRepository acceso a la tabla usuarios
     * @param codificador       codificador de contraseñas
     */
    public AuthService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder codificador) {
        this.usuarioRepository = usuarioRepository;
        this.codificador = codificador;
    }

    /**
     * Registra un usuario nuevo.
     *
     * @param datos usuario, contraseña y rol opcional
     * @return true si se registró; false si el nombre de usuario ya existe
     */
    @Transactional
    public boolean registrar(RegistroDTO datos) {
        String nombreUsuario = normalizar(datos.getUsuario());
        if (usuarioRepository.existsByNombreUsuario(nombreUsuario)) {
            return false;
        }
        Usuario nuevo = new Usuario();
        nuevo.setNombreUsuario(nombreUsuario);
        // La contraseña se guarda protegida con BCrypt, nunca en texto plano
        nuevo.setContrasena(codificador.encode(datos.getContrasena()));
        nuevo.setRol(datos.getRol());
        usuarioRepository.save(nuevo);
        return true;
    }

    /**
     * Verifica el usuario y la contraseña.
     *
     * <p>Devuelve false si el usuario no existe, si la contraseña no coincide
     * o si el usuario está inactivo. El controlador responde el mismo mensaje
     * en los tres casos para no dar pistas a un atacante.</p>
     *
     * @param datos usuario y contraseña escritos por el cliente
     * @return true si la autenticación es correcta
     */
    @Transactional
    public boolean autenticar(CredencialesDTO datos) {
        Optional<Usuario> encontrado = usuarioRepository.findByNombreUsuario(normalizar(datos.getUsuario()));
        if (encontrado.isEmpty()) {
            return false; // el usuario no existe
        }
        Usuario usuario = encontrado.get();
        // Compara la contraseña escrita con el hash guardado en la base de datos
        boolean coincide = codificador.matches(datos.getContrasena(), usuario.getContrasena());
        if (!coincide || !usuario.isActivo()) {
            return false;
        }
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);
        return true;
    }

    /**
     * Quita espacios y pasa a minúsculas el nombre de usuario,
     * para que "Admin.Torres" y "admin.torres" sean el mismo usuario.
     *
     * @param nombreUsuario nombre recibido
     * @return nombre normalizado
     */
    private String normalizar(String nombreUsuario) {
        return nombreUsuario.trim().toLowerCase(Locale.ROOT);
    }
}
