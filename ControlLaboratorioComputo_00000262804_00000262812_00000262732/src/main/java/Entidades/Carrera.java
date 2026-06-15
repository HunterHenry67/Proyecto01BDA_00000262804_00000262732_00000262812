/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.time.LocalTime;

/**
 *
 * @author Home
 */
public class Carrera {
    private Integer idCarrera;
    private String nombre;
    private LocalTime tiempoDiario;

    public Carrera() {
    }

    public Carrera(Integer idCarrera, String nombre, LocalTime tiempoDiario) {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
        this.tiempoDiario = tiempoDiario;
    }

    public Integer getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Integer idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalTime getTiempoDiario() {
        return tiempoDiario;
    }

    public void setTiempoDiario(LocalTime tiempoDiario) {
        this.tiempoDiario = tiempoDiario;
    }
    
    
}