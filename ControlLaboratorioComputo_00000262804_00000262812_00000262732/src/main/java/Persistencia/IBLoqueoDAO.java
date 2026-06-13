/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Entidades.Bloqueo;
import java.util.List;

/**
 *
 * @author Andre
 */
public interface IBloqueoDAO {

    Bloqueo registrarBloqueo(Bloqueo bloqueo) throws PersistenciaException;

    void desbloquearAlumno(int idAlumno) throws PersistenciaException;

    List<Bloqueo> consultar(String filtro) throws PersistenciaException;

    List<Bloqueo> consultarBloqueosActivos() throws PersistenciaException;

}
