/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.util.Date;

/**
 *
 * @author Home
 */
public class Bloqueo {
    private Integer idBloqueo;
    private Date fechaHoraIncioBloqueo;
    private Date fechaHoraFinalBloqueo;
    private String motivo;

    public Bloqueo() {
    }

    public Bloqueo(Integer idBloqueo, Date fechaHoraIncioBloqueo, Date fechaHoraFinalBloqueo, String motivo) {
        this.idBloqueo = idBloqueo;
        this.fechaHoraIncioBloqueo = fechaHoraIncioBloqueo;
        this.fechaHoraFinalBloqueo = fechaHoraFinalBloqueo;
        this.motivo = motivo;
    }

    public Integer getIdBloqueo() {
        return idBloqueo;
    }

    public void setIdBloqueo(Integer idBloqueo) {
        this.idBloqueo = idBloqueo;
    }

    public Date getFechaHoraIncioBloqueo() {
        return fechaHoraIncioBloqueo;
    }

    public void setFechaHoraIncioBloqueo(Date fechaHoraIncioBloqueo) {
        this.fechaHoraIncioBloqueo = fechaHoraIncioBloqueo;
    }

    public Date getFechaHoraFinalBloqueo() {
        return fechaHoraFinalBloqueo;
    }

    public void setFechaHoraFinalBloqueo(Date fechaHoraFinalBloqueo) {
        this.fechaHoraFinalBloqueo = fechaHoraFinalBloqueo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    
    
}
