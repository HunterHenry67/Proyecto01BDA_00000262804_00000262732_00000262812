/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dtos.ComputadoraDTO;
import Entidades.Computadora;
import Persistencia.IComputadoraDAO;
import Persistencia.PersistenciaException;
import java.util.List;

public class ComputadoraBO implements IComputadoraBO {

    private final IComputadoraDAO computadoraDAO;

    public ComputadoraBO(IComputadoraDAO computadoraDAO) {
        this.computadoraDAO = computadoraDAO;
    }

    @Override
    public Computadora obtenerPCPorIP(String ip) throws NegocioException {
        try {
            if (ip == null || ip.isBlank()) {
                throw new NegocioException("La IP no puede estar vacía.");
            }

            return computadoraDAO.obtenerPCPorIP(ip);

        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public Computadora validarEstatusPC(String ip) throws NegocioException {
        Computadora computadora = obtenerPCPorIP(ip);

        if (computadora == null) {
            throw new NegocioException("No se encontró una computadora con esa IP.");
        }

        if (!computadora.isEstatus()) {
            throw new NegocioException("La computadora no está disponible.");
        }

        return computadora;
    }

    @Override
    public Computadora validarComputadoraDisponible(Integer idComputadora) throws NegocioException {
        try {
            if (idComputadora == null || idComputadora <= 0) {
                throw new NegocioException("La computadora no es válida.");
            }

            Computadora computadora = computadoraDAO.mostrarComputadoraApartada(idComputadora);

            if (computadora == null) {
                throw new NegocioException("La computadora no está disponible.");
            }

            return computadora;

        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public ComputadoraDTO obtenerCatalogoSoftwarePC(Integer idComputadora) throws NegocioException {
        try {
            if (idComputadora == null || idComputadora <= 0) {
                throw new NegocioException("La computadora no es válida.");
            }

            return computadoraDAO.obtenerCatalogoSoftwarePC(idComputadora);

        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public Computadora obtenerComputadoraPorNumero(int numeroMaquina) throws NegocioException {
        try {
            if (numeroMaquina <= 0) {
                throw new NegocioException("El número de máquina no es válido.");
            }

            return computadoraDAO.obtenerComputadoraPorNumero(numeroMaquina);

        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public void actualizarEstatus(int idComputadora, boolean estatus) throws NegocioException {
        try {
            if (idComputadora <= 0) {
                throw new NegocioException("La computadora no es válida.");
            }

            computadoraDAO.actualizarEstatus(idComputadora, estatus);

        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public List<Computadora> obtenerMonitoreoEquipos(String busqueda, String filtro) throws NegocioException {
        try {
            return computadoraDAO.obtenerMonitoreoEquipos(busqueda, filtro);
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public int contarMonitoreoEquipos(String busqueda, String filtro) throws NegocioException {
        try {
            return computadoraDAO.contarMonitoreoEquipos(busqueda, filtro);

        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }
}