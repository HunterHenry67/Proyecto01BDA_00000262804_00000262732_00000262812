/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.UnidadAcademica;
import Negocio.IUnidadAcademicaBO;
import Negocio.NegocioException;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author BALAMRUSH
 */
public class UnidadAcademicaBO implements IUnidadAcademicaBO{

    private static final Logger LOGGER = Logger.getLogger(UnidadAcademicaBO.class.getName());
    
    private IUnidadAcademicaDAO unidadAcademicaDAO;
    
    public UnidadAcademicaBO(IUnidadAcademicaDAO unidadAcademicaDAO){
        this.unidadAcademicaDAO = unidadAcademicaDAO;
    }

    @Override
    public UnidadAcademica consultarUnidadAcademicaPorID(Integer idUnidadAcademica) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<UnidadAcademica> consultarUnidadesAcademicas(String filtro) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
