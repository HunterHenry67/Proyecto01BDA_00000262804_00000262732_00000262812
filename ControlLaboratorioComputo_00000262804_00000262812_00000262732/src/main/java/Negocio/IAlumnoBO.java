/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;

import Entidades.Alumno;
import java.util.List;

/**
 *
 * @author BALAMRUSH
 */
public interface IAlumnoBO {
    
    Alumno consultarAlumnoPorID(Integer idAlumno) throws NegocioException;

    List<Alumno> consultar(String filtro) throws NegocioException;

    boolean estaBloqueado(Integer idAlumno) throws NegocioException;

    Alumno iniciarSesion(Integer idAlumno, String contrasena) throws NegocioException;

    Alumno validarAlumnoDisponible(Integer idAlumno) throws NegocioException;
}
