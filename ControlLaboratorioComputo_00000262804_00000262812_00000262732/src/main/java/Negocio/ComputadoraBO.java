/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dtos.ComputadoraDTO;
import Entidades.Computadora;
import Persistencia.IComputadoraDAO;
import Persistencia.PersistenciaException;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class ComputadoraBO implements IComputadoraBO {

    private static final Logger LOGGER = Logger.getLogger(ComputadoraBO.class.getName());

    private IComputadoraDAO computadoraDAO;

    public ComputadoraBO(IComputadoraDAO computadoraDAO) {
        this.computadoraDAO = computadoraDAO;
    }

    @Override
    public Computadora obtenerPCPorIP(String ip) throws NegocioException {
        try {
            validarIP(ip);

            Computadora computadora = computadoraDAO.obtenerPCPorIP(ip.trim());
            if (computadora == null) {
                throw new NegocioException("No se encontro una computadora con esa IP.");
            }

            return computadora;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al obtener computadora por IP: " + ex.getMessage());
        }
    }

    @Override
    public Computadora validarEstatusPC(String ip) throws NegocioException {
        Computadora computadora = obtenerPCPorIP(ip);
        if (!computadora.isEstatus()) {
            throw new NegocioException("La computadora no esta disponible.");
        }
        return computadora;
    }

    @Override
    public Computadora validarComputadoraDisponible(Integer idComputadora) throws NegocioException {
        try {
            validarIdComputadora(idComputadora);

            Computadora computadora = computadoraDAO.mostrarComputadoraApartada(idComputadora);
            if (computadora == null) {
                throw new NegocioException("No se encontro la computadora.");
            }

            if (!computadora.isEstatus()) {
                throw new NegocioException("La computadora no esta disponible para apartado.");
            }

            return computadora;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al validar computadora disponible: " + ex.getMessage());
        }
    }

    @Override
    public ComputadoraDTO obtenerCatalogoSoftwarePC(Integer idComputadora) throws NegocioException {
        try {
            validarIdComputadora(idComputadora);

            ComputadoraDTO computadora = computadoraDAO.obtenerCatalogoSoftwarePC(idComputadora);
            if (computadora == null) {
                throw new NegocioException("No se encontro catalogo de software para la computadora.");
            }

            return computadora;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al obtener catalogo de software: " + ex.getMessage());
        }
    }

    private void validarIP(String ip) throws NegocioException {
        if (ip == null || ip.isBlank()) {
            throw new NegocioException("La IP no puede estar vacia.");
        }
    }

    private void validarIdComputadora(Integer idComputadora) throws NegocioException {
        if (idComputadora == null || idComputadora <= 0) {
            throw new NegocioException("El ID de la computadora no es valido.");
        }
    }
}