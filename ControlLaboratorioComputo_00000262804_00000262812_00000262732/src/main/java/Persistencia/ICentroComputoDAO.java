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

    CentroComputo consultarPorID(ConsultarCentroComputoDTO ) throws PersistenciaException;
    CentroComputo consultarPorMaquina(ConsultarMaquinaCentroComputoDTO ) throws PersistenciaException;
    CentroComputo consultarPorNombre(ConsultarAlumnoCentroComputoDTO) throws PersistenciaException;
    
    
}