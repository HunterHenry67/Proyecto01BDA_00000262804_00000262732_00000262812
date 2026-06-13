/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;
import Entidades.CentroComputo;


/**
 *
 * @author Andre
 */
public interface ICentroComputoDAO {

    CentroComputo consultarPorID(ConsultarCentroComputoDTO centro) throws PersistenciaException;
    CentroComputo consultarPorMaquina(ConsultarMaquinaCentroComputoDTO maquina) throws PersistenciaException;
    CentroComputo consultarPorNombre(ConsultarAlumnoCentroComputoDTO nombre) throws PersistenciaException;
    CentroComputo consultarPorCarrera(ConsultarCarreraCentroComputoDTO carrera) throws PersistenciaException;
    CentroComputo consultarPorUnidadAcademica(ConsultarUnidadAcademicaDTO unidadAcaademica) throws PersistenciaException;
    CentroComputo consultarPorCentroComputo(ConsultarCentroComputoDTO centroComputo) throws PersistenciaException;
    CentroComputo consultarPorHoraApartado(ConsultarHoraApartadoDTO horaApartado) throws PersistenciaException;
    
    
    
}