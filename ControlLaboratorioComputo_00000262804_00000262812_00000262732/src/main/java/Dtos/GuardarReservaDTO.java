/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dtos;

/**
 *
 * @author BALAMRUSH
 */
public class GuardarReservaDTO {
    private int idAlumno;
    private int idComputadora;

    public GuardarReservaDTO(int idAlumno, int idComputadora) {
        this.idAlumno = idAlumno;
        this.idComputadora = idComputadora;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public int getIdComputadora() {
        return idComputadora;
    }

}
