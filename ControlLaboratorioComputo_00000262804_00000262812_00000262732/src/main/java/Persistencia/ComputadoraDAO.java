/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dtos.ComputadoraDTO;
import Entidades.Computadora;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author BALAMRUSH
 */
public class ComputadoraDAO implements IComputadoraDAO{

    private IConexionBD conexion;
    
    public ComputadoraDAO(IConexionBD conexion){
        this.conexion = conexion;
    }

    @Override
    public Computadora obtenerPCPorIP(String ip) throws PersistenciaException {
        try (Connection conn = this.conexion.crearConexion()) {
            String comandoSQL = """
                                SELECT c.idComputadora, c.numeroMaquina, c.direccionIP, 
                                       c.estatus, c.tipo, c.idCentroComputo, 
                                       ua.nombre AS nombreCentro
                                FROM computadora c
                                INNER JOIN centroComputo cc ON c.idCentroComputo = cc.idCentroComputo
                                INNER JOIN unidadAcademica ua ON cc.idUnidadAcademica = ua.idUnidadAcademica
                                WHERE c.direccionIP = ?
                                """;
            
            PreparedStatement statement = conn.prepareStatement(comandoSQL);
            statement.setString(1, ip);
            ResultSet resultado = statement.executeQuery();
            
            if (resultado.next()) {
                Computadora pc = new Computadora();
                pc.setIdComputadora(resultado.getInt("idComputadora"));
                pc.setNumeroMaquina(resultado.getInt("numeroMaquina"));
                pc.setIp(resultado.getString("direccionIP"));
                pc.setEstatus(resultado.getBoolean("estatus"));
                pc.setTipo(resultado.getString("tipo"));
                pc.setIdCentroComputo(resultado.getInt("idCentroComputo"));
                
                return pc;
            }
            return null;
            
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar la computadora por IP: " + ex.getMessage());
        }
    }

        @Override
        public Computadora obtenerCatalogoSoftwarePC(Integer idComputadora) throws PersistenciaException {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

}
