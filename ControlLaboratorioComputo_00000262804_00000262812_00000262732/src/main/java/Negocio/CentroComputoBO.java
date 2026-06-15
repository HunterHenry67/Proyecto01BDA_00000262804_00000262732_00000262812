/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.CentroComputo;
import Persistencia.ICentroComputoDAO;
import Persistencia.PersistenciaException;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

public class CentroComputoBO implements ICentroComputoBO {

    private static final Logger LOGGER = Logger.getLogger(CentroComputoBO.class.getName());

    private ICentroComputoDAO centroComputoDAO;

    public CentroComputoBO(ICentroComputoDAO centroComputoDAO) {
        this.centroComputoDAO = centroComputoDAO;
    }

    @Override
    public CentroComputo obtenerPorID(Integer idCentroComputo) throws NegocioException {
        try {
            validarIdCentroComputo(idCentroComputo);

            CentroComputo centro = centroComputoDAO.obtenerPorID(idCentroComputo);
            if (centro == null) {
                throw new NegocioException("No se encontro el centro de computo.");
            }

            return centro;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al obtener centro de computo: " + ex.getMessage());
        }
    }

    @Override
    public boolean validarHorarioServicio(Integer idCentroComputo) throws NegocioException {
        CentroComputo centro = obtenerPorID(idCentroComputo);
        validarHorarioConfigurado(centro);

        LocalTime ahora = LocalTime.now();
        LocalTime inicio = centro.getHoraInicio().toLocalTime();
        LocalTime fin = centro.getHOranFin().toLocalTime();

        if (inicio.equals(fin)) {
            return true;
        }

        if (inicio.isBefore(fin)) {
            return !ahora.isBefore(inicio) && !ahora.isAfter(fin);
        }

        return !ahora.isBefore(inicio) || !ahora.isAfter(fin);
    }

    @Override
    public void validarCentroAbierto(Integer idCentroComputo) throws NegocioException {
        if (!validarHorarioServicio(idCentroComputo)) {
            throw new NegocioException("El centro de computo esta fuera de horario de servicio.");
        }
    }

    @Override
    public boolean validarContraseniaMaestra(String contraseniaMaestra) throws NegocioException {
        try {
            if (contraseniaMaestra == null || contraseniaMaestra.isBlank()) {
                throw new NegocioException("La contrasena maestra no puede estar vacia.");
            }

            return centroComputoDAO.validarContraseniaMaestra(contraseniaMaestra);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al validar la contrasena maestra: " + ex.getMessage());
        }
    }

    @Override
    public List<CentroComputo> obtenerCentrosPorUnidad(Integer idUnidadAcademica) throws NegocioException {
        try {
            validarIdUnidadAcademica(idUnidadAcademica);
            return centroComputoDAO.obtenerPorUnidadAcademica(idUnidadAcademica);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al obtener centros por unidad academica: " + ex.getMessage());
        }
    }

    @Override
    public List<CentroComputo> consultarCentrosDisponibles() throws NegocioException {
        try {
            List<CentroComputo> centros = centroComputoDAO.obtenerTodos();
            LocalTime ahora = LocalTime.now();

            return centros.stream()
                    .filter(centro -> centro.getHoraInicio() != null && centro.getHOranFin() != null)
                    .filter(centro -> estaDentroDelHorario(ahora, centro.getHoraInicio().toLocalTime(), centro.getHOranFin().toLocalTime()))
                    .toList();
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al consultar centros disponibles: " + ex.getMessage());
        }
    }

    @Override
    public CentroComputo obtenerCentroPorComputadora(Integer idComputadora) throws NegocioException {
        try {
            validarIdComputadora(idComputadora);

            CentroComputo centro = centroComputoDAO.obtenerPorComputadora(idComputadora);
            if (centro == null) {
                throw new NegocioException("No se encontro un centro de computo para esa computadora.");
            }

            return centro;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al obtener centro por computadora: " + ex.getMessage());
        }
    }

    private void validarIdCentroComputo(Integer idCentroComputo) throws NegocioException {
        if (idCentroComputo == null || idCentroComputo <= 0) {
            throw new NegocioException("El ID del centro de computo no es valido.");
        }
    }

    private void validarIdUnidadAcademica(Integer idUnidadAcademica) throws NegocioException {
        if (idUnidadAcademica == null || idUnidadAcademica <= 0) {
            throw new NegocioException("El ID de la unidad academica no es valido.");
        }
    }

    private void validarIdComputadora(Integer idComputadora) throws NegocioException {
        if (idComputadora == null || idComputadora <= 0) {
            throw new NegocioException("El ID de la computadora no es valido.");
        }
    }

    private void validarHorarioConfigurado(CentroComputo centro) throws NegocioException {
        if (centro.getHoraInicio() == null || centro.getHOranFin() == null) {
            throw new NegocioException("El centro de computo no tiene horario configurado.");
        }
    }

    private boolean estaDentroDelHorario(LocalTime hora, LocalTime inicio, LocalTime fin) {
        if (inicio.equals(fin)) {
            return true;
        }
        if (inicio.isBefore(fin)) {
            return !hora.isBefore(inicio) && !hora.isAfter(fin);
        }
        return !hora.isBefore(inicio) || !hora.isAfter(fin);
    }
}
