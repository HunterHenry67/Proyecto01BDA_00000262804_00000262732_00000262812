/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dtos.ReservaDTO;
import Entidades.Reserva;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andre
 */
public class ReservaDAO implements IReservaDAO{
    
    private final IConexionBD conexion;
    
    public ReservaDAO (IConexion conexion){
        this.conexion = conexion;
    }

    @Override
    public Reserva registrarReserva(Reserva reserva) throws PersistenciaException {
    }

    @Override
    public Reserva consultarResrevaActivaPorAlumno(int idAlumno) throws PersistenciaException {
    }

    @Override
    public Reserva consultarReservaActivaPorComputadora(int idComputadora) throws PersistenciaException {
        String comandoSQL = """
            SELECT r.idReserva,
                   r.fechaHoraApartado,
                   r.fechaHoraInicio,
                   r.fechaHoraFinal,
                   r.tiempoUso,
                   r.idAlumno,
                   r.idComputadora,
                   CONCAT(a.nombre, ' ', a.apellidoPaterno) AS alumnoNombre
            FROM reserva r
            JOIN alumno a ON r.idAlumno = a.idAlumno
            WHERE r.idComputadora = ?
              AND r.fechaHoraFinal IS NULL
            """;
        
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {
            
            ps.setInt(1, idComputadora);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reserva reserva = new Reserva();
                    reserva.setIdReserva(rs.getInt("idReserva"));
                    reserva.setFechaHoraApartado(rs.getTimestamp("fechaHoraApartado").toLocalDateTime());
                    reserva.setIdAlumno(rs.getInt("idAlumno")); 
                    reserva.setIdComputadora(rs.getInt("idComputadora"));
                    return reserva;
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar reservas activas por computadora: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Reserva> consultar(String filtro) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Reserva> consultarReservasActivas() throws PersistenciaException {
        List<Reserva> listaReservas = new ArrayList<>();
        
        String sql = """
                     SELECT 
                     r.idReserva,
                     r.fechaHoraApartado,
                     r.fechaHoraInicio,
                     r.fechaHoraFinal,
                     r.tiempoUso,
                     r.idAlumno,
                     r.idComputadora,
                     CONCAT(a.nombre, ' ', a.apellidoPaterno, ' ', a.apellidoMaterno) AS alumnoNombre
                     FROM reserva r
                     INNER JOIN alumno a ON r.idAlumno = a.idAlumno
                     WHERE r.fechaHoraFinal IS NULL
                     """;
        
        try (Connection conn = this.conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Reserva reserva = new Reserva();
                
                reserva.setIdReserva(rs.getInt("idReserva"));
                
                reserva.setFechaHoraApartado(rs.getObject("fechaHoraApartado", java.time.LocalDateTime.class));
                reserva.setFechaHoraInicio(rs.getObject("fechaHoraInicio", java.time.LocalDateTime.class));
                reserva.setFechaHoraFinal(rs.getObject("fechaHoraFinal", java.time.LocalDateTime.class));
                reserva.setTiempoUso(rs.getInt("tiempoUso"));
                reserva.setIdAlumno(rs.getInt("idAlumno")); 
                reserva.setIdComputadora(rs.getInt("idComputadora"));
                                
                listaReservas.add(reserva);
            }
            
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar la lista de reservas activas: " + ex.getMessage());
        }
        
        return listaReservas;
    }

    @Override
    public void finalizarBloqueo(int idReserva, LocalDateTime fechaFinalizacion) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void cancelarBloqueo(int idReserva) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int consultarMinutosUsadosPorAlumno(int idAlumno) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
