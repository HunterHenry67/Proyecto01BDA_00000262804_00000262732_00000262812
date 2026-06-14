/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dtos.ObtenerCatalogoSoftwareDTO;
import Entidades.Software;
import Persistencia.ISoftwareDAO;
import Persistencia.PersistenciaException;
import java.util.List;
import java.util.logging.Logger;

public class SoftwareBO implements ISoftwareBO {

    private static final Logger LOGGER = Logger.getLogger(SoftwareBO.class.getName());

    private ISoftwareDAO softwareDAO;

    public SoftwareBO(ISoftwareDAO softwareDAO) {
        this.softwareDAO = softwareDAO;
    }

    @Override
    public List<Software> obtenerCatalogoSoftware(ObtenerCatalogoSoftwareDTO dto) throws NegocioException {
        try {
            reglasNegocioObtenerCatalogoSoftware(dto);
            return softwareDAO.obtenerCatalogoSoftware(dto);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al obtener catalogo de software: " + ex.getMessage());
        }
    }

    @Override
    public List<Software> obtenerCatalogoSoftwarePorComputadora(Integer idComputadora) throws NegocioException {
        ObtenerCatalogoSoftwareDTO dto = new ObtenerCatalogoSoftwareDTO(idComputadora);
        return obtenerCatalogoSoftware(dto);
    }

    private void reglasNegocioObtenerCatalogoSoftware(ObtenerCatalogoSoftwareDTO dto) throws NegocioException {
        if (dto == null) {
            throw new NegocioException("La solicitud de catalogo de software no puede estar vacia.");
        }
        if (dto.getIdComputadora() == null || dto.getIdComputadora() <= 0) {
            throw new NegocioException("El ID de la computadora no es valido.");
        }
    }
}
