/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Entidades.Bloqueo;
import Persistencia.IBloqueoDAO;
import Persistencia.PersistenciaException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

public class BloqueoBO implements IBloqueoBO {

    private static final Logger LOGGER = Logger.getLogger(BloqueoBO.class.getName());

    private IBloqueoDAO bloqueoDAO;

    public BloqueoBO(IBloqueoDAO bloqueoDAO) {
        this.bloqueoDAO = bloqueoDAO;
    }

    @Override
    public Bloqueo bloquearAlumno(Integer idAlumno, String motivo) throws NegocioException {
        validarIdAlumno(idAlumno);
        validarMotivo(motivo);

        Bloqueo bloqueo = new Bloqueo();
        bloqueo.setIdAlumno(idAlumno);
        bloqueo.setMotivo(motivo.trim());
        bloqueo.setFechaHoraIncioBloqueo(LocalDateTime.now());
        bloqueo.setFechaHoraFinalBloqueo(null);

        return registrarBloqueo(bloqueo);
    }

    @Override
    public Bloqueo registrarBloqueo(Bloqueo bloqueo) throws NegocioException {
        try {
            reglasNegocioRegistrarBloqueo(bloqueo);

            if (alumnoYaTieneBloqueoActivo(bloqueo.getIdAlumno())) {
                throw new NegocioException("El alumno ya tiene un bloqueo activo.");
            }

            return bloqueoDAO.registrarBloqueo(bloqueo);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al registrar bloqueo: " + ex.getMessage());
        }
    }

    @Override
    public void desbloquearAlumno(Integer idAlumno) throws NegocioException {
        try {
            validarIdAlumno(idAlumno);
            bloqueoDAO.desbloquearAlumno(idAlumno);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al desbloquear alumno: " + ex.getMessage());
        }
    }

    @Override
    public List<Bloqueo> consultar(String filtro) throws NegocioException {
        try {
            if (filtro == null) {
                filtro = "";
            }
            return bloqueoDAO.consultar(filtro.trim());
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al consultar bloqueos: " + ex.getMessage());
        }
    }

    @Override
    public List<Bloqueo> consultarBloqueosActivos() throws NegocioException {
        try {
            return bloqueoDAO.consultarBloqueosActivos();
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al consultar bloqueos activos: " + ex.getMessage());
        }
    }

    private void reglasNegocioRegistrarBloqueo(Bloqueo bloqueo) throws NegocioException {
        if (bloqueo == null) {
            throw new NegocioException("El bloqueo no puede estar vacio.");
        }
        validarIdAlumno(bloqueo.getIdAlumno());
        validarMotivo(bloqueo.getMotivo());
        if (bloqueo.getFechaHoraIncioBloqueo() == null) {
            bloqueo.setFechaHoraIncioBloqueo(LocalDateTime.now());
        }
        if (bloqueo.getFechaHoraFinalBloqueo() != null
                && bloqueo.getFechaHoraFinalBloqueo().isBefore(bloqueo.getFechaHoraIncioBloqueo())) {
            throw new NegocioException("La fecha final del bloqueo no puede ser anterior a la fecha inicial.");
        }
    }

    private boolean alumnoYaTieneBloqueoActivo(Integer idAlumno) throws PersistenciaException {
        List<Bloqueo> bloqueosActivos = bloqueoDAO.consultarBloqueosActivos();
        for (Bloqueo bloqueo : bloqueosActivos) {
            if (bloqueo.getIdAlumno() == idAlumno) {
                return true;
            }
        }
        return false;
    }

    private void validarIdAlumno(Integer idAlumno) throws NegocioException {
        if (idAlumno == null || idAlumno <= 0) {
            throw new NegocioException("El ID del alumno no es valido.");
        }
    }

    private void validarMotivo(String motivo) throws NegocioException {
        if (motivo == null || motivo.isBlank()) {
            throw new NegocioException("El motivo del bloqueo no puede estar vacio.");
        }
    }
}
