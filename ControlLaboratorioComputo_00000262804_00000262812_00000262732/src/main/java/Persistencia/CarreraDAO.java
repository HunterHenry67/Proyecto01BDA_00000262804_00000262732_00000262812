 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.Carrera;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Esta clase se encarga de gestionar el acceso a los datos de la tabla 'carrera' en la base de datos
 * Su función es servir de puente para recuperar la información  
 * registrada en el bd.
 * * @author Andre
 */
public class CarreraDAO implements ICarreraDAO{

    private IConexionBD conexion;
    /**
     * Crea una nueva instancia del DAO de carreras.
     * @param conexion El objeto encargado de gestionar la conexión con la base de datos.
     */
    public CarreraDAO(IConexionBD conexion){
        this.conexion = conexion;
    }
    
    /**
     * Busca y recupera la información de una carrera específica basándose en su id
     * Este método hace una consulta a la base de datos y, si encuentra una coincidencia, 
     * devuelve un objeto Carrera con los datos encontrados.
     * * @param idCarrera El id de la carrera que deseas consultar.
     * @return Un objeto Carrera con toda su información, o null si no existe una carrera con ese ID.
     * @throws PersistenciaException Si ocurre algún error técnico al intentar conectar o realizar la consulta en la base de datos.
     */
    @Override
    public Carrera consultarCarrera(int idCarrera) throws PersistenciaException {
        String comandoSQL = """
                            SELECT
                                idCarrera,
                                nombre,
                                tiempoDiario
                            FROM carrera
                            WHERE idCarrera = ?
                            """;
        try (Connection conn = this.conexion.crearConexion();
                PreparedStatement stmnt = conn.prepareStatement(comandoSQL)){
                stmnt.setInt(1, idCarrera);
                
                try(ResultSet resultado = stmnt.executeQuery()) {
                    if(resultado.next()) {
                        Carrera carrera = new Carrera();
                        carrera.setIdCarrera(resultado.getInt("idCarrera"));
                        carrera.setNombre(resultado.getString("nombre"));
                        carrera.setTiempoDiario(resultado.getTime("tiempoDiario").toLocalTime());
                        return carrera;
                    }
                }
        } catch (SQLException ex){
            throw new PersistenciaException("Error al consuultar carrera" + ex.getMessage());
        }
        return null;
    }
}
