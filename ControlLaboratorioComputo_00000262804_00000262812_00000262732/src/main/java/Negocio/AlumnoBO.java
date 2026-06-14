/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.Alumno;
import Persistencia.IAlumnoDAO;
import Persistencia.PersistenciaException;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author BALAMRUSH
 */
public class AlumnoBO implements IAlumnoBO {

    private static final Logger LOGGER = Logger.getLogger(AlumnoBO.class.getName());

    private IAlumnoDAO alumnoDAO;

    public AlumnoBO(IAlumnoDAO alumnoDAO) {
        this.alumnoDAO = alumnoDAO;
    }

    @Override
    public Alumno consultarAlumnoPorID(Integer idAlumno) throws NegocioException {
        try {
            validarIdAlumno(idAlumno);

            Alumno alumno = alumnoDAO.consultarAlumnoPorID(idAlumno);
            if (alumno == null) {
                throw new NegocioException("No se encontro el alumno.");
            }

            return alumno;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al consultar alumno por ID: " + ex.getMessage());
        }
    }

    @Override
    public List<Alumno> consultar(String filtro) throws NegocioException {
        try {
            if (filtro == null) {
                filtro = "";
            }

            return alumnoDAO.consultar(filtro.trim());
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al consultar alumnos: " + ex.getMessage());
        }
    }

    @Override
    public boolean estaBloqueado(Integer idAlumno) throws NegocioException {
        try {
            validarIdAlumno(idAlumno);
            return alumnoDAO.estaBloqueado(idAlumno);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al verificar bloqueo del alumno: " + ex.getMessage());
        }
    }

    @Override
    public Alumno validarCredenciales(Integer idAlumno, String contrasena) throws NegocioException {
        try {
            validarIdAlumno(idAlumno);
            validarContrasena(contrasena);

            Alumno alumno = alumnoDAO.consultarCredenciales(idAlumno, contrasena);
            if (alumno == null) {
                throw new NegocioException("ID o contrasena incorrecta.");
            }

            if (!alumno.isEstatus()) {
                throw new NegocioException("El alumno no esta inscrito o activo.");
            }

            if (alumnoDAO.estaBloqueado(idAlumno)) {
                throw new NegocioException("El alumno esta bloqueado y no puede usar computadoras.");
            }

            return alumno;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al validar credenciales: " + ex.getMessage());
        }
    }

    @Override
    public Alumno validarAlumnoDisponible(Integer idAlumno) throws NegocioException {
        try {
            validarIdAlumno(idAlumno);

            Alumno alumno = alumnoDAO.consultarAlumnoPorID(idAlumno);
            if (alumno == null) {
                throw new NegocioException("El alumno no existe.");
            }

            if (!alumno.isEstatus()) {
                throw new NegocioException("El alumno no esta inscrito o activo.");
            }

            if (alumnoDAO.estaBloqueado(idAlumno)) {
                throw new NegocioException("El alumno esta bloqueado y no puede usar computadoras.");
            }

            return alumno;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al validar alumno disponible: " + ex.getMessage());
        }
    }

    private void validarIdAlumno(Integer idAlumno) throws NegocioException {
        if (idAlumno == null || idAlumno <= 0) {
            throw new NegocioException("El ID del alumno no es valido.");
        }
    }

    private void validarContrasena(String contrasena) throws NegocioException {
        if (contrasena == null || contrasena.isBlank()) {
            throw new NegocioException("La contrasena del alumno no puede estar vacia.");
        }
    }
}
