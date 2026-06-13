/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.UnidadAcademica;
import java.sql.Connection;
import java.sql.SQLException;
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
                                SELECT(
                                        )
                                """;
            
        }catch(SQLException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al conusltar la Unidad Académica por ID" +ex.getMessage());
        }
    }

    @Override
    public List<UnidadAcademica> consultarUnidadesAcademicas() throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
