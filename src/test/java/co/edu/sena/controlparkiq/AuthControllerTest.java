package co.edu.sena.controlparkiq;

import co.edu.sena.controlparkiq.model.Usuario;
import co.edu.sena.controlparkiq.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas automáticas de los servicios de registro e inicio de sesión.
 *
 * <p>Se ejecutan con {@code mvn test} sobre una base H2 en memoria.
 * Cubren los 6 casos de prueba de la orientación de la instructora
 * y verifican que la contraseña se guarde protegida.</p>
 *
 * @author Carlos Eduardo Valero
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    private static final String URL_REGISTRO = "/api/auth/registro";
    private static final String URL_LOGIN = "/api/auth/login";
    private static final String JSON_VALIDO = "{\"usuario\":\"porteria.torre1\",\"contrasena\":\"Parque2026\"}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /** Deja la tabla vacía antes de cada prueba. */
    @BeforeEach
    void limpiarTabla() {
        usuarioRepository.deleteAll();
    }

    /** Envía una petición POST con cuerpo JSON. */
    private ResultActions enviar(String url, String json) throws Exception {
        return mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json));
    }

    @Test
    @DisplayName("1 · Registro correcto → 201")
    void registroCorrecto() throws Exception {
        enviar(URL_REGISTRO, JSON_VALIDO)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Usuario registrado correctamente"));
    }

    @Test
    @DisplayName("2 · Registro repetido → 409")
    void registroRepetido() throws Exception {
        enviar(URL_REGISTRO, JSON_VALIDO);
        enviar(URL_REGISTRO, JSON_VALIDO)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("El usuario ya existe"));
    }

    @Test
    @DisplayName("3 · Registro con usuario vacío → 400")
    void registroConDatosVacios() throws Exception {
        enviar(URL_REGISTRO, "{\"usuario\":\"\",\"contrasena\":\"Parque2026\"}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Datos inválidos"));
    }

    @Test
    @DisplayName("4 · Login correcto → 200 Autenticación satisfactoria")
    void loginCorrecto() throws Exception {
        enviar(URL_REGISTRO, JSON_VALIDO);
        enviar(URL_LOGIN, JSON_VALIDO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Autenticación satisfactoria"));
    }

    @Test
    @DisplayName("5 · Login con contraseña incorrecta → 401 Error en la autenticación")
    void loginContrasenaIncorrecta() throws Exception {
        enviar(URL_REGISTRO, JSON_VALIDO);
        enviar(URL_LOGIN, "{\"usuario\":\"porteria.torre1\",\"contrasena\":\"OtraClave99\"}")
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Error en la autenticación"));
    }

    @Test
    @DisplayName("6 · Login de usuario inexistente → 401 Error en la autenticación")
    void loginUsuarioInexistente() throws Exception {
        enviar(URL_LOGIN, "{\"usuario\":\"no.existe\",\"contrasena\":\"Parque2026\"}")
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Error en la autenticación"));
    }

    @Test
    @DisplayName("Extra · La contraseña se guarda protegida con BCrypt")
    void contrasenaProtegida() throws Exception {
        enviar(URL_REGISTRO, JSON_VALIDO);
        Usuario guardado = usuarioRepository.findByNombreUsuario("porteria.torre1").orElseThrow();
        assertNotEquals("Parque2026", guardado.getContrasena());
        assertTrue(guardado.getContrasena().startsWith("$2a$10$"));
    }
}
