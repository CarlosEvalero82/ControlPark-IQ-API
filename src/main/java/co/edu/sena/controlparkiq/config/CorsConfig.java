package co.edu.sena.controlparkiq.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Permite que el front-end React de ControlPark-IQ (http://localhost:5173)
 * consuma todos los servicios de la API.
 *
 * <p>Antes estaba en el controlador de autenticación; ahora se configura una
 * sola vez para todas las rutas /api/**.</p>
 *
 * @author Carlos Eduardo Valero
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
