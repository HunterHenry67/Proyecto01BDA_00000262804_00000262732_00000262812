/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dtos;

/**
 *
 * @author user
 */
public class ComputadoraDTO {
    private int idComputadora;
    private int numeroMaquina;
    private String direccionIP;
    private boolean estatus;
    private String tipo;
    private int idCentroComputo;
    private String nombreCentro;

    public ComputadoraDTO() {
    }

    public ComputadoraDTO(int idComputadora, int numeroMaquina, String direccionIP, boolean estatus, String tipo, int idCentroComputo, String nombreCentro) {
        this.idComputadora = idComputadora;
        this.numeroMaquina = numeroMaquina;
        this.direccionIP = direccionIP;
        this.estatus = estatus;
        this.tipo = tipo;
        this.idCentroComputo = idCentroComputo;
        this.nombreCentro = nombreCentro;
    }

    public int getIdComputadora() {
        return idComputadora;
    }

    public void setIdComputadora(int idComputadora) {
        this.idComputadora = idComputadora;
    }

    public int getNumeroMaquina() {
        return numeroMaquina;
    }

    public void setNumeroMaquina(int numeroMaquina) {
        this.numeroMaquina = numeroMaquina;
    }

    public String getDireccionIP() {
        return direccionIP;
    }

    public void setDireccionIP(String direccionIP) {
        this.direccionIP = direccionIP;
    }

    public boolean isEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdCentroComputo() {
        return idCentroComputo;
    }

    public void setIdCentroComputo(int idCentroComputo) {
        this.idCentroComputo = idCentroComputo;
    }

    public String getNombreCentro() {
        return nombreCentro;
    }

    public void setNombreCentro(String nombreCentro) {
        this.nombreCentro = nombreCentro;
    }
    
    
    
    
}
