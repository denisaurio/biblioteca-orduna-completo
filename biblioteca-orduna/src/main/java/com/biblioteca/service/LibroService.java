package com.biblioteca.service;

import com.biblioteca.dto.AutorResponseDTO;
import com.biblioteca.dto.GeneroResponseDTO;
import com.biblioteca.dto.LibroResponseDTO;
import com.biblioteca.dto.RegistroLibroDTO;
import com.biblioteca.entity.*;
import com.biblioteca.exception.RecursoDuplicadoException;
import com.biblioteca.exception.RecursoNoEncontradoException;
import com.biblioteca.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/** RF01 / RF02 - Catálogo de libros. */
@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final EjemplarRepository ejemplarRepository;
    private final GeneroRepository generoRepository;
    private final PrestamoRepository prestamoRepository;
    private final AutorService autorService;
    private final NotificacionService notificacionService;

    public LibroService(LibroRepository libroRepository, AutorRepository autorRepository,
                        EjemplarRepository ejemplarRepository, GeneroRepository generoRepository,
                        PrestamoRepository prestamoRepository, AutorService autorService,
                        NotificacionService notificacionService) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.ejemplarRepository = ejemplarRepository;
        this.generoRepository = generoRepository;
        this.prestamoRepository = prestamoRepository;
        this.autorService = autorService;
        this.notificacionService = notificacionService;
    }

    /** RF01 - Registrar libro. RecursoDuplicadoException si el ISBN ya existe. Avisa a los lectores. */
    @Transactional
    public LibroResponseDTO registrarLibro(RegistroLibroDTO datos) {
        String isbn = normalizarIsbn(datos.isbn());
        if (libroRepository.existsByIsbn(isbn)) {
            throw new RecursoDuplicadoException("Ya existe un libro con el ISBN " + isbn);
        }
        Libro libro = new Libro();
        copiar(datos, isbn, libro);
        libro = libroRepository.save(libro);
        notificacionService.notificarNuevoLibro(libro);
        return aDTO(libro);
    }

    /** Editar libro (bibliotecario/gerente). */
    @Transactional
    public LibroResponseDTO actualizarLibro(Long idLibro, RegistroLibroDTO datos) {
        Libro libro = buscar(idLibro);
        String isbn = normalizarIsbn(datos.isbn());
        if (libroRepository.existsByIsbnAndIdNot(isbn, idLibro)) {
            throw new RecursoDuplicadoException("Ya existe otro libro con el ISBN " + isbn);
        }
        copiar(datos, isbn, libro);
        return aDTO(libroRepository.save(libro));
    }

    /** RF02 - Buscar libros por título, ISBN, autor o género. Criterio vacío = todos. */
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> buscarLibros(String criterio) {
        String q = criterio == null ? "" : criterio.trim();
        return libroRepository.buscar(q).stream().map(this::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public LibroResponseDTO obtenerLibro(Long idLibro) {
        return aDTO(buscar(idLibro));
    }

    /** "Ver libros" de un autor (tabla libros_autores). */
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> librosDeAutor(Long idAutor) {
        if (!autorRepository.existsById(idAutor)) {
            throw new RecursoNoEncontradoException("No existe el autor con id " + idAutor);
        }
        return libroRepository.findByAutoresIdOrderByTituloAsc(idAutor).stream().map(this::aDTO).toList();
    }

    /** "Libros destacados" del Inicio: los más prestados; si faltan, los más recientes. */
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> destacados(int cantidad) {
        LinkedHashSet<Long> ids = new LinkedHashSet<>();
        for (Object[] fila : prestamoRepository.contarPorLibro()) {
            if (ids.size() >= cantidad) break;
            ids.add((Long) fila[0]);
        }
        for (Libro l : libroRepository.findTop6ByOrderByIdDesc()) {
            if (ids.size() >= cantidad) break;
            ids.add(l.getId());
        }
        Map<Long, Libro> porId = new HashMap<>();
        libroRepository.findAllById(ids).forEach(l -> porId.put(l.getId(), l));
        return ids.stream().map(porId::get).filter(Objects::nonNull).map(this::aDTO).toList();
    }

    Libro buscar(Long idLibro) {
        return libroRepository.findById(idLibro)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el libro con id " + idLibro));
    }

    private void copiar(RegistroLibroDTO datos, String isbn, Libro libro) {
        libro.setTitulo(datos.titulo().trim());
        libro.setIsbn(isbn);
        libro.setEditorial(datos.editorial());
        libro.setAnioPublicacion(datos.anioPublicacion());
        libro.setNumPaginas(datos.numPaginas());

        List<Autor> autores = autorRepository.findAllById(datos.idsAutores());
        if (autores.size() != new HashSet<>(datos.idsAutores()).size()) {
            throw new RecursoNoEncontradoException("Uno o más autores de 'ids_autores' no existen");
        }
        libro.setAutores(new HashSet<>(autores));

        List<Genero> generos = generoRepository.findAllById(datos.idsGeneros());
        if (generos.size() != new HashSet<>(datos.idsGeneros()).size()) {
            throw new RecursoNoEncontradoException("Uno o más géneros de 'ids_generos' no existen");
        }
        libro.setGeneros(new HashSet<>(generos));
    }

    LibroResponseDTO aDTO(Libro l) {
        List<AutorResponseDTO> autores = l.getAutores().stream()
                .map(autorService::aDTO)
                .sorted(Comparator.comparing(AutorResponseDTO::nombre))
                .toList();
        List<GeneroResponseDTO> generos = l.getGeneros().stream()
                .map(GeneroService::aDTO)
                .sorted(Comparator.comparing(GeneroResponseDTO::nombre))
                .toList();
        long total = ejemplarRepository.countByLibroId(l.getId());
        long disponibles = ejemplarRepository.countByLibroIdAndEstado(l.getId(), EstadoEjemplar.DISPONIBLE);
        String estado = total == 0 ? "SIN_EJEMPLARES" : disponibles > 0 ? "DISPONIBLE" : "PRESTADO";
        return new LibroResponseDTO(l.getId(), l.getTitulo(), l.getIsbn(), l.getEditorial(),
                l.getAnioPublicacion(), l.getNumPaginas(), autores, generos, total, disponibles, estado);
    }

    /** Quita guiones y espacios: "978-612-4" == "9786124". */
    private static String normalizarIsbn(String isbn) {
        return isbn.replaceAll("[\\s-]", "").toUpperCase();
    }
}
