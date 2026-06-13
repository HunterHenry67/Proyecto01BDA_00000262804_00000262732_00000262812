/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Entidades.Reserva;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Andre
 */
public interface IReservaDAO {
    Reserva registrarReserva(Reserva reserva) throws PersistenciaException;
    Reserva consultarResrevaActivaPorAlumno(int idAlumno) throws PersistenciaException;
    Reserva consultarReservaActivaPorComputadora(int idComputadora) throws PersistenciaException;
    List<Reserva> consultar(String filtro) throws PersistenciaException;
    List<Reserva> consultarReservasActivas() throws PersistenciaException;
    void finalizarReserva(int idReserva, LocalDateTime fechaFinalizacion) throws PersistenciaException;
    void cancelarReserva(int idReserva) throws PersistenciaException;
    int consultarMinutosUsadosPorAlumno(int idAlumno); 
}
