/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;

import Entidades.Bloqueo;
import java.util.List;

/**
 *
 * @author BALAMRUSH
 */
public interface IBloqueoBO {
    
    Bloqueo bloquearAlumno(Integer idAlumno, String motivo) throws NegocioException;

    Bloqueo registrarBloqueo(Bloqueo bloqueo) throws NegocioException;

    void desbloquearAlumno(Integer idAlumno) throws NegocioException;

    List<Bloqueo> consultar(String filtro) throws NegocioException;

    List<Bloqueo> consultarBloqueosActivos() throws NegocioException;
    
}
