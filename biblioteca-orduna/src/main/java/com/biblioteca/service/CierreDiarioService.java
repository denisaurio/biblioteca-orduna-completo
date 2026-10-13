package com.biblioteca.service;

import com.biblioteca.dto.CierreDiarioDTO;
import com.biblioteca.dto.CierreDiarioResponseDTO;
import com.biblioteca.entity.*;
import com.biblioteca.exception.RecursoDuplicadoException;
import com.biblioteca.exception.RecursoNoEncontradoException;
import com.biblioteca.exception.TurnoPendienteException;
import com.biblioteca.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * RF12 - Cierre diario de la biblioteca.
 * - Manual: un bibliotecario/administrador lo genera al final del día (exige que no queden turnos abiertos).
 * - Automático: si nadie lo hizo, la tarea programada lo genera a las 23:55.
 */
@Service
public class CierreDiarioService {

    private final CierreDiarioRepository cierreRepository;
    private final PrestamoRepository prestamoRepository;
    private final DevolucionRepository devolucionRepository;
    private final MultaRepository multaRepository;
    private final PagoMultaRepository pagoMultaRepository;
    private final ReservaRepository reservaRepository;
    private final TurnoRepository turnoRepository;
    private final UsuarioService usuarioService;

    public CierreDiarioService(CierreDiarioRepository cierreRepository, PrestamoRepository prestamoRepository,
                               DevolucionRepository devolucionRepository, MultaRepository multaRepository,
                               PagoMultaRepository pagoMultaRepository, ReservaRepository reservaRepository,
                               TurnoRepository turnoRepository, UsuarioService usuarioService) {
        this.cierreRepository = cierreRepository;
        this.prestamoRepository = prestamoRepository;
        this.devolucionRepository = devolucionRepository;
        this.multaRepository = multaRepository;
        this.pagoMultaRepository = pagoMultaRepository;
        this.reservaRepository = reservaRepository;
        this.turnoRepository = turnoRepository;
        this.usuarioService = usuarioService;
    }

    /**
     * RF12 - Cierre manual del día de hoy.
     * - RecursoDuplicadoException (409) si hoy ya se cerró.
     * - TurnoPendienteException (400) si algún bibliotecario no cerró su turno.
     */
    @Transactional
    public CierreDiarioResponseDTO generarCierreDiario(Long idUsuario, CierreDiarioDTO datos) {
        Usuario usuario = usuarioService.buscarPersonal(idUsuario);
        LocalDate hoy = LocalDate.now();
        if (cierreRepository.existsByFecha(hoy)) {
            throw new RecursoDuplicadoException("El cierre del día " + hoy + " ya fue registrado");
        }
        long turnosAbiertos = turnoRepository.countByEstado(EstadoTurno.ABIERTO);
        if (turnosAbiertos > 0) {
            throw new TurnoPendienteException("No se puede cerrar el día: hay " + turnosAbiertos
                    + " turno(s) abierto(s). Ciérrenlos primero");
        }
        return aDTO(calcularYGuardar(hoy, usuario, datos == null ? null : datos.observaciones()));
    }

    /** Lo llama la tarea programada. Si el día ya se cerró, no hace nada. */
    @Transactional
    public Optional<CierreDiarioResponseDTO> generarCierreAutomatico() {
        LocalDate hoy = LocalDate.now();
        if (cierreRepository.existsByFecha(hoy)) {
            return Optional.empty();
        }
        long turnosAbiertos = turnoRepository.countByEstado(EstadoTurno.ABIERTO);
        String obs = "Cierre automático" + (turnosAbiertos > 0
                ? ". Atención: quedaron " + turnosAbiertos + " turno(s) sin cerrar" : "");
        return Optional.of(aDTO(calcularYGuardar(hoy, null, obs)));
    }

    /** GET /api/cierre-diario/{fecha} */
    @Transactional(readOnly = true)
    public CierreDiarioResponseDTO obtenerCierre(LocalDate fecha) {
        return aDTO(cierreRepository.findByFecha(fecha)
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay cierre registrado para " + fecha)));
    }

    /** GET /api/cierre-diario?desde=&hasta= (null = mes actual) */
    @Transactional(readOnly = true)
    public List<CierreDiarioResponseDTO> listarCierres(LocalDate desde, LocalDate hasta) {
        LocalDate[] r = ReporteService.rango(desde, hasta);
        return cierreRepository.findByFechaBetweenOrderByFechaDesc(r[0], r[1]).stream()
                .map(CierreDiarioService::aDTO).toList();
    }

    private CierreDiario calcularYGuardar(LocalDate fecha, Usuario usuario, String observaciones) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);

        List<Multa> multasDelDia = multaRepository.findByFechaGeneracionBetweenOrderByFechaGeneracionAsc(inicio, fin);
        BigDecimal montoMultas = multasDelDia.stream().map(Multa::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal recaudado = pagoMultaRepository.findByFechaPagoBetween(inicio, fin).stream()
                .map(PagoMulta::getMontoPagado).reduce(BigDecimal.ZERO, BigDecimal::add);

        CierreDiario c = new CierreDiario();
        c.setFecha(fecha);
        c.setPrestamosDelDia(prestamoRepository.countByFechaPrestamoBetween(inicio, fin));
        c.setDevolucionesDelDia(devolucionRepository.countByFechaDevolucionBetween(inicio, fin));
        c.setMultasGeneradas(multasDelDia.size());
        c.setMontoMultasGeneradas(montoMultas);
        c.setMontoRecaudado(recaudado);
        c.setPrestamosPendientes(prestamoRepository.countByEstado(EstadoPrestamo.ACTIVO));
        c.setPrestamosVencidos(prestamoRepository.countByEstadoAndFechaVencimientoBefore(EstadoPrestamo.ACTIVO, fecha));
        c.setReservasEnEspera(reservaRepository.countByEstado(EstadoReserva.PENDIENTE));
        c.setFechaGeneracion(LocalDateTime.now());
        c.setGeneradoPor(usuario);
        c.setObservaciones(observaciones);
        return cierreRepository.save(c);
    }

    private static CierreDiarioResponseDTO aDTO(CierreDiario c) {
        return new CierreDiarioResponseDTO(c.getId(), c.getFecha(), c.getPrestamosDelDia(),
                c.getDevolucionesDelDia(), c.getMultasGeneradas(), c.getMontoMultasGeneradas(),
                c.getMontoRecaudado(), c.getPrestamosPendientes(), c.getPrestamosVencidos(),
                c.getReservasEnEspera(), c.getFechaGeneracion(),
                c.getGeneradoPor() == null ? null : c.getGeneradoPor().getNombreCompleto(), c.getObservaciones());
    }
}
