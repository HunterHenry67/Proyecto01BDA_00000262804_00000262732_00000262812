/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.CentroComputo;
import Persistencia.CentroComputoDAO;
import Persistencia.ICentroComputoDAO;
import Persistencia.IConexionBD;
import Persistencia.PersistenciaException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

public class CentroComputoBO {

    private static final Logger LOGGER = Logger.getLogger(CentroComputoBO.class.getName());
    private ICentroComputoDAO centroComputoDAO;

    public CentroComputoBO(ICentroComputoDAO centroComputoDAO) {
        this.centroComputoDAO = centroComputoDAO;
    }

    public boolean validarHorarioServicio(Integer idCentroComputo) throws NegocioException {
        try {
            CentroComputo centro = centroComputoDAO.obtenerPorID(idCentroComputo);
            if (centro == null) {
                throw new NegocioException("No se encontró el centro de cómputo.");
            }
            LocalTime ahora = LocalTime.now();
            LocalTime inicio = centro.getHoraInicio().toLocalTime();
            LocalTime fin = centro.getHOranFin().toLocalTime();
            return !ahora.isBefore(inicio) && !ahora.isAfter(fin);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al validar el horario: " + ex.getMessage());
        }
    }

    public boolean validarContraseniaMaestra(String contraseniaMaestra) throws NegocioException {
        try {
            if (contraseniaMaestra == null || contraseniaMaestra.isBlank()) {
                throw new NegocioException("La contraseña maestra no puede estar vacía.");
            }
            return centroComputoDAO.validarContraseniaMaestra(contraseniaMaestra);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al validar la contraseña maestra: " + ex.getMessage());
        }
    }

    public List<CentroComputo> obtenerCentrosPorUnidad(Integer idUnidadAcademica) throws NegocioException {
        try {
            if (idUnidadAcademica == null) {
                throw new NegocioException("El ID de unidad académica no puede ser nulo.");
            }
            return centroComputoDAO.obtenerPorUnidadAcademica(idUnidadAcademica);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al obtener los centros por unidad: " + ex.getMessage());
        }
    }

    public List<CentroComputo> consultarCentrosDisponibles() throws NegocioException {
        try {
            List<CentroComputo> todos = centroComputoDAO.obtenerTodos();
            LocalTime ahora = LocalTime.now();
            return todos.stream()
                    .filter(c -> {
                        LocalTime inicio = c.getHoraInicio().toLocalTime();
                        LocalTime fin = c.getHOranFin().toLocalTime();
                        return !ahora.isBefore(inicio) && !ahora.isAfter(fin);
                    })
                    .toList();
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al consultar centros disponibles: " + ex.getMessage());
        }
    }

    public CentroComputo obtenerCentroPorComputadora(Integer idComputadora) throws NegocioException {
        try {
            if (idComputadora == null) {
                throw new NegocioException("El ID de computadora no puede ser nulo.");
            }
            CentroComputo centro = centroComputoDAO.obtenerPorComputadora(idComputadora);
            if (centro == null) {
                throw new NegocioException("No se encontró un centro para esa computadora.");
            }
            return centro;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al obtener el centro por computadora: " + ex.getMessage());
        }
    }
}
