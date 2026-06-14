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

    private void reglasNegocioReserva(GuardarReservaDTO reserva) throws NegocioException {
        if (reserva == null) {
            throw new NegocioException("La reserva no puede estar vacía.");
        }
        if (reserva.getIdAlumno() <= 0) {
            throw new NegocioException("El ID del alumno no es válido.");
        }
        if (reserva.getIdComputadora() <= 0) {
            throw new NegocioException("El ID de la computadora no es válido.");
        }
        if (reserva.getFechaHoraApartado() == null) {
            reserva.setFechaHoraApartado(LocalDateTime.now());
        }
    }

    @Override
    public Reserva guardar(GuardarReservaDTO reserva) throws NegocioException {
        try {
            this.reglasNegocioReserva(reserva);
            Reserva reservaGuardada = this.reservaDAO.guardar(reserva);
            return reservaGuardada;
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public Reserva cancelar(CancelarReservaDTO reserva) throws NegocioException {
        try {
            if (reserva == null || reserva.getIdReserva() <= 0) {
                throw new NegocioException("El ID de la reserva no es válido.");
            }
            Reserva reservaEncontrada = this.reservaDAO.consultarReservaPorID(reserva.getIdReserva());
            if (reservaEncontrada == null) {
                throw new NegocioException("No existe la reserva.");
            }
            if (reservaEncontrada.getFechaHoraFinal() != null) {
                throw new NegocioException("La reserva ya está finalizada o cancelada.");
            }
            return this.reservaDAO.cancelar(reserva);
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public Reserva finalizar(FinalizarReservaDTO reserva) throws NegocioException {
        try {
            if (reserva == null || reserva.getIdReserva() <= 0) {
                throw new NegocioException("El ID de la reserva no es válido.");
            }
            Reserva reservaEncontrada = this.reservaDAO.consultarReservaPorID(reserva.getIdReserva());
            if (reservaEncontrada == null) {
                throw new NegocioException("No existe la reserva.");
            }
            if (reservaEncontrada.getFechaHoraFinal() != null) {
                throw new NegocioException("La reserva ya está finalizada.");
            }

            if (reserva.getFechaHoraFinal() == null) {
                reserva.setFechaHoraFinal(LocalDateTime.now());
            }
            return this.reservaDAO.finalizar(reserva);

        } catch (PersistenciaException ex) {
            throw new NegocioException("");
        }
    }

    @Override
    public Reserva consultarReservaPorID(Integer idReserva) throws NegocioException {
        try {
            if (idReserva == null || idReserva <= 0) {
                throw new NegocioException("El ID de la reserva no es válido.");
            }
            Reserva reserva = this.reservaDAO.consultarReservaPorID(idReserva);
            if (reserva == null) {
                throw new NegocioException("No se encontró la reserva.");
            }

            return reserva;

        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public List<Reserva> consultar(String filtro) throws NegocioException {
        try {
            if (filtro == null) {
                filtro = "";
            }
            return this.reservaDAO.consultar(filtro);
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public List<Reserva> consultarReservasActivas() throws NegocioException {
        try {
            return this.reservaDAO.consultarReservasActivas();
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }

    }

}
