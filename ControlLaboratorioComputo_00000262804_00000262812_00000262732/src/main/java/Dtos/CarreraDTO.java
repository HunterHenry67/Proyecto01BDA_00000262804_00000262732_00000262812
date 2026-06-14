/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dtos;

/**
 *
 * @author user
 */
public class CarreraDTO {
    private int idCarrera;
    private String nombre;
    private int tiempoDiario; 
    private String tiempoFormateado; 

    public CarreraDTO() {
    }

    public CarreraDTO(int idCarrera, String nombre, int tiempoDiario, String tiempoFormateado) {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
        this.tiempoDiario = tiempoDiario;
        this.tiempoFormateado = tiempoFormateado;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTiempoDiario() {
        return tiempoDiario;
    }

    public void setTiempoDiario(int tiempoDiario) {
        this.tiempoDiario = tiempoDiario;
    }

    public String getTiempoFormateado() {
        return tiempoFormateado;
    }

    public void setTiempoFormateado(String tiempoFormateado) {
        this.tiempoFormateado = tiempoFormateado;
    }
}
