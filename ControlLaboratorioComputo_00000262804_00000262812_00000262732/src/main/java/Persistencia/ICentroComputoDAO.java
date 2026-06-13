/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;
import Entidades.CentroComputo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


/**
 *
 * @author Andre
 */
public interface ICentroComputoDAO {

    CentroComputo consultarPorIDAlumno(Integer idAlumno) throws PersistenciaException;
    
    CentroComputo consultarPorMaquina(Integer maquina) throws PersistenciaException;
    
    List<CentroComputo> consultarPorNombreAlumno(String nombre) throws PersistenciaException;
    
    List<CentroComputo> consultarPorCarrera(String carrera) throws PersistenciaException;
    
    List<CentroComputo> consultarPorUnidadAcademica(String unidadAcademica) throws PersistenciaException; 
    
    List<CentroComputo> consultarPorCentroComputo(String centroComputo) throws PersistenciaException;
    
    List<CentroComputo> consultarPorHoraInicio(LocalDateTime horaInicio) throws PersistenciaException;
    
}