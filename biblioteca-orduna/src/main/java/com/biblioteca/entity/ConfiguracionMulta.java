package com.biblioteca.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * RF13 - Pestaña "Multas y Reservas", versionada por vigencia (tabla configuracion_multas).
 * Cada cambio cierra la fila vigente (fecha_vigencia_hasta) y crea una nueva, así los cambios
 * no afectan de forma retroactiva a préstamos y multas ya calculados.
 * Fila vigente = la que tiene fecha_vigencia_hasta en null.
 */
@Entity
@Table(name = "configuracion_multas")
public class ConfiguracionMulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuracion")
    private Long id;

    @Column(name = "tarifa_multa_diaria", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaMultaDiaria;

    @Column(name = "dias_max_prestamo", nullable = false)
    private Integer diasMax;

    /** Días que tiene el lector para recoger un libro reservado que ya quedó disponible. */
    @Column(name = "dias_retiro_reserva", nullable = false)
    private Integer diasRetiroReserva;

    @Column(name = "fecha_vigencia_desde", nullable = false)
    private LocalDateTime fechaVigenciaDesde;

    @Column(name = "fecha_vigencia_hasta")
    private LocalDateTime fechaVigenciaHasta;

    /** Gerente que hizo el cambio (null en la configuración inicial). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gerente")
    private Usuario modificadoPor;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getTarifaMultaDiaria() { return tarifaMultaDiaria; }
    public void setTarifaMultaDiaria(BigDecimal v) { this.tarifaMultaDiaria = v; }
    public Integer getDiasMax() { return diasMax; }
    public void setDiasMax(Integer v) { this.diasMax = v; }
    public Integer getDiasRetiroReserva() { return diasRetiroReserva; }
    public void setDiasRetiroReserva(Integer v) { this.diasRetiroReserva = v; }
    public LocalDateTime getFechaVigenciaDesde() { return fechaVigenciaDesde; }
    public void setFechaVigenciaDesde(LocalDateTime v) { this.fechaVigenciaDesde = v; }
    public LocalDateTime getFechaVigenciaHasta() { return fechaVigenciaHasta; }
    public void setFechaVigenciaHasta(LocalDateTime v) { this.fechaVigenciaHasta = v; }
    public Usuario getModificadoPor() { return modificadoPor; }
    public void setModificadoPor(Usuario v) { this.modificadoPor = v; }
}
