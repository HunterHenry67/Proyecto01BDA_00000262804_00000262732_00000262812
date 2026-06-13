/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Entidades.UnidadAcademica;
import java.util.List;

/**
 *
 * @author Andre
 */
public interface IUnidadAcademicaDAO {

    UnidadAcademica consultarUnidadAcademicaPorID(Integer idUndadAcademica) throws PersistenciaException;

    List<UnidadAcademica> consultarUnidadesAcademicas() throws PersistenciaException;
}
