/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dtos.CancelarReservaDTO;
import Dtos.FinalizarReservaDTO;
import Dtos.GuardarReservaDTO;
import Entidades.Reserva;
import Persistencia.IReservaDAO;
import Persistencia.PersistenciaException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author BALAMRUSH
 */

public class ReservaBO implements IReservaBO {

    private static final Logger LOGGER = Logger.getLogger(ReservaBO.class.getName());

    private IReservaDAO reservaDAO;

    public ReservaBO(IReservaDAO reservaDAO) {
        this.reservaDAO = reservaDAO;
    }

    @Override
    public Reserva guardar(GuardarReservaDTO reserva) throws NegocioException {
        try {
            reglasNegocioGuardarReserva(reserva);

            Reserva reservaActivaAlumno = reservaDAO.consultarResrevaActivaPorAlumno(reserva.getIdAlumno());
            if (reservaActivaAlumno != null) {
                throw new NegocioException("El alumno ya tiene una reserva activa.");
            }

            Reserva reservaActivaComputadora = reservaDAO.consultarReservaActivaPorComputadora(reserva.getIdComputadora());
            if (reservaActivaComputadora != null) {
                throw new NegocioException("La computadora ya tiene una reserva activa.");
            }

            return reservaDAO.guardar(reserva);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al guardar reserva: " + ex.getMessage());
        }
    }

    @Override
    public Reserva cancelar(CancelarReservaDTO reserva) throws NegocioException {
        try {
            reglasNegocioCancelarReserva(reserva);

            Reserva reservaEncontrada = reservaDAO.consultarReservaPorID(reserva.getIdReserva());
            if (reservaEncontrada == null) {
                throw new NegocioException("No existe la reserva.");
            }

            if (reservaEncontrada.getFechaHoraFinal() != null) {
                throw new NegocioException("La reserva ya esta finalizada o cancelada.");
            }

            return reservaDAO.cancelar(reserva);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al cancelar reserva: " + ex.getMessage());
        }
    }

    @Override
    public Reserva finalizar(FinalizarReservaDTO reserva) throws NegocioException {
        try {
            reglasNegocioFinalizarReserva(reserva);

            Reserva reservaEncontrada = reservaDAO.consultarReservaPorID(reserva.getIdReserva());
            if (reservaEncontrada == null) {
                throw new NegocioException("No existe la reserva.");
            }

            if (reservaEncontrada.getFechaHoraFinal() != null) {
                throw new NegocioException("La reserva ya esta finalizada.");
            }

            if (reserva.getFechaHoraFinal() == null) {
                reserva.setFechaHoraFinal(LocalDateTime.now());
            }

            return reservaDAO.finalizar(reserva);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al finalizar reserva: " + ex.getMessage());
        }
    }

    @Override
    public Reserva consultarReservaPorID(Integer idReserva) throws NegocioException {
        try {
            validarIdReserva(idReserva);

            Reserva reserva = reservaDAO.consultarReservaPorID(idReserva);
            if (reserva == null) {
                throw new NegocioException("No se encontro la reserva.");
            }

            return reserva;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al consultar reserva por ID: " + ex.getMessage());
        }
    }

    @Override
    public List<Reserva> consultar(String filtro) throws NegocioException {
        try {
            if (filtro == null) {
                filtro = "";
            }

            return reservaDAO.consultar(filtro.trim());
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al consultar reservas: " + ex.getMessage());
        }
    }

    @Override
    public List<Reserva> consultarReservasActivas() throws NegocioException {
        try {
            return reservaDAO.consultarReservasActivas();
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("Error al consultar reservas activas: " + ex.getMessage());
        }
    }

    private void reglasNegocioGuardarReserva(GuardarReservaDTO reserva) throws NegocioException {
        if (reserva == null) {
            throw new NegocioException("La reserva no puede estar vacia.");
        }
        if (reserva.getIdAlumno() == null || reserva.getIdAlumno() <= 0) {
            throw new NegocioException("El ID del alumno no es valido.");
        }
        if (reserva.getIdComputadora() == null || reserva.getIdComputadora() <= 0) {
            throw new NegocioException("El ID de la computadora no es valido.");
        }
        if (reserva.getFechaHoraApartado() == null) {
            reserva.setFechaHoraApartado(LocalDateTime.now());
        }
        if (reserva.getFechaHoraInicio() != null && reserva.getFechaHoraInicio().isBefore(reserva.getFechaHoraApartado())) {
            throw new NegocioException("La fecha de inicio no puede ser anterior a la fecha de apartado.");
        }
        if (reserva.getFechaHoraFinal() != null) {
            throw new NegocioException("Una reserva nueva no debe tener fecha final.");
        }
        if (reserva.getTiempoUso() != null && reserva.getTiempoUso() < 0) {
            throw new NegocioException("El tiempo de uso no puede ser negativo.");
        }
    }

    private void reglasNegocioCancelarReserva(CancelarReservaDTO reserva) throws NegocioException {
        if (reserva == null) {
            throw new NegocioException("La solicitud de cancelacion no puede estar vacia.");
        }
        validarIdReserva(reserva.getIdReserva());
    }

    private void reglasNegocioFinalizarReserva(FinalizarReservaDTO reserva) throws NegocioException {
        if (reserva == null) {
            throw new NegocioException("La solicitud de finalizacion no puede estar vacia.");
        }
        validarIdReserva(reserva.getIdReserva());
    }

    private void validarIdReserva(Integer idReserva) throws NegocioException {
        if (idReserva == null || idReserva <= 0) {
            throw new NegocioException("El ID de la reserva no es valido.");
        }
    }
}
