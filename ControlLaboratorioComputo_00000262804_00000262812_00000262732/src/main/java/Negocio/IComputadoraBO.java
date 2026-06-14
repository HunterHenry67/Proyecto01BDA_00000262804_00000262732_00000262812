/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;

import Dtos.ComputadoraDTO;
import Entidades.Computadora;

/**
 *
 * @author BALAMRUSH
 */
public interface IComputadoraBO {
    
    Computadora obtenerPCPorIP(String ip) throws NegocioException;

    Computadora validarEstatusPC(String ip) throws NegocioException;

    Computadora validarComputadoraDisponible(Integer idComputadora) throws NegocioException;

    ComputadoraDTO obtenerCatalogoSoftwarePC(Integer idComputadora) throws NegocioException;
}
