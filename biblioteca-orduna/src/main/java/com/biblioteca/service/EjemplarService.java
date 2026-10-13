package com.biblioteca.service;

import com.biblioteca.dto.ActualizarEjemplarDTO;
import com.biblioteca.dto.EjemplarResponseDTO;
import com.biblioteca.dto.RegistroEjemplarDTO;
import com.biblioteca.entity.Ejemplar;
import com.biblioteca.entity.EstadoEjemplar;
import com.biblioteca.entity.Libro;
import com.biblioteca.exception.OperacionInvalidaException;
import com.biblioteca.exception.RecursoDuplicadoException;
import com.biblioteca.exception.RecursoNoEncontradoException;
import com.biblioteca.repository.EjemplarRepository;
import com.biblioteca.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;

/** RF14 - Ejemplares físicos (registrar, editar, dar de baja, consultar). */
@Service
public class EjemplarService {

    /** Estados que el personal puede poner a mano. PRESTADO y RESERVADO los maneja el sistema. */
    private static final EnumSet<EstadoEjemplar> MANUALES =
            EnumSet.of(EstadoEjemplar.DISPONIBLE, EstadoEjemplar.DANADO, EstadoEjemplar.PERDIDO, EstadoEjemplar.BAJA);

    private final EjemplarRepository ejemplarRepository;
    private final LibroRepository libroRepository;
    private final ReservaService reservaService;

    public EjemplarService(EjemplarRepository ejemplarRepository, LibroRepository libroRepository,
                           ReservaService reservaService) {
        this.ejemplarRepository = ejemplarRepository;
        this.libroRepository = libroRepository;
        this.reservaService = reservaService;
    }

    /**
     * RF14 - Registrar ejemplar. RecursoDuplicadoException si el código ya existe.
     * Si hay lectores esperando ese libro, el ejemplar nuevo se aparta para el primero de la cola.
     */
    @Transactional
    public EjemplarResponseDTO registrarEjemplar(Long idLibro, RegistroEjemplarDTO datos) {
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el libro con id " + idLibro));
        String codigo = datos.codigoInventario().trim().toUpperCase();
        if (ejemplarRepository.existsByCodigoInventario(codigo)) {
            throw new RecursoDuplicadoException("Ya existe un ejemplar con el código de inventario " + codigo);
        }
        Ejemplar e = new Ejemplar();
        e.setLibro(libro);
        e.setCodigoInventario(codigo);
        e.setUbicacion(datos.ubicacion());
        e.setEstado(EstadoEjemplar.DISPONIBLE);
        e = ejemplarRepository.save(e);
        reservaService.asignarEjemplarLiberado(e);
        return aDTO(e);
    }

    /** Pantalla Ejemplares: filtros opcionales por libro, estado y texto. */
    @Transactional(readOnly = true)
    public List<EjemplarResponseDTO> listarEjemplares(Long idLibro, EstadoEjemplar estado, String q) {
        return ejemplarRepository.buscar(idLibro, estado, q == null ? "" : q.trim()).stream()
                .map(EjemplarService::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public EjemplarResponseDTO obtenerEjemplar(Long idEjemplar) {
        return aDTO(buscar(idEjemplar));
    }

    /**
     * Editar ubicación y/o estado (DISPONIBLE, DANADO, PERDIDO, BAJA).
     * No se puede cambiar el estado de un ejemplar PRESTADO o RESERVADO.
     * Si vuelve a DISPONIBLE (ej. reparado), se ofrece a la cola de reservas.
     */
    @Transactional
    public EjemplarResponseDTO actualizarEjemplar(Long idEjemplar, ActualizarEjemplarDTO datos) {
        Ejemplar e = buscar(idEjemplar);
        if (datos.ubicacion() != null) {
            e.setUbicacion(datos.ubicacion().isBlank() ? null : datos.ubicacion().trim());
        }
        EstadoEjemplar nuevo = datos.estado();
        if (nuevo != null && nuevo != e.getEstado()) {
            if (!MANUALES.contains(nuevo)) {
                throw new OperacionInvalidaException("Estado no permitido: use DISPONIBLE, DANADO, PERDIDO o BAJA");
            }
            if (e.getEstado() == EstadoEjemplar.PRESTADO || e.getEstado() == EstadoEjemplar.RESERVADO) {
                throw new OperacionInvalidaException("El ejemplar está " + e.getEstado().name().toLowerCase()
                        + "; su estado cambia al registrar la devolución o atender la reserva");
            }
            if (nuevo == EstadoEjemplar.DISPONIBLE) {
                reservaService.asignarEjemplarLiberado(e);
            } else {
                e.setEstado(nuevo);
            }
        }
        return aDTO(ejemplarRepository.save(e));
    }

    private Ejemplar buscar(Long idEjemplar) {
        return ejemplarRepository.findById(idEjemplar)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el ejemplar con id " + idEjemplar));
    }

    static EjemplarResponseDTO aDTO(Ejemplar e) {
        return new EjemplarResponseDTO(e.getId(), e.getLibro().getId(), e.getLibro().getTitulo(),
                e.getCodigoInventario(), e.getUbicacion(), e.getEstado().name());
    }
}
