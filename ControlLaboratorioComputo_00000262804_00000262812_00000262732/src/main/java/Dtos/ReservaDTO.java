/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dtos;

import java.sql.Timestamp;

/**
 *
 * @author user
 */
public class ReservaDTO {
    private int idReserva;
    private Timestamp fechaHoraApartado;
    private Timestamp fechaHoraInicio;
    private Timestamp fechaHoraFinal;
    private int tiempoUso;
    private int idAlumno; 
    private int idComputadora;
    private String nombreAlumno;

    public ReservaDTO() {
    }

    public ReservaDTO(int idReserva, Timestamp fechaHoraApartado, Timestamp fechaHoraInicio, Timestamp fechaHoraFinal, int tiempoUso, int idAlumno, int idComputadora, String nombreAlumno) {
        this.idReserva = idReserva;
        this.fechaHoraApartado = fechaHoraApartado;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFinal = fechaHoraFinal;
        this.tiempoUso = tiempoUso;
        this.idAlumno = idAlumno;
        this.idComputadora = idComputadora;
        this.nombreAlumno = nombreAlumno;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public Timestamp getFechaHoraApartado() {
        return fechaHoraApartado;
    }

    public void setFechaHoraApartado(Timestamp fechaHoraApartado) {
        this.fechaHoraApartado = fechaHoraApartado;
    }

    public Timestamp getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Timestamp fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Timestamp getFechaHoraFinal() {
        return fechaHoraFinal;
    }

    public void setFechaHoraFinal(Timestamp fechaHoraFinal) {
        this.fechaHoraFinal = fechaHoraFinal;
    }

    public int getTiempoUso() {
        return tiempoUso;
    }

    public void setTiempoUso(int tiempoUso) {
        this.tiempoUso = tiempoUso;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getIdComputadora() {
        return idComputadora;
    }

    public void setIdComputadora(int idComputadora) {
        this.idComputadora = idComputadora;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }
    
    
}
