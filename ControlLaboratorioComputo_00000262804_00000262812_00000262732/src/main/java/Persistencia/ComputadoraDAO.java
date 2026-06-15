/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dtos.ComputadoraDTO;
import Entidades.Computadora;
import Entidades.Software;
import Persistencia.PersistenciaException;
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

    private static final Logger LOGGER = Logger.getLogger(ComputadoraDAO.class.getName());

    private Connection transaccion;

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
                     WHERE direccionIP = ?
                     """;

        try (Connection conn = this.conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ip);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Computadora pc = new Computadora();
                    pc.setIdComputadora(rs.getInt("idComputadora"));
                    pc.setNumeroMaquina(rs.getInt("numeroMaquina"));
                    pc.setIp(rs.getString("direccionIP"));
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
    public Computadora mostrarComputadoraApartada(Integer idComputadora) throws PersistenciaException {
        String sql = """
                     SELECT
                     idComputadora,
                     numeroMaquina,
                     direccionIP,
                     estatus,
                     tipo,
                     idCentroComputo
                     FROM Computadora
                     WHERE idComputadora = ? AND estatus = true
                     """;
        try (Connection conn = this.conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idComputadora);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Computadora pc = new Computadora();
                    pc.setIdComputadora(rs.getInt("idComputadora"));
                    pc.setNumeroMaquina(rs.getInt("numeroMaquina"));
                    pc.setIp(rs.getString("direccionIP"));
                    pc.setEstatus(rs.getBoolean("estatus"));
                    pc.setTipo(rs.getString("tipo"));
                    pc.setIdCentroComputo(rs.getInt("idCentroComputo"));

                    return pc;
                }
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error en ComputadoraDao al obtener status de pc por id");
        }
        return null;

    }

    @Override
    public void mostrarComputadoraComoDisponible(int idComputadora, Connection transaccion) throws PersistenciaException {
        String comandoSQL = """
                        UPDATE computadora
                        SET estatus = true
                        WHERE idComputadora = ?;
                        """;
        try (PreparedStatement statement = transaccion.prepareStatement(comandoSQL)) {
            statement.setInt(1, idComputadora);
            int filasCambiadas = statement.executeUpdate();
            if (filasCambiadas == 0) {
                throw new PersistenciaException("No fue posible mostrar la computadora como disponible.");
            }
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al mostrar computadora disponible: " + ex.getMessage());
        }
    }

    @Override
    public ComputadoraDTO obtenerCatalogoSoftwarePC(Integer idComputadora) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                            SELECT c.idComputadora, c.numeroMaquina, c.direccionIP, c.estatus, c.tipo, c.idCentroComputo,
                                   s.idSoftware, s.nombre
                            FROM computadora c
                            INNER JOIN computadora_software cs ON cs.idComputadora = c.idComputadora
                            INNER JOIN software s ON s.idSoftware = cs.idSoftware
                            WHERE c.idComputadora = ?
                            """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            statement.setInt(1, idComputadora);
            ResultSet resultado = statement.executeQuery();

            ComputadoraDTO computadora = null;
            List<Software> catalogo = new ArrayList<>();

            while (resultado.next()) {
                if (computadora == null) {
                    computadora = new ComputadoraDTO();
                    computadora.setIdComputadora(resultado.getInt("idComputadora"));
                    computadora.setNumeroMaquina(resultado.getInt("numeroMaquina"));
                    computadora.setDireccionIP(resultado.getString("direccionIP"));
                    computadora.setEstatus(resultado.getBoolean("estatus"));
                    computadora.setTipo(resultado.getString("tipo"));
                    computadora.setIdCentroComputo(resultado.getInt("idCentroComputo"));
                }
                Software software = new Software();
                software.setIdSoftware(resultado.getInt("idSoftware"));
                software.setNombre(resultado.getString("nombre"));
                catalogo.add(software);
            }

            if (computadora != null) {
                computadora.setCatalogoSoftware(catalogo);
            }
            return computadora;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al obtener el catálogo de software: " + ex.getMessage());
        }
    }

    @Override
    public List<Computadora> consultarComputadoras() throws PersistenciaException {
        String sql = """
                    SELECT idComputadora, 
                            numeroMaquina, 
                            direccionIP, 
                            estatus, 
                            tipo, 
                            idCentroComputo
                    FROM computadora
                    ORDER BY numeroMaquina;
                """;
        List<Computadora> computadoras = new ArrayList<>();
        try (Connection conn = this.conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Computadora pc = new Computadora();
                pc.setIdComputadora(rs.getInt("idComputadora"));
                pc.setNumeroMaquina(rs.getInt("numeroMaquina"));
                pc.setIp(rs.getString("direccionIP"));
                pc.setEstatus(rs.getBoolean("estatus"));
                pc.setTipo(rs.getString("tipo"));
                pc.setIdCentroComputo(rs.getInt("idCentroComputo"));
                computadoras.add(pc);
            }
            return computadoras;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar computadoras: " + ex.getMessage());
        }
    }

}
