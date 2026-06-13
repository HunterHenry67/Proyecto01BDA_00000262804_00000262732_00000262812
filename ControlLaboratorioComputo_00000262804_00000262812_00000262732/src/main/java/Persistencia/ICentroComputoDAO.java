/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;
import Dtos.ConsultarIDCentroComputoDTO;
import Dtos.ConsultarMaquinaCentroComputoDTO;
import Dtos.ConsultarNombreCentroComputoDTO;
import Dtos.ConsultarCarreraCentroComputoDTO;
import Dtos.ConsultarUnidadAcademicaCentroComputoDTO;
import Dtos.ConsultarCentroComputoDTO;
import Dtos.ConsultarHoraInicioCentroComputoDTO;
import Entidades.CentroComputo;


/**
 *
 * @author Andre
 */
public interface ICentroComputoDAO {

    CentroComputo consultarPorID(ConsultarIDCentroComputoDTO id) throws PersistenciaException;
    CentroComputo consultarPorMaquina(ConsultarMaquinaCentroComputoDTO maquina) throws PersistenciaException;
    CentroComputo consultarPorNombre(ConsultarNombreCentroComputoDTO nombre) throws PersistenciaException;
    CentroComputo consultarPorCarrera(ConsultarCarreraCentroComputoDTO carrera) throws PersistenciaException;
    CentroComputo consultarPorUnidadAcademica(ConsultarUnidadAcademicaCentroComputoDTO unidadAcaademica) throws PersistenciaException;
    CentroComputo consultarPorCentroComputo(ConsultarCentroComputoDTO centroComputo) throws PersistenciaException;
    CentroComputo consultarPorHoraInicio(ConsultarHoraInicioCentroComputoDTO horaInicio) throws PersistenciaException;
    
}