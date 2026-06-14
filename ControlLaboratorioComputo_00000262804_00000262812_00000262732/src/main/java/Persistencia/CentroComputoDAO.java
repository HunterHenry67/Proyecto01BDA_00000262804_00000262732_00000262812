/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.CentroComputo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Andre
 */
public class CentroComputoDAO implements ICentroComputoDAO {

    private static final Logger LOGGER = Logger.getLogger(ReservaDAO.class.getName());

    private IConexionBD conexion;

    @Override
    public CentroComputo obtenerPorID(Integer idCentroComputo) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = """
                     SELECT idCentroComputo, contrasenaMaestra, horaInicio, horaFin, idUnidadAcademica
                     FROM centroComputo
                     WHERE idCentroComputo = ?
                     """;
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, idCentroComputo);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                return new CentroComputo(
                        resultado.getInt("idCentroComputo"),
                        resultado.getTime("horaInicio"),
                        resultado.getTime("horaFin"),
                        resultado.getString("contrasenaMaestra"),
                        resultado.getInt("idUnidadAcademica")
                );
            }
            return null;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al obtener el centro de cómputo por ID: " + ex.getMessage());
        } 
    }

    @Override
    public List<CentroComputo> obtenerTodos() throws PersistenciaException {
        List<CentroComputo> lista = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = """
                     SELECT idCentroComputo, contrasenaMaestra, horaInicio, horaFin, idUnidadAcademica
                     FROM centroComputo
                     """;
            PreparedStatement statement = conexion.prepareStatement(sql);
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                lista.add(new CentroComputo(
                        resultado.getInt("idCentroComputo"),
                        resultado.getTime("horaInicio"),
                        resultado.getTime("horaFin"),
                        resultado.getString("contrasenaMaestra"),
                        resultado.getInt("idUnidadAcademica")
                ));
            }
            return lista;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al obtener todos los centros de cómputo: " + ex.getMessage());
        }
    }

    @Override
    public List<CentroComputo> obtenerPorUnidadAcademica(Integer idUnidadAcademica) throws PersistenciaException {
        List<CentroComputo> lista = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = """
                     SELECT idCentroComputo, contrasenaMaestra, horaInicio, horaFin, idUnidadAcademica
                     FROM centroComputo
                     WHERE idUnidadAcademica = ?
                     """;
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, idUnidadAcademica);
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                lista.add(new CentroComputo(
                        resultado.getInt("idCentroComputo"),
                        resultado.getTime("horaInicio"),
                        resultado.getTime("horaFin"),
                        resultado.getString("contrasenaMaestra"),
                        resultado.getInt("idUnidadAcademica")
                ));
            }
            return lista;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al obtener centros por unidad académica: " + ex.getMessage());
        }
    }

    @Override
    public CentroComputo obtenerPorComputadora(Integer idComputadora) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean validarContraseniaMaestra(String contraseniaMaestra) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
