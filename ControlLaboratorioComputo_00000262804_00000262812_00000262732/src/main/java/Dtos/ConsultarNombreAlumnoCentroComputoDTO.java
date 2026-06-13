/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dtos;

import java.time.LocalTime;

/**
 *
 * @author Andre
 */
public class ConsultarNombreAlumnoCentroComputoDTO {
    private String maquina;
    private String idAlumno;
    private String nombreAlumno;
    private String carrera;
    private String unidadAcademica;
    private String centroDeComputo;
    private LocalTime horaInicio;

    public ConsultarNombreAlumnoCentroComputoDTO(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    
    
    public String getMaquina() {
        return maquina;
    }

    public void setMaquina(String maquina) {
        this.maquina = maquina;
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(String unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public String getCentroDeComputo() {
        return centroDeComputo;
    }

    public void setCentroDeComputo(String centroDeComputo) {
        this.centroDeComputo = centroDeComputo;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
  
}
