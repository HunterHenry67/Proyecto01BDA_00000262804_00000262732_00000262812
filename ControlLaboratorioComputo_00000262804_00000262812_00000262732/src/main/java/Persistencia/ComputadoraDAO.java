/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dtos.ComputadoraDTO;
import Entidades.Computadora;
import Entidades.Software;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ComputadoraDAO implements IComputadoraDAO {

    private static final Logger LOGGER = Logger.getLogger(ComputadoraDAO.class.getName());

    private final IConexionBD conexion;

    public ComputadoraDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public Computadora obtenerPCPorIP(String ip) throws PersistenciaException {
        String sql = """
            SELECT idComputadora, numeroMaquina, direccionIP, estatus, tipo, idCentroComputo
            FROM computadora
            WHERE direccionIP = ?
        """;

        try (Connection conn = conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ip);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearComputadora(rs);
                }
            }

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al obtener computadora por IP: " + ex.getMessage());
        }

        return null;
    }

    @Override
    public Computadora mostrarComputadoraApartada(Integer idComputadora) throws PersistenciaException {
        String sql = """
            SELECT idComputadora, numeroMaquina, direccionIP, estatus, tipo, idCentroComputo
            FROM computadora
            WHERE idComputadora = ?
            AND estatus = true
        """;

        try (Connection conn = conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idComputadora);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearComputadora(rs);
                }
            }

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al validar computadora disponible: " + ex.getMessage());
        }

        return null;
    }

    @Override
    public void mostrarComputadoraComoDisponible(int idComputadora, Connection transaccion) throws PersistenciaException {
        String sql = """
            UPDATE computadora
            SET estatus = true
            WHERE idComputadora = ?
        """;

        try (PreparedStatement ps = transaccion.prepareStatement(sql)) {
            ps.setInt(1, idComputadora);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No fue posible mostrar la computadora como disponible.");
            }

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al mostrar computadora disponible: " + ex.getMessage());
        }
    }

    @Override
    public ComputadoraDTO obtenerCatalogoSoftwarePC(Integer idComputadora) throws PersistenciaException {
        String sql = """
            SELECT 
                c.idComputadora,
                c.numeroMaquina,
                c.direccionIP,
                c.estatus,
                c.tipo,
                c.idCentroComputo,
                s.idSoftware,
                s.nombre
            FROM computadora c
            INNER JOIN computadora_software cs ON cs.idComputadora = c.idComputadora
            INNER JOIN software s ON s.idSoftware = cs.idSoftware
            WHERE c.idComputadora = ?
        """;

        try (Connection conn = conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idComputadora);

            try (ResultSet rs = ps.executeQuery()) {
                ComputadoraDTO computadora = null;
                List<Software> catalogo = new ArrayList<>();

                while (rs.next()) {
                    if (computadora == null) {
                        computadora = new ComputadoraDTO();
                        computadora.setIdComputadora(rs.getInt("idComputadora"));
                        computadora.setNumeroMaquina(rs.getInt("numeroMaquina"));
                        computadora.setDireccionIP(rs.getString("direccionIP"));
                        computadora.setEstatus(rs.getBoolean("estatus"));
                        computadora.setTipo(rs.getString("tipo"));
                        computadora.setIdCentroComputo(rs.getInt("idCentroComputo"));
                    }

                    Software software = new Software();
                    software.setIdSoftware(rs.getInt("idSoftware"));
                    software.setNombre(rs.getString("nombre"));
                    catalogo.add(software);
                }

                if (computadora != null) {
                    computadora.setCatalogoSoftware(catalogo);
                }

                return computadora;
            }

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al obtener catálogo de software: " + ex.getMessage());
        }
    }

    @Override
    public Computadora obtenerComputadoraPorNumero(int numeroMaquina) throws PersistenciaException {
        String sql = """
            SELECT idComputadora, numeroMaquina, direccionIP, estatus, tipo, idCentroComputo
            FROM computadora
            WHERE numeroMaquina = ?
        """;

        try (Connection conn = conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, numeroMaquina);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearComputadora(rs);
                }
            }

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al obtener computadora por número: " + ex.getMessage());
        }

        return null;
    }

    @Override
    public void actualizarEstatus(int idComputadora, boolean estatus) throws PersistenciaException {
        String sql = """
            UPDATE computadora
            SET estatus = ?
            WHERE idComputadora = ?
        """;

        try (Connection conn = conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, estatus);
            ps.setInt(2, idComputadora);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se encontró la computadora a actualizar.");
            }

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al actualizar estatus de computadora: " + ex.getMessage());
        }
    }

    
    @Override
    public List<Computadora> obtenerMonitoreoEquipos(String busqueda, String filtro) throws PersistenciaException {
    
        List<Computadora> lista = new ArrayList<>();

        String sql = """
            SELECT *
            FROM computadora
            ORDER BY numeroMaquina
        """;

        try (Connection con = conexion.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Computadora computadora = new Computadora();

                computadora.setIdComputadora(rs.getInt("idComputadora"));
                computadora.setNumeroMaquina(rs.getInt("numeroMaquina"));
                computadora.setIp(rs.getString("direccionIp"));
                computadora.setEstatus(rs.getBoolean("estatus"));
                computadora.setTipo(rs.getString("tipo"));
                computadora.setIdCentroComputo(rs.getInt("idCentroComputo"));

                lista.add(computadora);
            }

        } catch (SQLException ex) {
            throw new PersistenciaException(ex.getMessage());
        }

        return lista;
    }

    @Override
    public int contarMonitoreoEquipos(String busqueda, String filtro) throws PersistenciaException {
        String where = construirWhere(filtro);
        String sql = """
            SELECT COUNT(*) AS total
            FROM computadora c
            LEFT JOIN centroComputo cc ON c.idCentroComputo = cc.idCentroComputo
            LEFT JOIN unidadAcademica ua ON cc.idUnidadAcademica = ua.idUnidadAcademica
            LEFT JOIN reserva r
                ON c.idComputadora = r.idComputadora
                AND r.fechaHoraFinal IS NULL
            LEFT JOIN alumno a ON r.idAlumno = a.idAlumno
            LEFT JOIN carrera ca ON a.idCarrera = ca.idCarrera
        """ + where;

        try (Connection conn = conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String valorBusqueda = "%" + busqueda.trim() + "%";

            if ("Todos".equals(filtro)) {

                ps.setString(1, valorBusqueda);
                ps.setString(2, valorBusqueda);
                ps.setString(3, valorBusqueda);
                ps.setString(4, valorBusqueda);
                ps.setString(5, valorBusqueda);
                ps.setString(6, valorBusqueda);
                ps.setString(7, valorBusqueda);

            } else {

                ps.setString(1, valorBusqueda);

            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al contar monitoreo de equipos: " + ex.getMessage());
        }

        return 0;
    }

    private String construirWhere(String filtro) {
        Map<String, String> campos = new HashMap<>();

        campos.put("Máquina", "CAST(c.numeroMaquina AS CHAR)");
        campos.put("ID Alumno", "CAST(a.idAlumno AS CHAR)");
        campos.put("Nombre", "CONCAT_WS(' ', a.nombre, a.apellidoPaterno, a.apellidoMaterno)");
        campos.put("Carrera", "ca.nombre");
        campos.put("Unidad Académica", "ua.nombre");
        campos.put("Centro de Cómputo", "cc.nombre");
        campos.put("Estado", "CASE WHEN c.estatus = true THEN 'Disponible' ELSE 'Bloqueada' END");

        if (filtro == null || filtro.equals("Todos")) {
            return """
                WHERE (
                    CAST(c.numeroMaquina AS CHAR) LIKE ?
                    OR CAST(a.idAlumno AS CHAR) LIKE ?
                    OR CONCAT_WS(' ', a.nombre, a.apellidoPaterno, a.apellidoMaterno) LIKE ?
                    OR ca.nombre LIKE ?
                    OR ua.nombre LIKE ?
                    OR cc.nombre LIKE ?
                    OR CASE WHEN c.estatus = true THEN 'Disponible' ELSE 'Bloqueada' END LIKE ?
                )
            """.replace("?", "?");
        }

        String campo = campos.get(filtro);

        if (campo == null) {
            campo = "CAST(c.numeroMaquina AS CHAR)";
        }

        return " WHERE " + campo + " LIKE ? ";
    }

    private Computadora mapearComputadora(ResultSet rs) throws SQLException {
        Computadora computadora = new Computadora();
        computadora.setIdComputadora(rs.getInt("idComputadora"));
        computadora.setNumeroMaquina(rs.getInt("numeroMaquina"));
        computadora.setIp(rs.getString("direccionIP"));
        computadora.setEstatus(rs.getBoolean("estatus"));
        computadora.setTipo(rs.getString("tipo"));
        computadora.setIdCentroComputo(rs.getInt("idCentroComputo"));
        return computadora;
    }
}
