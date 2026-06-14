/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.Alumno;
import Persistencia.IAlumnoDAO;
import Persistencia.PersistenciaException;
import java.util.List;

/**
 *
 * @author BALAMRUSH
 */
public class AlumnoBO implements IAlumnoBO{

    private IAlumnoDAO alumnoDAO;

    public AlumnoBO(IAlumnoDAO alumnoDAO) {
        this.alumnoDAO = alumnoDAO;
    }
    
    @Override
    public Alumno consultarAlumnoPorID(Integer idAlumno) throws NegocioException {
        try {
            Alumno alumno = alumnoDAO.consultarAlumnoPorID(idAlumno);
            
            if (alumno == null) {
                throw new NegocioException("Error: No se encontró ningún alumno registrado con el ID " + idAlumno);
            }
            return alumno;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al consultar alumno por id " + e.getMessage());
        }
    }

    @Override
    public List<Alumno> consultar(String filtro) throws NegocioException {
        try {
            List<Alumno> alumnos = alumnoDAO.consultar(filtro);
            
            if (alumnos == null || alumnos.isEmpty()) {
                throw new NegocioException("No se encontraron alumnos con el filtro .");
            }
            
            return alumnos;
            
        } catch (PersistenciaException e) {
            throw new NegocioException("Error de conexion al consultar: " + e.getMessage());
        }
    }

    @Override
    public boolean estaBloqueado(Integer idAlumno) throws NegocioException {
        try {
            boolean bloqueado = alumnoDAO.estaBloqueado(idAlumno);
            return bloqueado;
            
        } catch (PersistenciaException e) {
            throw new NegocioException("Error de conexion al verificar bloqueo: " + e.getMessage());
        }
    }

    @Override
    public Alumno validarCredenciales(Integer idAlumno, String contrasena) throws NegocioException {
        if (idAlumno == null) {
            throw new NegocioException("El ID del alumno no debe estar vacío");
        } 
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new NegocioException("La contraseña del alumno no puede estar vacía");
        }
        
        try {
            Alumno alumno = alumnoDAO.consultarCredenciales(idAlumno, contrasena);
            
            if (alumno == null) {
                throw new NegocioException("id o contraseña incorrecta");
            }
            return alumno;
            
        } catch (PersistenciaException e) {
            throw new NegocioException("Error de conexión al validar credenciales " + e.getMessage());
        }
    }

    @Override
    public Alumno validarAlumnoDisponible(Integer idAlumno) throws NegocioException {
        if (idAlumno == null) {
            throw new NegocioException("El ID del alumno no puede estar vacío o ser nulo");
        }
        
        try {
            Alumno alumno = alumnoDAO.consultarAlumnoPorID(idAlumno);
            
            if (alumno == null) {
                throw new NegocioException("El alumno con ID " + idAlumno + " no existe.");
            }
            
            return alumno;
            
        } catch (PersistenciaException e) {
            throw new NegocioException("Error de conexion al validar alumno " + e.getMessage());
        }
    }
    
}
