package co.edu.sena.controlparkiq.controller;

import co.edu.sena.controlparkiq.dto.RegistroDTO;
import co.edu.sena.controlparkiq.dto.UsuarioActualizarDTO;
import co.edu.sena.controlparkiq.model.Usuario;
import co.edu.sena.controlparkiq.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Servicios web de gestión de usuarios del sistema (HU-02).
 *
 * <pre>
 * GET    /api/usuarios       → 200
 * GET    /api/usuarios/{id}  → 200 · 404
 * POST   /api/usuarios       → 201 · 400 · 409
 * PUT    /api/usuarios/{id}  → 200 · 400 · 404 · 409
 * DELETE /api/usuarios/{id}  → 204 · 404 · 409
 * </pre>
 *
 * @author Carlos Eduardo Valero
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Cuentas de administración, portería y residentes (HU-02)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios (sin contraseñas)")
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un usuario por id")
    public Usuario buscar(@PathVariable Long id) {
        return usuarioService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Crear un usuario (mismas reglas del registro)")
    public ResponseEntity<Usuario> crear(@Valid @RequestBody RegistroDTO datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(datos));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cambiar el rol o activar/inactivar un usuario")
    public Usuario actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioActualizarDTO datos) {
        return usuarioService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario (no el último administrador)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
