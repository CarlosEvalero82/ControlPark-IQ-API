package co.edu.sena.controlparkiq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la API REST de ControlPark-IQ.
 *
 * <p>Evidencia GA7-220501096-AA5-EV03 · Diseño y desarrollo de servicios web - proyecto.
 * Al ejecutarla, Spring Boot levanta el servidor en http://localhost:8080 y deja
 * disponibles los servicios de autenticación, usuarios, personas, unidades
 * residenciales, vehículos, zonas, espacios, tarifas, sesiones de parqueo y
 * facturas de visitantes.</p>
 *
 * @author Carlos Eduardo Valero
 */
@SpringBootApplication
public class ControlParkApplication {

    /**
     * Punto de entrada de la aplicación.
     *
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(ControlParkApplication.class, args);
    }
}
