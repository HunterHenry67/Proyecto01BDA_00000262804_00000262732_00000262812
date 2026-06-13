/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author Home
 */
public class UnidadAcademica {
    private Integer idUnidadAcademica;
    private String nombre;
    
    public UnidadAcademica(){
        
    }

    public UnidadAcademica(Integer idUnidadAcademica, String nombre) {
        this.idUnidadAcademica = idUnidadAcademica;
        this.nombre = nombre;
    }

    public Integer getIdUnidadAcademica() {
        return idUnidadAcademica;
    }

    public void setIdUnidadAcademica(Integer idUnidadAcademica) {
        this.idUnidadAcademica = idUnidadAcademica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
    
}
