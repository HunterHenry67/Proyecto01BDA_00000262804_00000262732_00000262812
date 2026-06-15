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
 * Clase encargada de realizar todas las consultas a la base de datos relacionadas 
 * con los centros de cómputo. Aquí es donde se conecta el código con las tablas 
 * para obtener información sobre los centros, sus horarios y ubicaciones.
 * * @author Andre
 */
public class CentroComputoDAO implements ICentroComputoDAO {

    private static final Logger LOGGER = Logger.getLogger(ReservaDAO.class.getName());

    private IConexionBD conexion;

    public CentroComputoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    /**
     * Busca en la base de datos un centro de cómputo específico usando su id
     * Si encuentra una coincidencia, devuelve el objeto con todos sus datos; si no, regresa nulo.
     * * @param idCentroComputo El número de ID del centro de cómputo que quieres buscar.
     * @return El objeto CentroComputo con su información, o null si no existe.
     * @throws PersistenciaException Si hubo un error técnico al intentar conectar o consultar la base de datos.
     */
    @Override
    public CentroComputo obtenerPorID(Integer idCentroComputo) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = """
                     SELECT idCentroComputo,horaInicio, horaFin, idUnidadAcademica
                     FROM centroComputo
                     WHERE idCentroComputo = ?
                     """;
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, idCentroComputo);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                CentroComputo cc = new CentroComputo();
                cc.setIdCentroComputo(resultado.getInt("idCentroComputo"));
                cc.setHoraInicio(resultado.getTime("horaInicio"));
                cc.setHOranFin(resultado.getTime("horaFin"));
                cc.setIdUnidadAcademica(resultado.getInt("idUnidadAcademica"));
                return cc;
            }
            return null;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al obtener el centro de cómputo por ID: " + ex.getMessage());
        }
    }

    /**
     * Trae una lista completa de todos los centros de cómputo que están registrados 
     * actualmente en el sistema.
     * * @return Una lista con todos los objetos CentroComputo encontrados.
     * @throws PersistenciaException Si ocurrió un problema al recuperar los registros de la base de datos.
     */
    @Override
    public List<CentroComputo> obtenerTodos() throws PersistenciaException {
        List<CentroComputo> lista = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = """
                     SELECT idCentroComputo, horaInicio, horaFin, idUnidadAcademica
                     FROM centroComputo
                     """;
            PreparedStatement statement = conexion.prepareStatement(sql);
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                CentroComputo cc = new CentroComputo();
                cc.setIdCentroComputo(resultado.getInt("idCentroComputo"));
                cc.setHoraInicio(resultado.getTime("horaInicio"));
                cc.setHOranFin(resultado.getTime("horaFin"));
                cc.setIdUnidadAcademica(resultado.getInt("idUnidadAcademica"));
                lista.add(cc);
            }
            return lista;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al obtener todos los centros de cómputo: " + ex.getMessage());
        }
    }

    /**
     * Busca todos los centros de cómputo que pertenecen a una unidad académica en particular
     * Esto es útil para listar solo los centros de una zona o edificio específico.
     * * @param idUnidadAcademica El ID de la unidad académica que se desea consultar.
     * @return Una lista con los centros de cómputo encontrados en esa unidad.
     * @throws PersistenciaException Si falló la comunicación con la base de datos.
     */
    @Override
    public List<CentroComputo> obtenerPorUnidadAcademica(Integer idUnidadAcademica) throws PersistenciaException {
        List<CentroComputo> lista = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = """
                     SELECT idCentroComputo, horaInicio, horaFin, idUnidadAcademica
                     FROM centroComputo
                     WHERE idUnidadAcademica = ?
                     """;
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, idUnidadAcademica);
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                CentroComputo cc = new CentroComputo();
                cc.setIdCentroComputo(resultado.getInt("idCentroComputo"));
                cc.setHoraInicio(resultado.getTime("horaInicio"));
                cc.setHOranFin(resultado.getTime("horaFin"));
                cc.setIdUnidadAcademica(resultado.getInt("idUnidadAcademica"));
                lista.add(cc);
            }
            return lista;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al obtener centros por unidad académica: " + ex.getMessage());
        }
    }

    /**
     * Averigua a qué centro de cómputo pertenece una computadora  
     * Hace un cruce de información para localizar físicamente dónde está un equipo.
     * * @param idComputadora El ID de la computadora de la cual queremos saber su ubicación.
     * @return El objeto CentroComputo donde se encuentra la computadora, o null si no se encontró.
     * @throws PersistenciaException Si hubo un error al realizar la búsqueda o la conexión.
     */
    @Override
    public CentroComputo obtenerPorComputadora(Integer idComputadora) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = """
                     SELECT cc.idCentroComputo, cc.horaInicio, cc.horaFin, cc.idUnidadAcademica
                     FROM centroComputo cc
                     INNER JOIN computadora c ON c.idCentroComputo = cc.idCentroComputo
                     WHERE c.idComputadora = ?
                     """;
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, idComputadora);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                CentroComputo cc = new CentroComputo();
                cc.setIdCentroComputo(resultado.getInt("idCentroComputo"));
                cc.setHoraInicio(resultado.getTime("horaInicio"));
                cc.setHOranFin(resultado.getTime("horaFin"));
                cc.setIdUnidadAcademica(resultado.getInt("idUnidadAcademica"));
                return cc;
            }
            return null;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al obtener el centro de cómputo por computadora: " + ex.getMessage());
        }
    }

    /**
     * Revisa si una contraseña maestra es correcta verificándola con el registros 
     * de la base de datos, es un método para procesos de validación de administrador
     * * @param contraseniaMaestra La contraseña que el usuario escribió para ser validada.
     * @return true si la contraseña coincide con alguna registrada, false en caso contrario.
     * @throws PersistenciaException Si hubo un problema al ejecutar la consulta de verificación.
     */
    @Override
    public boolean validarContraseniaMaestra(String contraseniaMaestra) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String sql = """
                     SELECT idCentroComputo
                     FROM centroComputo
                     WHERE contrasenaMaestra = ?
                     """;
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setString(1, contraseniaMaestra);
            ResultSet resultado = statement.executeQuery();
            return resultado.next();
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al validar la contraseña maestra: " + ex.getMessage());
        }
    }
}
