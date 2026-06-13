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
    public Reserva obtenerReservaActiva(int idComputadora) {
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
            System.err.println("Error en ReservaDAO.obtenerReservaActiva: " + e.getMessage());
        }
        return null;
    }
}
