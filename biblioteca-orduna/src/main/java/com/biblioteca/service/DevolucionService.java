package com.biblioteca.service;

import com.biblioteca.dto.*;
import com.biblioteca.entity.*;
import com.biblioteca.exception.OperacionInvalidaException;
import com.biblioteca.exception.RecursoNoEncontradoException;
import com.biblioteca.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;

/** RF07 / RF08 / RF15 - Devoluciones, cálculo de multa y pagos de multa. */
@Service
public class DevolucionService {

    private static final EnumSet<EstadoEjemplar> ESTADOS_AL_DEVOLVER =
            EnumSet.of(EstadoEjemplar.DISPONIBLE, EstadoEjemplar.DANADO, EstadoEjemplar.PERDIDO, EstadoEjemplar.BAJA);

    private final PrestamoRepository prestamoRepository;
    private final DevolucionRepository devolucionRepository;
    private final MultaRepository multaRepository;
    private final PagoMultaRepository pagoMultaRepository;
    private final UsuarioService usuarioService;
    private final ConfiguracionService configuracionService;
    private final NotificacionService notificacionService;
    private final ReservaService reservaService;

    public DevolucionService(PrestamoRepository prestamoRepository, DevolucionRepository devolucionRepository,
                             MultaRepository multaRepository, PagoMultaRepository pagoMultaRepository,
                             UsuarioService usuarioService, ConfiguracionService configuracionService,
                             NotificacionService notificacionService, ReservaService reservaService) {
        this.prestamoRepository = prestamoRepository;
        this.devolucionRepository = devolucionRepository;
        this.multaRepository = multaRepository;
        this.pagoMultaRepository = pagoMultaRepository;
        this.usuarioService = usuarioService;
        this.configuracionService = configuracionService;
        this.notificacionService = notificacionService;
        this.reservaService = reservaService;
    }

    /**
     * RF07 / RF08 - Registrar devolución y calcular multa.
     * - FECHA_FIJA con retraso: multa = días de retraso × tarifa guardada en el préstamo.
     * - ABIERTO: nunca genera multa.
     * - Si el libro vuelve bien, el ejemplar pasa al primero de la cola de reservas (FIFO) o queda DISPONIBLE.
     *   Si vuelve DANADO/PERDIDO, se marca así y no se asigna a nadie.
     */
    @Transactional
    public DevolucionResponseDTO registrarDevolucion(NuevaDevolucionDTO datos) {
        Prestamo prestamo = prestamoRepository.findById(datos.idPrestamo())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el préstamo con id " + datos.idPrestamo()));
        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO || devolucionRepository.existsByPrestamoId(prestamo.getId())) {
            throw new OperacionInvalidaException("El préstamo " + prestamo.getId() + " ya fue devuelto");
        }
        EstadoEjemplar estadoFinal = datos.estadoEjemplar() != null ? datos.estadoEjemplar() : EstadoEjemplar.DISPONIBLE;
        if (!ESTADOS_AL_DEVOLVER.contains(estadoFinal)) {
            throw new OperacionInvalidaException("estado_ejemplar debe ser DISPONIBLE, DANADO, PERDIDO o BAJA");
        }

        LocalDateTime ahora = LocalDateTime.now();
        int diasRetraso = prestamo.getTipo() == TipoPrestamo.ABIERTO ? 0
                : calcularDiasRetraso(prestamo.getFechaVencimiento(), ahora.toLocalDate());

