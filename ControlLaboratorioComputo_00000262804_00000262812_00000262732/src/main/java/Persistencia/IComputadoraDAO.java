/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Dtos.ComputadoraDTO;

/**
 *
 * @author BALAMRUSH
 */
public interface IComputadoraDAO {
    public ComputadoraDTO obtenerPCPorIP(String ip);
}
