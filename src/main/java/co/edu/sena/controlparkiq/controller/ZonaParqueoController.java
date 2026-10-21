package co.edu.sena.controlparkiq.controller;

import co.edu.sena.controlparkiq.dto.ZonaParqueoDTO;
import co.edu.sena.controlparkiq.model.ZonaParqueo;
import co.edu.sena.controlparkiq.service.ZonaParqueoService;
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
 * Servicios web de las zonas de parqueo (HU-05).
 *
 * <pre>
 * GET    /api/zonas       → 200
 * GET    /api/zonas/{id}  → 200 · 404
 * POST   /api/zonas       → 201 · 400 · 409
 * PUT    /api/zonas/{id}  → 200 · 400 · 404 · 409
 * DELETE /api/zonas/{id}  → 204 · 404 · 409
 * </pre>
 *
 * @author Carlos Eduardo Valero
 */
@RestController
@RequestMapping("/api/zonas")
@Tag(name = "Zonas de parqueo", description = "Sectores del parqueadero (HU-05)")
public class ZonaParqueoController {

    private final ZonaParqueoService zonaService;

    public ZonaParqueoController(ZonaParqueoService zonaService) {
        this.zonaService = zonaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las zonas")
    public List<ZonaParqueo> listar() {
        return zonaService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar una zona por id")
    public ZonaParqueo buscar(@PathVariable Long id) {
        return zonaService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Crear una zona")
    public ResponseEntity<ZonaParqueo> crear(@Valid @RequestBody ZonaParqueoDTO datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(zonaService.crear(datos));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una zona")
    public ZonaParqueo actualizar(@PathVariable Long id, @Valid @RequestBody ZonaParqueoDTO datos) {
        return zonaService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una zona (si no tiene espacios)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        zonaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
