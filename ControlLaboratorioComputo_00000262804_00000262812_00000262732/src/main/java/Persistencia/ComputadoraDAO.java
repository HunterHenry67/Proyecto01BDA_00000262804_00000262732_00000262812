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
import java.util.logging.Logger;

/**
 *
 * @author BALAMRUSH
 */
public class ComputadoraDAO implements IComputadoraDAO {

    private static final Logger LOGGER = Logger.getLogger(ComputadoraDAO.class.getName());
    

    private IConexionBD conexion;

    public ComputadoraDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public Computadora obtenerPCPorIP(String ip) throws PersistenciaException {
        String sql = """
                     SELECT
                     idComputadora,
                     numeroMaquina,
                     direccionIP,
                     estatus,
                     tipo,
                     idCentroComputo
                     FROM Computadora
                     WHERE direccionIp = ?
                     """;

        try (Connection conn = this.conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ip);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Computadora pc = new Computadora();
                    pc.setIdComputadora(rs.getInt("idComputadora"));
                    pc.setNumeroMaquina(rs.getInt("numeroMaquina"));
                    pc.setIp(rs.getString("direccionIp"));
                    pc.setEstatus(rs.getBoolean("estatus"));
                    pc.setTipo(rs.getString("tipo"));
                    pc.setIdCentroComputo(rs.getInt("idCentroComputo"));

                    return pc;
                }
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error en ComputadoraDao al obtener pc por ip");
        }
        return null;
    }

    @Override
    public Computadora obtenerCatalogoSoftwarePC(Integer idComputadora) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mostrarComputadoraComoDisponible(int idComputadora) throws PersistenciaException {
        try(Connection conexion = this.conexion.crearConexion()){
            String comandoSQL = """
                                UPDATE computadora
                                    SET estatus = 'APARTADA'
                                    WHERE idComputadora = ?;
                                """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            statement.setInt(1, idComputadora);
            int filasCambiadas = statement.executeUpdate();
            
            if(filasCambiadas == 0){
                throw new PersistenciaException("No fue posible mostrar la computadora como apartada.");
            }
        }catch(SQLException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al mostrar computadora disponible: "+ex.getMessage());
            
        }
    }
}
