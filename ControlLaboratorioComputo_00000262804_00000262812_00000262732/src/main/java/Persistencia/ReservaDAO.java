/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dtos.ReservaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Andre
 */
public class ReservaDAO implements IReservaDAO{
    
    private final ConexionBD conexion = new ConexionBD();

    @Override
    public ReservaDTO obtenerReservaActiva(int idComputadora) {
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
                    ReservaDTO reserva = new ReservaDTO();
                    reserva.setIdReserva(rs.getInt("idReserva"));
                    reserva.setFechaHoraApartado(rs.getTimestamp("fechaHoraApartado"));
                    reserva.setIdAlumno(rs.getInt("idAlumno")); 
                    reserva.setIdComputadora(rs.getInt("idComputadora"));
                    reserva.setNombreAlumno(rs.getString("AlumnoNombre")); // Atributo auxiliar
                    return reserva;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en ReservaDAO.obtenerReservaActiva: " + e.getMessage());
        }
        return null;
    }
}
