package co.edu.sena.controlparkiq.service;

import co.edu.sena.controlparkiq.dto.FacturaDTO;
import co.edu.sena.controlparkiq.dto.PagoDTO;
import co.edu.sena.controlparkiq.exception.ConflictoException;
import co.edu.sena.controlparkiq.exception.RecursoNoEncontradoException;
import co.edu.sena.controlparkiq.exception.ReglaNegocioException;
import co.edu.sena.controlparkiq.model.EstadoSesion;
import co.edu.sena.controlparkiq.model.FacturaVisitante;
import co.edu.sena.controlparkiq.model.SesionParqueo;
import co.edu.sena.controlparkiq.model.Tarifa;
import co.edu.sena.controlparkiq.model.TipoPersona;
import co.edu.sena.controlparkiq.repository.FacturaVisitanteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Lógica del cobro a visitantes (HU-09).
 *
 * <p>Reglas del negocio:</p>
 * <ul>
 *   <li>Solo se factura una sesión CERRADA (primero se registra la salida) (400).</li>
 *   <li>Solo se factura a visitantes: los residentes pagan mensualidad (400).</li>
 *   <li>Una sola factura por sesión (409).</li>
 *   <li>Horas cobradas = horas de permanencia redondeadas hacia arriba (mínimo 1).</li>
 *   <li>Una factura pagada no se vuelve a pagar ni se elimina (409).</li>
 * </ul>
 *
 * @author Carlos Eduardo Valero
 */
@Service
public class FacturaVisitanteService {

    private static final String RECURSO = "la factura";
    private static final long MINUTOS_POR_HORA = 60;

    private final FacturaVisitanteRepository facturaRepository;
    private final SesionParqueoService sesionService;
    private final TarifaService tarifaService;

    public FacturaVisitanteService(FacturaVisitanteRepository facturaRepository, SesionParqueoService sesionService,
                                   TarifaService tarifaService) {
        this.facturaRepository = facturaRepository;
        this.sesionService = sesionService;
        this.tarifaService = tarifaService;
    }

    /**
     * @param pagada filtro opcional: true pagadas, false pendientes
     * @return lista de facturas
     */
    public List<FacturaVisitante> listar(Boolean pagada) {
        return pagada == null ? facturaRepository.findAll() : facturaRepository.findByPagada(pagada);
    }

    public FacturaVisitante buscar(Long id) {
        return facturaRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException(RECURSO, id));
    }

    /**
     * Genera la factura de una sesión de visitante y calcula el valor.
     *
     * @param datos id de la sesión
     * @return la factura generada (pendiente de pago)
     */
    @Transactional
    public FacturaVisitante generar(FacturaDTO datos) {
        SesionParqueo sesion = sesionService.buscar(datos.getSesionId());
        if (sesion.getVehiculo().getPropietario().getTipo() != TipoPersona.VISITANTE) {
            throw new ReglaNegocioException("Solo se factura a visitantes: los residentes pagan mensualidad");
        }
        if (sesion.getEstado() != EstadoSesion.CERRADA) {
            throw new ReglaNegocioException("Primero registre la salida del vehículo (la sesión sigue abierta)");
        }
        if (facturaRepository.existsBySesionId(sesion.getId())) {
            throw new ConflictoException("La sesión " + sesion.getId() + " ya tiene factura");
        }
        Tarifa tarifa = tarifaService.buscarPorTipo(sesion.getVehiculo().getTipo());
        int horas = calcularHoras(sesion.getHoraEntrada(), sesion.getHoraSalida());

        FacturaVisitante factura = new FacturaVisitante();
        factura.setSesion(sesion);
        factura.setHorasCobradas(horas);
        factura.setValorHora(tarifa.getValorHora());
        factura.setValorTotal(tarifa.getValorHora().multiply(BigDecimal.valueOf(horas)));
        factura.setFechaEmision(LocalDateTime.now());
        factura.setPagada(false);
        return facturaRepository.save(factura);
    }

    /**
     * Registra el pago de una factura.
     *
     * @param id    id de la factura
     * @param datos método de pago
     * @return la factura pagada
     */
    @Transactional
    public FacturaVisitante pagar(Long id, PagoDTO datos) {
        FacturaVisitante factura = buscar(id);
        if (factura.isPagada()) {
            throw new ConflictoException("La factura " + id + " ya fue pagada");
        }
        factura.setPagada(true);
        factura.setMetodoPago(datos.getMetodoPago());
        factura.setFechaPago(LocalDateTime.now());
        return facturaRepository.save(factura);
    }

    /**
     * Anula (elimina) una factura pendiente. Las pagadas no se pueden eliminar (409).
     *
     * @param id id de la factura
     */
    @Transactional
    public void eliminar(Long id) {
        FacturaVisitante factura = buscar(id);
        if (factura.isPagada()) {
            throw new ConflictoException("No se puede eliminar la factura " + id + ": ya fue pagada");
        }
        facturaRepository.delete(factura);
    }

    /**
     * Toda fracción de hora se cobra como hora completa; mínimo una hora.
     * Ejemplo: 2 h 30 min → 3 horas.
     */
    private int calcularHoras(LocalDateTime entrada, LocalDateTime salida) {
        long minutos = Duration.between(entrada, salida).toMinutes();
        long horas = (minutos + MINUTOS_POR_HORA - 1) / MINUTOS_POR_HORA;
        return (int) Math.max(1, horas);
    }
}
