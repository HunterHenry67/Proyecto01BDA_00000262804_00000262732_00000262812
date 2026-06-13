/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dtos.ComputadoraDTO;
import Dtos.ObtenerCatalogoSoftwareDTO;
import Entidades.Computadora;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author BALAMRUSH
 */
public class ComputadoraDAO implements IComputadoraDAO {

    private static final Logger LOGGER = Logger.getLogger(UnidadAcademicaDAO.class.getName());

    private IConexionBD conexion;

    public ComputadoraDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    /*
    @Override
    public Computadora obtenerPCPorIP(String ip) throws PersistenciaException{
        String sql = "SELECT c.*, cc.Nombre AS CentroNombre FROM COMPUTADORA c "
                   + "JOIN CENTROCOMPUTO cc ON c.IDCentroComputo = cc.IDCentroComputo "
                   + "WHERE c.DireccionIP = ?";
        
        try (Connection conn = this.conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ip);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Computadora pc = new Computadora();
                    pc.setIdComputadora(rs.getInt("IDComputadora"));
                    pc.setNumeroMaquina(rs.getInt("NumeroMaquina"));
                    pc.setDireccionIP(rs.getString("DireccionIP"));
                    pc.setEstatus(rs.getBoolean("Estatus")); 
                    pc.setTipo(rs.getString("Tipo"));
                    pc.setIdCentroComputo(rs.getInt("IDCentroComputo"));
                    
                    return pc;
                }
            }
        } catch (SQLException ex) {
            System.getLogger(ComputadoraDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
    }
     */

    @Override
    public Computadora obtenerPCPorIP(String ip) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
