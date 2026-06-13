/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Dtos.ComputadoraDTO;
import Entidades.Computadora;

/**
 *
 * @author BALAMRUSH
 */
public interface IComputadoraDAO {
    
    public Computadora obtenerPCPorIP(String ip) throws PersistenciaException;
    
    public Computadora obtenerCatalogoSoftwarePC(Integer idComputadora) throws PersistenciaException;
    
}
