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

/**
 *
 * @author Andre
 */
public class UnidadAcademicaDAO implements IUnidadAcademicaDAO{

    private static final Logger LOGGER = Logger.getLogger(UnidadAcademicaDAO.class.getName());
    
    private IConexionBD conexion;
    
    public UnidadAcademicaDAO(IConexionBD conexion){
        this.conexion = conexion;
    }

    
    @Override
    public UnidadAcademica consultarUnidadAcademicaPorID(Integer idUndadAcademica) throws PersistenciaException {
        try(Connection conexion = this.conexion.crearConexion()){
            String comandoSQL = """
                                SELECT 
                                    idUnidadAcademica,
                                    nombre
                                FROM unidadAcademica
                                WHERE id = ?                                    
                                """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            statement.setInt(1, idUndadAcademica);
            ResultSet resultado = statement.executeQuery();
            if(resultado.next()){
                return new UnidadAcademica(
                        resultado.getInt("idUnidadAcademica"), 
                        resultado.getString("nombre"));
            }
            return null;         
        }catch(SQLException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al conusltar la Unidad Académica por ID: " +ex.getMessage());
        }
    }

    @Override
    public List<UnidadAcademica> consultarUnidadesAcademicas(String filtro) throws PersistenciaException {
        List<UnidadAcademica> listaUnidadesAcademicas = new ArrayList<>();
        try(Connection conexion = this.conexion.crearConexion()){
            String comandoSQL = """
                                SELECT idUnidadAcademica,
                                        nombre,
                                FROM unidadAcedmica
                                WHERE nombre = ?;
                                """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            String filtroBusqueda = "%" + filtro + "%";
            statement.setString(1, filtroBusqueda);
            
            ResultSet resultado = statement.executeQuery();
            if(resultado.next()){
                listaUnidadesAcademicas.add(new UnidadAcademica(
                                                resultado.getInt("id"),
                                                resultado.getString("nombre")));
            }
            return listaUnidadesAcademicas;
        }catch(SQLException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar el listado de unidades académicas: " +ex.getMessage());
        }
    }
    
}
