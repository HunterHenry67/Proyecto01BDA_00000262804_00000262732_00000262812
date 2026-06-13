/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Entidades.Alumno;
import java.util.List;

/**
 *
 * @author BALAMRUSH
 */
public interface IAlumnoDAO {

    List<Alumno> consultar(String filtro) throws PersistenciaException;

    Alumno consultarAlumnoPorID(int idAlumno) throws PersistenciaException;

    boolean estaBloqueado(int idAlumno) throws PersistenciaException;

    boolean validarCredenciales(int idAlumno, String contrasena) throws PersistenciaException;
}
