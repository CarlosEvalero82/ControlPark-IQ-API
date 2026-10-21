package co.edu.sena.controlparkiq.controller;

import co.edu.sena.controlparkiq.dto.EspacioParqueoDTO;
import co.edu.sena.controlparkiq.model.EspacioParqueo;
import co.edu.sena.controlparkiq.model.EstadoEspacio;
import co.edu.sena.controlparkiq.model.TipoVehiculo;
import co.edu.sena.controlparkiq.service.EspacioParqueoService;
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
 * Servicios web de los espacios de parqueo (HU-05 y HU-06).
 *
 * <pre>
 * GET    /api/espacios[?estado=DISPONIBLE&amp;tipoVehiculo=CARRO]  → 200 · 400
 * GET    /api/espacios/{id}                                     → 200 · 404
 * POST   /api/espacios                                          → 201 · 400 · 404 · 409
 * PUT    /api/espacios/{id}                                     → 200 · 400 · 404 · 409
 * DELETE /api/espacios/{id}                                     → 204 · 404 · 409
 * </pre>
 *
 * @author Carlos Eduardo Valero
 */
@RestController
@RequestMapping("/api/espacios")
@Tag(name = "Espacios de parqueo", description = "Cupos del parqueadero y su disponibilidad (HU-05, HU-06)")
public class EspacioParqueoController {

    private final EspacioParqueoService espacioService;

    public EspacioParqueoController(EspacioParqueoService espacioService) {
        this.espacioService = espacioService;
    }

    @GetMapping
    @Operation(summary = "Listar espacios (filtros opcionales ?estado= y ?tipoVehiculo=)")
    public List<EspacioParqueo> listar(@RequestParam(required = false) EstadoEspacio estado,
                                       @RequestParam(required = false) TipoVehiculo tipoVehiculo) {
        return espacioService.listar(estado, tipoVehiculo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un espacio por id")
    public EspacioParqueo buscar(@PathVariable Long id) {
        return espacioService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Crear un espacio")
    public ResponseEntity<EspacioParqueo> crear(@Valid @RequestBody EspacioParqueoDTO datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(espacioService.crear(datos));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un espacio (no si está ocupado)")
    public EspacioParqueo actualizar(@PathVariable Long id, @Valid @RequestBody EspacioParqueoDTO datos) {
        return espacioService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un espacio (si no está ocupado ni tiene historial)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        espacioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