        Devolucion d = new Devolucion();
        d.setPrestamo(prestamo);
        d.setFechaDevolucion(ahora);
        d.setDiasRetraso(diasRetraso);
        d.setObservaciones(datos.observaciones());
        d = devolucionRepository.save(d);
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);

        Multa multa = null;
        if (diasRetraso > 0) {
            BigDecimal tarifa = prestamo.getTarifaMultaDiaria() != null ? prestamo.getTarifaMultaDiaria()
                    : configuracionService.obtenerParametros().getTarifaMultaDiaria();
            multa = new Multa();
            multa.setDevolucion(d);
            multa.setUsuario(prestamo.getLector());
            multa.setMonto(tarifa.multiply(BigDecimal.valueOf(diasRetraso)));
            multa.setMontoPagado(BigDecimal.ZERO);
            multa.setEstado(EstadoMulta.PENDIENTE);
            multa.setFechaGeneracion(ahora);
            multa = multaRepository.save(multa);
            notificacionService.crear(prestamo.getLector(), TipoNotificacion.MULTA_PENDIENTE,
                    NotificacionService.mensajeMulta(multa) + " (" + diasRetraso + " día(s) de retraso)", multa.getId());
        }

        Ejemplar ejemplar = prestamo.getEjemplar();
        Long idReservaNotificada = null;
        if (estadoFinal == EstadoEjemplar.DISPONIBLE) {
            idReservaNotificada = reservaService.asignarEjemplarLiberado(ejemplar).map(Reserva::getId).orElse(null);
        } else {
            ejemplar.setEstado(estadoFinal);
        }

        return new DevolucionResponseDTO(d.getId(), prestamo.getId(), d.getFechaDevolucion(), diasRetraso,
                d.getObservaciones(), multa == null ? null : multa.getId(),
                multa == null ? null : multa.getMonto(), idReservaNotificada);
    }

    /**
     * RF15 - Registrar pago de multa. Acepta pagos parciales:
     * el monto debe ser mayor que 0 y no mayor que el saldo. La multa queda PARCIAL o PAGADA.
     */
    @Transactional
    public PagoMultaResponseDTO registrarPagoMulta(Long idMulta, PagoMultaDTO datos, Long idBibliotecario) {
        Usuario bibliotecario = usuarioService.buscarPersonal(idBibliotecario);
        Multa multa = multaRepository.findById(idMulta)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la multa con id " + idMulta));
        if (multa.getEstado() == EstadoMulta.PAGADA) {
            throw new OperacionInvalidaException("La multa " + idMulta + " ya fue pagada");
        }
        BigDecimal saldo = multa.getSaldo();
        if (datos.montoPagado().signum() <= 0) {
            throw new OperacionInvalidaException("El monto pagado debe ser mayor que 0");
        }
        if (datos.montoPagado().compareTo(saldo) > 0) {
            throw new OperacionInvalidaException("El monto pagado (S/ " + datos.montoPagado()
                    + ") supera el saldo de la multa (S/ " + saldo + ")");
        }
        PagoMulta pago = new PagoMulta();
        pago.setMulta(multa);
        pago.setMontoPagado(datos.montoPagado());
        pago.setMetodoPago(datos.metodoPago());
        pago.setFechaPago(LocalDateTime.now());
        pago.setBibliotecario(bibliotecario);
        pago = pagoMultaRepository.save(pago);

        multa.setMontoPagado(multa.getMontoPagado().add(datos.montoPagado()));
        multa.setEstado(multa.getSaldo().signum() == 0 ? EstadoMulta.PAGADA : EstadoMulta.PARCIAL);
        return pagoADTO(pago);
    }

    /** Pantalla "Pagos de multa". filtro: PENDIENTES (por defecto: pendientes y parciales), PAGADAS o TODAS. */
    @Transactional(readOnly = true)
    public List<MultaResponseDTO> listarMultas(String filtro) {
        String f = filtro == null || filtro.isBlank() ? "PENDIENTES" : filtro.trim().toUpperCase();
        List<Multa> multas = switch (f) {
            case "PENDIENTES" -> multaRepository.findByEstadoInOrderByFechaGeneracionDesc(
                    EnumSet.of(EstadoMulta.PENDIENTE, EstadoMulta.PARCIAL));
            case "PAGADAS" -> multaRepository.findByEstadoInOrderByFechaGeneracionDesc(EnumSet.of(EstadoMulta.PAGADA));
            case "TODAS" -> multaRepository.findAllByOrderByFechaGeneracionDesc();
            default -> throw new OperacionInvalidaException("Filtro inválido: use PENDIENTES, PAGADAS o TODAS");
        };
        return multas.stream().map(this::multaADTO).toList();
    }

    /** "Mis multas" del lector logueado. */
    @Transactional(readOnly = true)
    public List<MultaResponseDTO> misMultas(Long idLector) {
        return multaRepository.findByUsuarioIdOrderByFechaGeneracionDesc(idLector).stream().map(this::multaADTO).toList();
    }

    /** Historial de pagos (auditoría del gerente). Fechas null = mes actual. */
    @Transactional(readOnly = true)
    public List<PagoMultaResponseDTO> listarPagos(LocalDate desde, LocalDate hasta) {
        LocalDate[] r = ReporteService.rango(desde, hasta);
        return pagoMultaRepository.findByFechaPagoBetweenOrderByFechaPagoDesc(r[0].atStartOfDay(), r[1].atTime(LocalTime.MAX))
                .stream().map(DevolucionService::pagoADTO).toList();
    }

    /** Días completos después de la fecha de vencimiento (0 si a tiempo o si no tiene fecha). */
    static int calcularDiasRetraso(LocalDate vencimiento, LocalDate devolucion) {
        if (vencimiento == null) return 0;
        long dias = ChronoUnit.DAYS.between(vencimiento, devolucion);
        return (int) Math.max(0, dias);
    }

    MultaResponseDTO multaADTO(Multa m) {
        List<PagoMulta> pagos = pagoMultaRepository.findByMultaIdOrderByFechaPagoAsc(m.getId());
        PagoMulta ultimo = pagos.isEmpty() ? null : pagos.get(pagos.size() - 1);
        Prestamo p = m.getDevolucion().getPrestamo();
        return new MultaResponseDTO(m.getId(), m.getUsuario().getId(), m.getUsuario().getNombreCompleto(),
                m.getUsuario().getDni(), p.getEjemplar().getLibro().getTitulo(), m.getDevolucion().getDiasRetraso(),
                m.getMonto(), m.getMontoPagado(), m.getSaldo(), m.getEstado().name(), m.getFechaGeneracion(),
                ultimo == null ? null : ultimo.getMetodoPago().name(), ultimo == null ? null : ultimo.getFechaPago());
    }

    static PagoMultaResponseDTO pagoADTO(PagoMulta p) {
        Multa m = p.getMulta();
        return new PagoMultaResponseDTO(p.getId(), m.getId(), m.getUsuario().getNombreCompleto(), p.getMontoPagado(),
                p.getMetodoPago().name(), p.getFechaPago(), p.getBibliotecario().getId(), m.getSaldo(),
                m.getEstado().name());
    }
}
