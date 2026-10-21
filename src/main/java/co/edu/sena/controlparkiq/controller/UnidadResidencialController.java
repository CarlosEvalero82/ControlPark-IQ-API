package co.edu.sena.controlparkiq.controller;

import co.edu.sena.controlparkiq.dto.UnidadResidencialDTO;
import co.edu.sena.controlparkiq.model.UnidadResidencial;
import co.edu.sena.controlparkiq.service.UnidadResidencialService;
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
 * Servicios web de las unidades residenciales (HU-03).
 *
 * <pre>
 * GET    /api/unidades       → 200
 * GET    /api/unidades/{id}  → 200 · 404
 * POST   /api/unidades       → 201 · 400 · 409
 * PUT    /api/unidades/{id}  → 200 · 400 · 404 · 409
 * DELETE /api/unidades/{id}  → 204 · 404 · 409
 * </pre>
 *
 * @author Carlos Eduardo Valero
 */
@RestController
@RequestMapping("/api/unidades")
@Tag(name = "Unidades residenciales", description = "Apartamentos o casas del conjunto (HU-03)")
public class UnidadResidencialController {

    private final UnidadResidencialService unidadService;

    public UnidadResidencialController(UnidadResidencialService unidadService) {
        this.unidadService = unidadService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las unidades")
    public List<UnidadResidencial> listar() {
        return unidadService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar una unidad por id")
    public UnidadResidencial buscar(@PathVariable Long id) {
        return unidadService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Crear una unidad")
    public ResponseEntity<UnidadResidencial> crear(@Valid @RequestBody UnidadResidencialDTO datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadService.crear(datos));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una unidad")
    public UnidadResidencial actualizar(@PathVariable Long id, @Valid @RequestBody UnidadResidencialDTO datos) {
        return unidadService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una unidad (si no tiene residentes)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        unidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
