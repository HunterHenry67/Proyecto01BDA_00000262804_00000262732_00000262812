/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.Bloqueo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andre
 */
public class BloqueoDAO implements IBloqueoDAO {

    private IConexionBD conexion;
    private Connection transaccion;

    public BloqueoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public Bloqueo registrarBloqueo(Bloqueo bloqueo) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void desbloquearAlumno(int idAlumno) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Bloqueo> consultar(String filtro) throws PersistenciaException {
        List<Bloqueo> listaBloqueosFiltrada = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                                SELECT
                                    idBloqueo,
                                    fechaHoraInicioBloqueo,
                                    fechaHoraFinalBloqueo,
                                    motivo,
                                    idAlumno
                                FROM bloqueo
                                WHERE DATE_FORMAT(fechaHoraInicioBloqueo, '%Y-%m-%d %H:%i:%s') LIKE ?
                                           OR DATE_FORMAT(fechaHoraFinalBloqueo, '%Y-%m-%d %H:%i:%s') LIKE ?;
                                """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            String busquedaFiltro = "%" + filtro + "%";
            statement.setString(1, busquedaFiltro);
            statement.setString(2, busquedaFiltro);

            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                Timestamp fechaFinal = resultado.getTimestamp("fechaHoraFinalBloqueo");
                listaBloqueosFiltrada.add(new Bloqueo(
                        resultado.getInt("idBloqueo"),
                        resultado.getTimestamp("fechaHoraInicioBloqueo").toLocalDateTime(),
                        fechaFinal != null ? fechaFinal.toLocalDateTime() : null,
                        resultado.getString("motivo"),
                        resultado.getInt("idAlumno")
                ));
            }
            return listaBloqueosFiltrada;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar los bloqueos por filtro: " + ex.getMessage());
        }
    }

    @Override
    public List<Bloqueo> consultarBloqueosActivos() throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
