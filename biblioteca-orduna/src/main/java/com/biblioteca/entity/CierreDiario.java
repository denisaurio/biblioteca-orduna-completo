package com.biblioteca.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * RF12 - Corte consolidado de la biblioteca al final del día (uno por fecha).
 * Distinto de Turno: el turno es por bibliotecario; el cierre diario es de toda la biblioteca.
 */
@Entity
@Table(name = "cierres_diarios")
public class CierreDiario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cierre")
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate fecha;

    // --- Movimiento del día ---
    @Column(name = "prestamos_del_dia", nullable = false)
    private long prestamosDelDia;

    @Column(name = "devoluciones_del_dia", nullable = false)
    private long devolucionesDelDia;

    @Column(name = "multas_generadas", nullable = false)
    private long multasGeneradas;

    @Column(name = "monto_multas_generadas", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoMultasGeneradas;

    @Column(name = "monto_recaudado", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoRecaudado;

    // --- Foto al momento del cierre ---
    /** Total de préstamos pendientes de devolución (ACTIVO) al cerrar. */
    @Column(name = "prestamos_pendientes", nullable = false)
    private long prestamosPendientes;

    /** De los pendientes, cuántos ya están vencidos. */
    @Column(name = "prestamos_vencidos", nullable = false)
    private long prestamosVencidos;

    @Column(name = "reservas_en_espera", nullable = false)
    private long reservasEnEspera;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    /** Quién hizo el cierre. null = lo generó la tarea automática. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario generadoPor;

    @Column(length = 500)
    private String observaciones;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public long getPrestamosDelDia() { return prestamosDelDia; }
    public void setPrestamosDelDia(long v) { this.prestamosDelDia = v; }
    public long getDevolucionesDelDia() { return devolucionesDelDia; }
    public void setDevolucionesDelDia(long v) { this.devolucionesDelDia = v; }
    public long getMultasGeneradas() { return multasGeneradas; }
    public void setMultasGeneradas(long v) { this.multasGeneradas = v; }
    public BigDecimal getMontoMultasGeneradas() { return montoMultasGeneradas; }
    public void setMontoMultasGeneradas(BigDecimal v) { this.montoMultasGeneradas = v; }
    public BigDecimal getMontoRecaudado() { return montoRecaudado; }
    public void setMontoRecaudado(BigDecimal v) { this.montoRecaudado = v; }
    public long getPrestamosPendientes() { return prestamosPendientes; }
    public void setPrestamosPendientes(long v) { this.prestamosPendientes = v; }
    public long getPrestamosVencidos() { return prestamosVencidos; }
    public void setPrestamosVencidos(long v) { this.prestamosVencidos = v; }
    public long getReservasEnEspera() { return reservasEnEspera; }
    public void setReservasEnEspera(long v) { this.reservasEnEspera = v; }
    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime v) { this.fechaGeneracion = v; }
    public Usuario getGeneradoPor() { return generadoPor; }
    public void setGeneradoPor(Usuario v) { this.generadoPor = v; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String v) { this.observaciones = v; }
}
