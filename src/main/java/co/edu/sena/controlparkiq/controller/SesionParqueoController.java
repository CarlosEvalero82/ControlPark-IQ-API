package co.edu.sena.controlparkiq.controller;

import co.edu.sena.controlparkiq.dto.EntradaDTO;
import co.edu.sena.controlparkiq.dto.SalidaDTO;
import co.edu.sena.controlparkiq.model.EstadoSesion;
import co.edu.sena.controlparkiq.model.SesionParqueo;
import co.edu.sena.controlparkiq.service.SesionParqueoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDate;
import java.util.List;

/**
 * Servicios web de las sesiones de parqueo: entrada y salida de vehículos (HU-08, HU-10).
 *
 * <pre>
 * GET    /api/sesiones[?estado=ABIERTA&amp;fecha=2026-10-20]  → 200 · 400
 * GET    /api/sesiones/{id}                               → 200 · 404
 * POST   /api/sesiones        (entrada)                   → 201 · 400 · 404 · 409
 * PUT    /api/sesiones/{id}   (salida)                    → 200 · 400 · 404 · 409
 * DELETE /api/sesiones/{id}                               → 204 · 404 · 409
 * </pre>
 *
 * @author Carlos Eduardo Valero
 */
@RestController
@RequestMapping("/api/sesiones")
@Tag(name = "Sesiones de parqueo", description = "Entrada y salida de vehículos (HU-08, HU-10)")
public class SesionParqueoController {

    private final SesionParqueoService sesionService;

    public SesionParqueoController(SesionParqueoService sesionService) {
        this.sesionService = sesionService;
    }

    @GetMapping
    @Operation(summary = "Listar sesiones (filtros opcionales ?estado= y ?fecha=AAAA-MM-DD)")
    public List<SesionParqueo> listar(@RequestParam(required = false) EstadoSesion estado,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return sesionService.listar(estado, fecha);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar una sesión por id")
    public SesionParqueo buscar(@PathVariable Long id) {
        return sesionService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Registrar la entrada de un vehículo (ocupa el espacio)")
    public ResponseEntity<SesionParqueo> registrarEntrada(@Valid @RequestBody EntradaDTO datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sesionService.registrarEntrada(datos));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Registrar la salida del vehículo (cierra la sesión y libera el espacio)")
    public SesionParqueo registrarSalida(@PathVariable Long id, @Valid @RequestBody(required = false) SalidaDTO datos) {
        return sesionService.registrarSalida(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una sesión registrada por error (si no tiene factura)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sesionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
