/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.UnidadAcademica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;



public class UnidadAcademicaDAO implements IUnidadAcademicaDAO {

    private static final Logger LOGGER = Logger.getLogger(UnidadAcademicaDAO.class.getName());

    private IConexionBD conexion;

    public UnidadAcademicaDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    /**
     * Consulta una unidad académica específica a partir de su identificador.
     * @param idUnidadAcademica identificador de la unidad académica que se desea consultar.
     * @return unidad académica encontrada; null si no existe una unidad académica con ese ID.
     * @throws PersistenciaException si ocurre un error al consultar la unidad académica en la base de datos.
     */
    @Override
    public UnidadAcademica consultarUnidadAcademicaPorID(Integer idUnidadAcademica) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                    SELECT idUnidadAcademica,
                           nombre
                    FROM unidadAcademica
                    WHERE idUnidadAcademica = ?
                    """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            statement.setInt(1, idUnidadAcademica);

            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                return new UnidadAcademica(
                        resultado.getInt("idUnidadAcademica"),
                        resultado.getString("nombre"));
            }
            return null;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar la Unidad Académica por ID: " + ex.getMessage());
        }
    }

    /**
     * Consulta las unidades académicas registradas aplicando un filtro de búsqueda.
     * @param filtro texto utilizado para buscar unidades académicas por nombre.
     * @return lista de unidades académicas que coinciden con el filtro indicado.
     * @throws PersistenciaException si ocurre un error al consultar el listado de unidades académicas.
     */
    @Override
    public List<UnidadAcademica> consultarUnidadesAcademicas(String filtro) throws PersistenciaException {
        List<UnidadAcademica> listaUnidadesAcademicas = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                    SELECT idUnidadAcademica,
                           nombre
                    FROM unidadAcademica
                    WHERE nombre LIKE ?
                    """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            String filtroBusqueda = "%" + filtro + "%";
            statement.setString(1, filtroBusqueda);

            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                listaUnidadesAcademicas.add(new UnidadAcademica(
                        resultado.getInt("idUnidadAcademica"),
                        resultado.getString("nombre")));
            }
            return listaUnidadesAcademicas;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar el listado de unidades académicas: " + ex.getMessage());
        }
    }
}
