package co.edu.sena.controlparkiq.controller;

import co.edu.sena.controlparkiq.dto.PersonaDTO;
import co.edu.sena.controlparkiq.model.Persona;
import co.edu.sena.controlparkiq.model.TipoPersona;
import co.edu.sena.controlparkiq.service.PersonaService;
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
 * Servicios web de las personas: residentes y visitantes (HU-02).
 *
 * <pre>
 * GET    /api/personas[?tipo=RESIDENTE]  → 200
 * GET    /api/personas/{id}              → 200 · 404
 * POST   /api/personas                   → 201 · 400 · 404 · 409
 * PUT    /api/personas/{id}              → 200 · 400 · 404 · 409
 * DELETE /api/personas/{id}              → 204 · 404 · 409
 * </pre>
 *
 * @author Carlos Eduardo Valero
 */
@RestController
@RequestMapping("/api/personas")
@Tag(name = "Personas", description = "Residentes y visitantes del conjunto (HU-02)")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    @Operation(summary = "Listar personas (filtro opcional ?tipo=RESIDENTE o VISITANTE)")
    public List<Persona> listar(@RequestParam(required = false) TipoPersona tipo) {
        return personaService.listar(tipo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar una persona por id")
    public Persona buscar(@PathVariable Long id) {
        return personaService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Registrar una persona")
    public ResponseEntity<Persona> crear(@Valid @RequestBody PersonaDTO datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personaService.crear(datos));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una persona")
    public Persona actualizar(@PathVariable Long id, @Valid @RequestBody PersonaDTO datos) {
        return personaService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una persona (si no tiene vehículos)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        personaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
