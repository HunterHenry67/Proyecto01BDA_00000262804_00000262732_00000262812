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
    
    Reserva guardar(GuardarReservaDTO reserva) throws NegocioException;

    Reserva cancelar(CancelarReservaDTO reserva) throws NegocioException;

    Reserva finalizar(FinalizarReservaDTO reserva) throws NegocioException;

    Reserva consultarReservaPorID(Integer idReserva) throws NegocioException;

    List<Reserva> consultar(String filtro) throws NegocioException;

    List<Reserva> consultarReservasActivas() throws NegocioException;
    
    Reserva consultarReservaActivaPorComputadora(Integer idComputadora) throws NegocioException;
}
