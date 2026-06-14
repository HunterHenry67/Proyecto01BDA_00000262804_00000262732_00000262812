/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;

import Dtos.CancelarReservaDTO;
import Dtos.FinalizarReservaDTO;
import Dtos.GuardarReservaDTO;
import Entidades.Reserva;
import java.util.List;

/**
 *
 * @author BALAMRUSH
 */
public interface IReservaBO {
    
    Reserva apartarComputadora(Integer idAlumno, Integer idComputadora) throws NegocioException;

    Reserva guardar(GuardarReservaDTO reservaDTO) throws NegocioException;

    Reserva cancelar(CancelarReservaDTO cancelarReservaDTO) throws NegocioException;

    Reserva finalizar(FinalizarReservaDTO finalizarReservaDTO) throws NegocioException;

    Reserva finalizar(Integer idReserva) throws NegocioException;

    Reserva consultarReservaPorID(Integer idReserva) throws NegocioException;

    Reserva consultarReservaActivaPorAlumno(Integer idAlumno) throws NegocioException;

    Reserva consultarReservaActivaPorComputadora(Integer idComputadora) throws NegocioException;

    List<Reserva> consultar(String filtro) throws NegocioException;

    List<Reserva> consultarReservasActivas() throws NegocioException;

    int consultarMinutosUsadosPorAlumno(Integer idAlumno) throws NegocioException;
}
