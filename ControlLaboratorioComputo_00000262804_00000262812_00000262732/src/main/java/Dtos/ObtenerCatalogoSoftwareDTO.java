/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dtos;

/**
 *
 * @author Andre
 */
public class ObtenerCatalogoSoftwareDTO {

    private Integer idComputadora;

    public ObtenerCatalogoSoftwareDTO() {
    }

    public ObtenerCatalogoSoftwareDTO(Integer idComputadora) {
        this.idComputadora = idComputadora;
    }

    public Integer getIdComputadora() {
        return idComputadora;
    }

    public void setIdComputadora(Integer idComputadora) {
        this.idComputadora = idComputadora;
    }
}
