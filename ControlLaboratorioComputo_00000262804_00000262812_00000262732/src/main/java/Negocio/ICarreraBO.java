/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;

import Entidades.Carrera;

/**
 *
 * @author BALAMRUSH
 */
public interface ICarreraBO {
    
    Carrera consultarCarrera(Integer idCarrera) throws NegocioException;
}
