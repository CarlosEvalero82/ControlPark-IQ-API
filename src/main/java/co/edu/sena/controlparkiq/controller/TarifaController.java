package co.edu.sena.controlparkiq.controller;

import co.edu.sena.controlparkiq.dto.TarifaDTO;
import co.edu.sena.controlparkiq.model.Tarifa;
import co.edu.sena.controlparkiq.service.TarifaService;
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
 * Servicios web de las tarifas (HU-07).
 *
 * <pre>
 * GET    /api/tarifas       → 200
 * GET    /api/tarifas/{id}  → 200 · 404
 * POST   /api/tarifas       → 201 · 400 · 409
 * PUT    /api/tarifas/{id}  → 200 · 400 · 404 · 409
 * DELETE /api/tarifas/{id}  → 204 · 404
 * </pre>
 *
 * @author Carlos Eduardo Valero
 */
@RestController
@RequestMapping("/api/tarifas")
@Tag(name = "Tarifas", description = "Valor por hora (visitantes) y mensualidad (residentes) (HU-07)")
public class TarifaController {

    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las tarifas")
    public List<Tarifa> listar() {
        return tarifaService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar una tarifa por id")
    public Tarifa buscar(@PathVariable Long id) {
        return tarifaService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Crear la tarifa de un tipo de vehículo")
    public ResponseEntity<Tarifa> crear(@Valid @RequestBody TarifaDTO datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifaService.crear(datos));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una tarifa")
    public Tarifa actualizar(@PathVariable Long id, @Valid @RequestBody TarifaDTO datos) {
        return tarifaService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una tarifa")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tarifaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
