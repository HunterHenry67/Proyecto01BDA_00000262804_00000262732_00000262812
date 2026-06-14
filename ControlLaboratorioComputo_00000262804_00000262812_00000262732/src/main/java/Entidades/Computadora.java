/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author Home
 */
public class Computadora {
    private Integer idComputadora;
    private String tipo;
    private Integer numeroMaquina;
    private String ip;
    private boolean estatus;
    private int idCentroComputo;

    public Computadora() {
    }

    public Computadora(Integer idComputadora, String tipo, Integer numeroMaquina, String ip, boolean estatus, int idCentroComputo) {
        this.idComputadora = idComputadora;
        this.tipo = tipo;
        this.numeroMaquina = numeroMaquina;
        this.ip = ip;
        this.estatus = estatus;
        this.idCentroComputo = idCentroComputo;
    }

    public Integer getIdComputadora() {
        return idComputadora;
    }

    public void setIdComputadora(Integer idComputadora) {
        this.idComputadora = idComputadora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getNumeroMaquina() {
        return numeroMaquina;
    }

    public void setNumeroMaquina(Integer numeroMaquina) {
        this.numeroMaquina = numeroMaquina;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public int getIdCentroComputo() {
        return idCentroComputo;
    }

    public void setIdCentroComputo(int idCentroComputo) {
        this.idCentroComputo = idCentroComputo;
    }
      
}
