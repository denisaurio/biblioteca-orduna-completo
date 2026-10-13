package com.biblioteca.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_libro")
    private Libro libro;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario lector;

    /** Ejemplar apartado cuando la reserva pasa a NOTIFICADA. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ejemplar_asignado")
    private Ejemplar ejemplarAsignado;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

    /** Cuándo se le apartó el ejemplar (paso a NOTIFICADA). */
    @Column(name = "fecha_notificacion")
    private LocalDateTime fechaNotificacion;

    /** Último día para recoger el ejemplar apartado; después la reserva VENCE. */
    @Column(name = "fecha_limite_retiro")
    private java.time.LocalDate fechaLimiteRetiro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Libro getLibro() { return libro; }
    public void setLibro(Libro libro) { this.libro = libro; }
    public Usuario getLector() { return lector; }
    public void setLector(Usuario lector) { this.lector = lector; }
    public Ejemplar getEjemplarAsignado() { return ejemplarAsignado; }
    public void setEjemplarAsignado(Ejemplar ejemplarAsignado) { this.ejemplarAsignado = ejemplarAsignado; }
    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDateTime fechaReserva) { this.fechaReserva = fechaReserva; }
    public LocalDateTime getFechaNotificacion() { return fechaNotificacion; }
    public void setFechaNotificacion(LocalDateTime fechaNotificacion) { this.fechaNotificacion = fechaNotificacion; }
    public java.time.LocalDate getFechaLimiteRetiro() { return fechaLimiteRetiro; }
    public void setFechaLimiteRetiro(java.time.LocalDate fechaLimiteRetiro) { this.fechaLimiteRetiro = fechaLimiteRetiro; }
    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }
}
