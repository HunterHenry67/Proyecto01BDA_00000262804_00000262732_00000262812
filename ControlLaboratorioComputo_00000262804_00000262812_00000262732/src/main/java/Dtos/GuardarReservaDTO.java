/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dtos;

import java.time.LocalDateTime;

/**
 *
 * @author BALAMRUSH
 */
public class GuardarReservaDTO {
    private LocalDateTime fechaHoraApartado;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFinal;
    private Integer tiempoUso;
    private Integer idAlumno;
    private Integer idComputadora;

    public GuardarReservaDTO(LocalDateTime fechaHoraApartado, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFinal, Integer tiempoUso, Integer idAlumno, Integer idComputadora) {
        this.fechaHoraApartado = fechaHoraApartado;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFinal = fechaHoraFinal;
        this.tiempoUso = tiempoUso;
        this.idAlumno = idAlumno;
        this.idComputadora = idComputadora;
    }

    public LocalDateTime getFechaHoraApartado() {
        return fechaHoraApartado;
    }

    public void setFechaHoraApartado(LocalDateTime fechaHoraApartado) {
        this.fechaHoraApartado = fechaHoraApartado;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFinal() {
        return fechaHoraFinal;
    }

    public void setFechaHoraFinal(LocalDateTime fechaHoraFinal) {
        this.fechaHoraFinal = fechaHoraFinal;
    }

    public Integer getTiempoUso() {
        return tiempoUso;
    }

    public void setTiempoUso(Integer tiempoUso) {
        this.tiempoUso = tiempoUso;
    }

    public Integer getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(Integer idAlumno) {
        this.idAlumno = idAlumno;
    }

    public Integer getIdComputadora() {
        return idComputadora;
    }

    public void setIdComputadora(Integer idComputadora) {
        this.idComputadora = idComputadora;
    }
    
    

    

}
