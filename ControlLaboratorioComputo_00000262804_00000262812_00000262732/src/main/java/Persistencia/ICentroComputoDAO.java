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

    CentroComputo obtenerPorID(Integer idCentroComputo) throws PersistenciaException;
    CentroComputo obtenerPorUnidadAcademica(Integer idUnidadAcademica) throws PersistenciaException;
    CentroComputo validarContraseniaMaestra(String contraseniaMaestria) throws PersistenciaException;
    List<CentroComputo>
    
}