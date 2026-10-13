package co.edu.sena.controlparkiq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del servicio web de autenticación de ControlPark-IQ.
 *
 * <p>Evidencia GA7-220501096-AA5-EV01 · Diseño y desarrollo de servicios web - caso.
 * Al ejecutarla, Spring Boot levanta el servidor en http://localhost:8080
 * y deja disponibles los servicios de registro e inicio de sesión.</p>
 *
 * @author Carlos Eduardo Valero
 */
@SpringBootApplication
public class ControlParkAuthApplication {

    /**
     * Punto de entrada de la aplicación.
     *
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(ControlParkAuthApplication.class, args);
    }
}
