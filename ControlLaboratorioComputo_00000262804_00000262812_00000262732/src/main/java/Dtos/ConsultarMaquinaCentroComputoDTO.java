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
public class ConsultarMaquinaCentroComputoDTO {
    private Integer maquina;
    private Integer idAlumno;
    private String nombreAlumno;
    private String carrera;
    private String unidadAcademica;
    private String centroDeComputo;
    private LocalTime horaInicio;

    public ConsultarMaquinaCentroComputoDTO(Integer maquina) {
        this.maquina = maquina;
    }

    public Integer getMaquina() {
        return maquina;
    }

    public void setMaquina(Integer maquina) {
        this.maquina = maquina;
    }

    public Integer getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(Integer idAlumno) {
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
