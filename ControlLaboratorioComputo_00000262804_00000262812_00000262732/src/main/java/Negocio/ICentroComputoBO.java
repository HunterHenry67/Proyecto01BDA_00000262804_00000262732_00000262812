/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;

import Entidades.CentroComputo;
import java.util.List;

/**
 *
 * @author BALAMRUSH
 */
public interface ICentroComputoBO {
    
    CentroComputo obtenerPorID(Integer idCentroComputo) throws NegocioException;

    boolean validarHorarioServicio(Integer idCentroComputo) throws NegocioException;

    void validarCentroAbierto(Integer idCentroComputo) throws NegocioException;

    boolean validarContraseniaMaestra(String contraseniaMaestra) throws NegocioException;

    List<CentroComputo> obtenerCentrosPorUnidad(Integer idUnidadAcademica) throws NegocioException;

    List<CentroComputo> consultarCentrosDisponibles() throws NegocioException;

    CentroComputo obtenerCentroPorComputadora(Integer idComputadora) throws NegocioException;
}
