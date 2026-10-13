package com.biblioteca.service;

import com.biblioteca.dto.DashboardDTO;
import com.biblioteca.dto.MultaResponseDTO;
import com.biblioteca.dto.PanelLectorDTO;
import com.biblioteca.entity.*;
import com.biblioteca.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumSet;

/** Pantalla INICIO: panel completo (bibliotecario/gerente) y versión reducida (lector). */
@Service
public class DashboardService {

    private static final int DESTACADOS = 6;

    private final LibroRepository libroRepository;
    private final EjemplarRepository ejemplarRepository;
    private final PrestamoRepository prestamoRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MultaRepository multaRepository;
    private final LibroService libroService;
    private final PrestamoService prestamoService;
    private final ReservaService reservaService;
    private final DevolucionService devolucionService;
    private final NotificacionService notificacionService;
    private final UsuarioService usuarioService;

    public DashboardService(LibroRepository libroRepository, EjemplarRepository ejemplarRepository,
                            PrestamoRepository prestamoRepository, ReservaRepository reservaRepository,
                            UsuarioRepository usuarioRepository, MultaRepository multaRepository,
                            LibroService libroService, PrestamoService prestamoService,
                            ReservaService reservaService, DevolucionService devolucionService,
                            NotificacionService notificacionService, UsuarioService usuarioService) {
        this.libroRepository = libroRepository;
        this.ejemplarRepository = ejemplarRepository;
        this.prestamoRepository = prestamoRepository;
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.multaRepository = multaRepository;
        this.libroService = libroService;
        this.prestamoService = prestamoService;
        this.reservaService = reservaService;
        this.devolucionService = devolucionService;
        this.notificacionService = notificacionService;
        this.usuarioService = usuarioService;
    }

    /** KPIs en tiempo real + libros destacados. */
    @Transactional(readOnly = true)
    public DashboardDTO dashboard() {
        return new DashboardDTO(
                libroRepository.count(),
                ejemplarRepository.count(),
                ejemplarRepository.countByEstado(EstadoEjemplar.DISPONIBLE),
                prestamoRepository.countByEstado(EstadoPrestamo.ACTIVO),
                prestamoRepository.countByEstadoAndFechaVencimientoBefore(EstadoPrestamo.ACTIVO, LocalDate.now()),
                reservaRepository.countByEstado(EstadoReserva.PENDIENTE),
                usuarioRepository.countByRol(Rol.LECTOR),
                multaRepository.findByEstadoIn(EnumSet.of(EstadoMulta.PENDIENTE, EstadoMulta.PARCIAL)).size(),
                libroService.destacados(DESTACADOS));
    }

    /** Panel del lector: sus préstamos activos, reservas activas, deuda y avisos sin leer. */
    @Transactional(readOnly = true)
    public PanelLectorDTO panelLector(Long idLector) {
        Usuario lector = usuarioService.buscar(idLector);
        BigDecimal deuda = devolucionService.misMultas(idLector).stream()
                .map(MultaResponseDTO::saldo).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new PanelLectorDTO(
                usuarioService.estadoDe(lector).name(),
                prestamoService.misPrestamos(idLector).stream().filter(p -> !p.estado().equals("DEVUELTO")).toList(),
                reservaService.misReservas(idLector).stream()
                        .filter(r -> r.estado().equals("PENDIENTE") || r.estado().equals("NOTIFICADA")).toList(),
                deuda,
                notificacionService.contarNoLeidas(idLector),
                libroService.destacados(DESTACADOS));
    }
}
