/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

/**
 *
 * @author Andre
 */
import Entidades.Carrera;
import Persistencia.ICarreraDAO;
import Persistencia.PersistenciaException;
import java.util.logging.Logger;

public class CarreraBO implements ICarreraBO {

    private static final Logger LOGGER = Logger.getLogger(CarreraBO.class.getName());

    private ICarreraDAO carreraDAO;

    public CarreraBO(ICarreraDAO carreraDAO) {
        this.carreraDAO = carreraDAO;
    }

    @Override
    public Carrera consultarCarrera(Integer idCarrera) throws NegocioException {
        try {
            validarIdCarrera(idCarrera);

            Carrera carrera = carreraDAO.consultarCarrera(idCarrera);
            if (carrera == null) {
                throw new NegocioException("No se encontro la carrera.");
            }

            if (carrera.getTiempoDiario() == null) {
                throw new NegocioException("La carrera no tiene tiempo diario configurado.");
            }

            if (carrera.getTiempoDiario().equals(java.time.LocalTime.MIDNIGHT)) {
                throw new NegocioException("La carrera no tiene tiempo diario válido configurado.");
            }

            return carrera;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al consultar carrera: " + ex.getMessage());
        }
    }

    private void validarIdCarrera(Integer idCarrera) throws NegocioException {
        if (idCarrera == null || idCarrera <= 0) {
            throw new NegocioException("El ID de la carrera no es valido.");
        }
    }
}
