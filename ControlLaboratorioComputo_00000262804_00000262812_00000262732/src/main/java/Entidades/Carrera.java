/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author Home
 */
public class Carrera {
    private Integer idCarrera;
    private String nombre;
    private Integer tiempoDiario;

    public Carrera() {
    }

    public Carrera(Integer idCarrera, String nombre, Integer tiempoDiario) {
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

    public Integer getTiempoDiario() {
        return tiempoDiario;
    }

    public void setTiempoDiario(Integer tiempoDiario) {
        this.tiempoDiario = tiempoDiario;
    }
    
    
}