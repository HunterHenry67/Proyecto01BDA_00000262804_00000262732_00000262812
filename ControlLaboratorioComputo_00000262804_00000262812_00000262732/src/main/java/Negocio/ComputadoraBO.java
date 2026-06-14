/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.Computadora;
import Persistencia.IComputadoraDAO;
import Persistencia.PersistenciaException;

/**
 *
 * @author user
 */
public class ComputadoraBO {
    private IComputadoraDAO computadoraDAO;
    
    public ComputadoraBO (IComputadoraDAO computadoraDAO){
        this.computadoraDAO = computadoraDAO;
    }
    
    public Computadora validarEstatusPC (String ip) throws PersistenciaException, NegocioException{
        Computadora compu = computadoraDAO.obtenerPCPorIP(ip);
        
        if(compu == null){
            throw new NegocioException("Computadora con el ip: " + ip + " no existe en el sistema");
        }
        if(compu.isEstatus() == false){
            throw new NegocioException("Computadora con el ip: " + ip + " no esta habilitada");
        }
        return compu;
    }
}
