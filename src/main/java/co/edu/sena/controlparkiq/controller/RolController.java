package co.edu.sena.controlparkiq.controller;

import co.edu.sena.controlparkiq.model.Rol;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Servicio web de consulta de los roles del sistema.
 *
 * <p>Los roles son fijos (enum {@link Rol}), por eso solo se pueden consultar.</p>
 *
 * <pre>
 * GET /api/roles → 200
 * </pre>
 *
 * @author Carlos Eduardo Valero
 */
@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "Roles fijos del sistema")
public class RolController {

    @GetMapping
    @Operation(summary = "Listar los roles del sistema")
    public List<Rol> listar() {
        return Arrays.asList(Rol.values());
    }
}
