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

    List<CentroComputo> obtenerTodos() throws PersistenciaException;

    List<CentroComputo> obtenerPorUnidadAcademica(Integer idUnidadAcademica) throws PersistenciaException;

    CentroComputo obtenerPorComputadora(Integer idComputadora) throws PersistenciaException;

    boolean validarContraseniaMaestra(String contraseniaMaestra) throws PersistenciaException;
}