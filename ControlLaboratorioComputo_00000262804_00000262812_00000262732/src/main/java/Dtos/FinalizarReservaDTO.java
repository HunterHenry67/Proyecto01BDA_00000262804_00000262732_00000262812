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
public class FinalizarReservaDTO {
    private int idReserva;
    private LocalDateTime fechaHoraFinal;

    public FinalizarReservaDTO() {
    }

    public FinalizarReservaDTO(int idReserva, LocalDateTime fechaHoraFinal) {
        this.idReserva = idReserva;
        this.fechaHoraFinal = fechaHoraFinal;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdReserva() {
        return idReserva;
    }
    public LocalDateTime getFechaHoraFinal() {
        return fechaHoraFinal;
    }

    public void setFechaHoraFinal(LocalDateTime fechaHoraFinal) {
        this.fechaHoraFinal = fechaHoraFinal;
    }
}
