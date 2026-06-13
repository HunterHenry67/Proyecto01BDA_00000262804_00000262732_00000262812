/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Dtos.ObtenerCatalogoSoftwareDTO;
import Entidades.Computadora;
import java.util.List;

/**
 *
 * @author BALAMRUSH
 */
public interface IComputadoraDAO {
    Computadora obtenerPCPorIP(String ip) throws PersistenciaException;
    
}
