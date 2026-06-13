/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.time.LocalDateTime;


/**
 *
 * @author Home
 */
public class Bloqueo {
    private Integer idBloqueo;
    private LocalDateTime fechaHoraIncioBloqueo;
    private LocalDateTime fechaHoraFinalBloqueo;
    private String motivo;
    private int idAlumno;

    public Bloqueo() {
    }

    public Bloqueo(Integer idBloqueo, LocalDateTime fechaHoraIncioBloqueo, LocalDateTime fechaHoraFinalBloqueo, String motivo, int idAlumno) {
        this.idBloqueo = idBloqueo;
        this.fechaHoraIncioBloqueo = fechaHoraIncioBloqueo;
        this.fechaHoraFinalBloqueo = fechaHoraFinalBloqueo;
        this.motivo = motivo;
        this.idAlumno = idAlumno;
    }

    public Integer getIdBloqueo() {
        return idBloqueo;
    }

    public void setIdBloqueo(Integer idBloqueo) {
        this.idBloqueo = idBloqueo;
    }

    public LocalDateTime getFechaHoraIncioBloqueo() {
        return fechaHoraIncioBloqueo;
    }

    public void setFechaHoraIncioBloqueo(LocalDateTime fechaHoraIncioBloqueo) {
        this.fechaHoraIncioBloqueo = fechaHoraIncioBloqueo;
    }

    public LocalDateTime getFechaHoraFinalBloqueo() {
        return fechaHoraFinalBloqueo;
    }

    public void setFechaHoraFinalBloqueo(LocalDateTime fechaHoraFinalBloqueo) {
        this.fechaHoraFinalBloqueo = fechaHoraFinalBloqueo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }  
}
