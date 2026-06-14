/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;

import Dtos.ObtenerCatalogoSoftwareDTO;
import Entidades.Software;
import java.util.List;

/**
 *
 * @author BALAMRUSH
 */
public interface ISoftwareBO {
    
    List<Software> obtenerCatalogoSoftware(ObtenerCatalogoSoftwareDTO dto) throws NegocioException;

    List<Software> obtenerCatalogoSoftwarePorComputadora(Integer idComputadora) throws NegocioException;
}
