/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Dtos.ComputadoraDTO;
import Entidades.Computadora;
import java.sql.Connection;

/**
 *
 * @author BALAMRUSH
 */
public interface IComputadoraDAO {

    Computadora obtenerPCPorIP(String ip) throws PersistenciaException;

    ComputadoraDTO obtenerCatalogoSoftwarePC(Integer idComputadora) throws PersistenciaException;

    void mostrarComputadoraComoDisponible(int idComputadora, Connection transaccion) throws PersistenciaException;

    Computadora mostrarComputadoraApartada(Integer idComputadora) throws PersistenciaException;

}
