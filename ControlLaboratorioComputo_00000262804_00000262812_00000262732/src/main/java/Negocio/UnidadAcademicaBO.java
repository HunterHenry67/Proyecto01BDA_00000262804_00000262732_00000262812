/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.UnidadAcademica;
import Persistencia.IUnidadAcademicaDAO;
import Persistencia.PersistenciaException;
import java.util.List;
import java.util.logging.Logger;

public class UnidadAcademicaBO implements IUnidadAcademicaBO {

    private static final Logger LOGGER = Logger.getLogger(UnidadAcademicaBO.class.getName());

    private IUnidadAcademicaDAO unidadAcademicaDAO;

    public UnidadAcademicaBO(IUnidadAcademicaDAO unidadAcademicaDAO) {
        this.unidadAcademicaDAO = unidadAcademicaDAO;
    }

    @Override
    public UnidadAcademica consultarUnidadAcademicaPorID(Integer idUnidadAcademica) throws NegocioException {
        try {
            validarIdUnidadAcademica(idUnidadAcademica);

            UnidadAcademica unidad = unidadAcademicaDAO.consultarUnidadAcademicaPorID(idUnidadAcademica);
            if (unidad == null) {
                throw new NegocioException("No se encontro la unidad academica.");
            }

            return unidad;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al consultar unidad academica: " + ex.getMessage());
        }
    }

    @Override
    public List<UnidadAcademica> consultarUnidadesAcademicas(String filtro) throws NegocioException {
        try {
            if (filtro == null) {
                filtro = "";
            }
            return unidadAcademicaDAO.consultarUnidadesAcademicas(filtro.trim());
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al consultar unidades academicas: " + ex.getMessage());
        }
    }

    private void validarIdUnidadAcademica(Integer idUnidadAcademica) throws NegocioException {
        if (idUnidadAcademica == null || idUnidadAcademica <= 0) {
            throw new NegocioException("El ID de la unidad academica no es valido.");
        }
    }
}
