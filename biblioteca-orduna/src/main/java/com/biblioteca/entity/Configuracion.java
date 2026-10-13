package com.biblioteca.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * RF13 - Pestaña "General" de Configuración y "Configuración de avisos" (RF11).
 * Tabla de una sola fila (id = 1). Las tarifas y días van versionados en ConfiguracionMulta.
 */
@Entity
@Table(name = "configuracion")
public class Configuracion {
    public static final Long ID_UNICO = 1L;

    @Id
    @Column(name = "id_configuracion")
    private Long id = ID_UNICO;

    @Column(name = "nombre_biblioteca", length = 150)
    private String nombreBiblioteca;

    @Column(name = "correo_contacto", length = 150)
    private String correoContacto;

    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;

    @Column(name = "hora_apertura")
    private LocalTime horaApertura;

    @Column(name = "hora_cierre")
    private LocalTime horaCierre;

    // ---- Interruptores de "Configuración de avisos" ----
    @Column(name = "aviso_vencimiento", nullable = false)
    private boolean avisoVencimiento = true;

    @Column(name = "aviso_reserva", nullable = false)
    private boolean avisoReserva = true;

    @Column(name = "aviso_multa", nullable = false)
    private boolean avisoMulta = true;

    @Column(name = "aviso_nuevo_libro", nullable = false)
    private boolean avisoNuevoLibro = true;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreBiblioteca() { return nombreBiblioteca; }
    public void setNombreBiblioteca(String v) { this.nombreBiblioteca = v; }
    public String getCorreoContacto() { return correoContacto; }
    public void setCorreoContacto(String v) { this.correoContacto = v; }
    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String v) { this.telefonoContacto = v; }
    public LocalTime getHoraApertura() { return horaApertura; }
    public void setHoraApertura(LocalTime v) { this.horaApertura = v; }
    public LocalTime getHoraCierre() { return horaCierre; }
    public void setHoraCierre(LocalTime v) { this.horaCierre = v; }
    public boolean isAvisoVencimiento() { return avisoVencimiento; }
    public void setAvisoVencimiento(boolean v) { this.avisoVencimiento = v; }
    public boolean isAvisoReserva() { return avisoReserva; }
    public void setAvisoReserva(boolean v) { this.avisoReserva = v; }
    public boolean isAvisoMulta() { return avisoMulta; }
    public void setAvisoMulta(boolean v) { this.avisoMulta = v; }
    public boolean isAvisoNuevoLibro() { return avisoNuevoLibro; }
    public void setAvisoNuevoLibro(boolean v) { this.avisoNuevoLibro = v; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime v) { this.fechaActualizacion = v; }
}
