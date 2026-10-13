package com.biblioteca.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** RF17 - Sanciones y bloqueos. */
@Entity
@Table(name = "sanciones")
public class Sancion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sancion")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    /** Bibliotecario (o gerente) responsable de la sanción. */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bibliotecario")
    private Usuario bibliotecario;

    @Column(nullable = false, length = 300)
    private String motivo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    /** La sanción está activa hasta este día inclusive (salvo que se levante antes). */
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    /** Si se levantó antes de tiempo. null = no se levantó. */
    @Column(name = "fecha_levantamiento")
    private LocalDateTime fechaLevantamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_levantado_por")
    private Usuario levantadoPor;

    /** ACTIVA mientras no se levante y no pase fecha_fin. */
    public boolean estaActiva(LocalDate hoy) {
        return fechaLevantamiento == null && !fechaFin.isBefore(hoy);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Usuario getBibliotecario() { return bibliotecario; }
    public void setBibliotecario(Usuario bibliotecario) { this.bibliotecario = bibliotecario; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public LocalDateTime getFechaLevantamiento() { return fechaLevantamiento; }
    public void setFechaLevantamiento(LocalDateTime v) { this.fechaLevantamiento = v; }
    public Usuario getLevantadoPor() { return levantadoPor; }
    public void setLevantadoPor(Usuario v) { this.levantadoPor = v; }
}
