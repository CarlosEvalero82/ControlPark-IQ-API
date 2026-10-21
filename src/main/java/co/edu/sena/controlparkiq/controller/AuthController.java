package co.edu.sena.controlparkiq.controller;

import co.edu.sena.controlparkiq.dto.CredencialesDTO;
import co.edu.sena.controlparkiq.dto.RegistroDTO;
import co.edu.sena.controlparkiq.dto.RespuestaDTO;
import co.edu.sena.controlparkiq.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone los servicios web de registro e inicio de sesión.
 *
 * <p>Es la capa "mesero": recibe la petición HTTP, la entrega al servicio
 * y responde con el código de estado y el mensaje correspondientes.</p>
 *
 * <pre>
 * POST /api/auth/registro → 201 registrado · 400 datos inválidos · 409 ya existe
 * POST /api/auth/login    → 200 satisfactoria · 400 datos vacíos · 401 error
 * </pre>
 *
 * @author Carlos Eduardo Valero
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /** Mensaje único de fallo: no revela si falló el usuario o la contraseña. */
    public static final String MENSAJE_ERROR_AUTENTICACION = "Error en la autenticación";
    public static final String MENSAJE_AUTENTICACION_OK = "Autenticación satisfactoria";
    public static final String MENSAJE_REGISTRO_OK = "Usuario registrado correctamente";
    public static final String MENSAJE_USUARIO_EXISTE = "El usuario ya existe";

    private final AuthService authService;

    /**
     * Constructor: Spring inyecta el servicio de autenticación.
     *
     * @param authService lógica de registro e inicio de sesión
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * SERVICIO 1 · Registro de usuario.
     *
     * @param datos usuario y contraseña (validados con @Valid)
     * @return 201 si se registró · 409 si el usuario ya existe
     */
    @PostMapping("/registro")
    public ResponseEntity<RespuestaDTO> registrar(@Valid @RequestBody RegistroDTO datos) {
        if (!authService.registrar(datos)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RespuestaDTO(false, MENSAJE_USUARIO_EXISTE));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RespuestaDTO(true, MENSAJE_REGISTRO_OK));
    }

    /**
     * SERVICIO 2 · Inicio de sesión.
     *
     * @param datos usuario y contraseña (validados con @Valid)
     * @return 200 "Autenticación satisfactoria" · 401 "Error en la autenticación"
     */
    @PostMapping("/login")
    public ResponseEntity<RespuestaDTO> iniciarSesion(@Valid @RequestBody CredencialesDTO datos) {
        if (authService.autenticar(datos)) {
            return ResponseEntity.ok(new RespuestaDTO(true, MENSAJE_AUTENTICACION_OK));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new RespuestaDTO(false, MENSAJE_ERROR_AUTENTICACION));
    }
}
