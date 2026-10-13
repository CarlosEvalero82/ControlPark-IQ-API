package co.edu.sena.controlparkiq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuración de seguridad del servicio.
 *
 * <p>Registra el codificador BCrypt como un componente (bean) de Spring para
 * que el servicio lo reciba por inyección de dependencias. Solo se usa la
 * librería spring-security-crypto; no se activa Spring Security completo.</p>
 *
 * @author Carlos Eduardo Valero
 */
@Configuration
public class SeguridadConfig {

    /** Costo del cifrado: 2^10 iteraciones (valor recomendado por defecto). */
    private static final int FUERZA_BCRYPT = 10;

    /**
     * Crea el codificador de contraseñas BCrypt.
     *
     * @return codificador que genera hashes con salt aleatorio
     */
    @Bean
    public BCryptPasswordEncoder codificadorContrasenas() {
        return new BCryptPasswordEncoder(FUERZA_BCRYPT);
    }
}
