/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;

import Entidades.UnidadAcademica;
import java.util.List;

/**
 *
 * @author BALAMRUSH
 */
public interface IUnidadAcademicaBO {
    
    UnidadAcademica consultarUnidadAcademicaPorID(Integer idUnidadAcademica) throws NegocioException;

    List<UnidadAcademica> consultarUnidadesAcademicas(String filtro) throws NegocioException;
}
