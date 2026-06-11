/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.sql.Time;

/**
 *
 * @author Home
 */
public class CentroComputo {
    private Integer idCentroComputo;
    private Time HoraInicio;
    private Time HOranFin;
    private String contraseniaMaestra;
    private Integer idUnidadAcademica;

    public CentroComputo() {
    }

    public CentroComputo(Integer idCentroComputo, Time HoraInicio, Time HOranFin, String contraseniaMaestra, Integer idUnidadAcademica) {
        this.idCentroComputo = idCentroComputo;
        this.HoraInicio = HoraInicio;
        this.HOranFin = HOranFin;
        this.contraseniaMaestra = contraseniaMaestra;
        this.idUnidadAcademica = idUnidadAcademica;
    }

    public Integer getIdCentroComputo() {
        return idCentroComputo;
    }

    public void setIdCentroComputo(Integer idCentroComputo) {
        this.idCentroComputo = idCentroComputo;
    }

    public Time getHoraInicio() {
        return HoraInicio;
    }

    public void setHoraInicio(Time HoraInicio) {
        this.HoraInicio = HoraInicio;
    }

    public Time getHOranFin() {
        return HOranFin;
    }

    public void setHOranFin(Time HOranFin) {
        this.HOranFin = HOranFin;
    }

    public String getContraseniaMaestra() {
        return contraseniaMaestra;
    }

    public void setContraseniaMaestra(String contraseniaMaestra) {
        this.contraseniaMaestra = contraseniaMaestra;
    }

    public Integer getIdUnidadAcademica() {
        return idUnidadAcademica;
    }

    public void setIdUnidadAcademica(Integer idUnidadAcademica) {
        this.idUnidadAcademica = idUnidadAcademica;
    }
}
