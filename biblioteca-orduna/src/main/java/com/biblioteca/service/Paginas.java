package com.biblioteca.service;

import com.biblioteca.dto.PaginaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.function.Function;

/** Ayudante para paginar: pagina empieza en 0, tamaño entre 1 y 100 (por defecto 10). */
final class Paginas {
    private Paginas() {}

    static Pageable de(Integer pagina, Integer tamanio) {
        int p = pagina == null || pagina < 0 ? 0 : pagina;
        int t = tamanio == null || tamanio < 1 ? 10 : Math.min(tamanio, 100);
        return PageRequest.of(p, t);
    }

    static <E, D> PaginaDTO<D> aDTO(Page<E> page, Function<E, D> mapper) {
        return new PaginaDTO<>(page.getContent().stream().map(mapper).toList(), page.getNumber(),
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
