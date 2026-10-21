package co.edu.sena.controlparkiq.controller;

import co.edu.sena.controlparkiq.dto.FacturaDTO;
import co.edu.sena.controlparkiq.dto.PagoDTO;
import co.edu.sena.controlparkiq.model.FacturaVisitante;
import co.edu.sena.controlparkiq.service.FacturaVisitanteService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Servicios web de las facturas de visitantes (HU-09).
 *
 * <pre>
 * GET    /api/facturas[?pagada=false]  → 200
 * GET    /api/facturas/{id}            → 200 · 404
 * POST   /api/facturas   (generar)     → 201 · 400 · 404 · 409
 * PUT    /api/facturas/{id} (pagar)    → 200 · 400 · 404 · 409
 * DELETE /api/facturas/{id} (anular)   → 204 · 404 · 409
 * </pre>
 *
 * @author Carlos Eduardo Valero
 */
@RestController
@RequestMapping("/api/facturas")
@Tag(name = "Facturas de visitantes", description = "Cobro a visitantes al salir (HU-09)")
public class FacturaVisitanteController {

    private final FacturaVisitanteService facturaService;

    public FacturaVisitanteController(FacturaVisitanteService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    @Operation(summary = "Listar facturas (filtro opcional ?pagada=true o false)")
    public List<FacturaVisitante> listar(@RequestParam(required = false) Boolean pagada) {
        return facturaService.listar(pagada);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar una factura por id")
    public FacturaVisitante buscar(@PathVariable Long id) {
        return facturaService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Generar la factura de una sesión de visitante (el valor lo calcula el sistema)")
    public ResponseEntity<FacturaVisitante> generar(@Valid @RequestBody FacturaDTO datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaService.generar(datos));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Registrar el pago de una factura")
    public FacturaVisitante pagar(@PathVariable Long id, @Valid @RequestBody PagoDTO datos) {
        return facturaService.pagar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Anular una factura pendiente (las pagadas no se eliminan)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        facturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
