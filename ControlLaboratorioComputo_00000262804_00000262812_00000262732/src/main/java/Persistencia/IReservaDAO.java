/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Dtos.CancelarReservaDTO;
import Dtos.FinalizarReservaDTO;
import Dtos.GuardarReservaDTO;
import Entidades.Reserva;
import java.util.List;

/**
 *
 * @author Andre
 */
public interface IReservaDAO {
    
    Reserva guardar(GuardarReservaDTO reserva) throws PersistenciaException;

    int registrarReserva(GuardarReservaDTO reserva) throws PersistenciaException;

    Reserva consultarResrevaActivaPorAlumno(int idAlumno) throws PersistenciaException;

    Reserva consultarReservaActivaPorComputadora(int idComputadora) throws PersistenciaException;

    List<Reserva> consultar(String filtro) throws PersistenciaException;

    List<Reserva> consultarReservasActivas() throws PersistenciaException;

    void finalizarReserva(FinalizarReservaDTO reserva) throws PersistenciaException;

    void cancelarReserva(CancelarReservaDTO reserva) throws PersistenciaException;

    int consultarMinutosUsadosPorAlumno(int idAlumno);
}
