package co.edu.sena.controlparkiq.controller;

import co.edu.sena.controlparkiq.dto.VehiculoDTO;
import co.edu.sena.controlparkiq.model.Vehiculo;
import co.edu.sena.controlparkiq.service.VehiculoService;
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
 * Servicios web de los vehículos (HU-04).
 *
 * <pre>
 * GET    /api/vehiculos[?propietarioId=1]  → 200
 * GET    /api/vehiculos/{id}               → 200 · 404
 * POST   /api/vehiculos                    → 201 · 400 · 404 · 409
 * PUT    /api/vehiculos/{id}               → 200 · 400 · 404 · 409
 * DELETE /api/vehiculos/{id}               → 204 · 404 · 409
 * </pre>
 *
 * @author Carlos Eduardo Valero
 */
@RestController
@RequestMapping("/api/vehiculos")
@Tag(name = "Vehículos", description = "Carros y motos de residentes y visitantes (HU-04)")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    @Operation(summary = "Listar vehículos (filtro opcional ?propietarioId=)")
    public List<Vehiculo> listar(@RequestParam(required = false) Long propietarioId) {
        return vehiculoService.listar(propietarioId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un vehículo por id")
    public Vehiculo buscar(@PathVariable Long id) {
        return vehiculoService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Registrar un vehículo")
    public ResponseEntity<Vehiculo> crear(@Valid @RequestBody VehiculoDTO datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculoService.crear(datos));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un vehículo")
    public Vehiculo actualizar(@PathVariable Long id, @Valid @RequestBody VehiculoDTO datos) {
        return vehiculoService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un vehículo (si no tiene historial de parqueo)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
