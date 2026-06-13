/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author BALAMRUSH
 */
public class AlumnoDAO implements IAlumnoDAO{
    
    private ConexionBD conexion = new ConexionBD();
    
    @Override
    public boolean validarCredenciales(int idAlumno, String contrasena) {
        String sql = "SELECT idAlumno FROM ALUMNO WHERE idAlumno = ? AND contrasena = ?";
        
        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idAlumno);
            ps.setString(2, contrasena);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException e) {
            System.err.println("Error en AlumnoDAO.validarCredenciales: " + e.getMessage());
        }
        return false;
    }
}
