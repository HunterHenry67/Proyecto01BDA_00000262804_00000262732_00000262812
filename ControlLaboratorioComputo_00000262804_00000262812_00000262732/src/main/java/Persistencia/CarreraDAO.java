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
 *
 * @author Andre
 */
public class CarreraDAO implements ICarreraDAO{

    private IConexionBD conexion;
    
    public CarreraDAO(IConexionBD conexion){
        this.conexion = conexion;
    }
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
                        carrera.setTiempoDiario(resultado.getInt("tiempoDiario"));
                        return carrera;
                    }
                }
        } catch (SQLException ex){
            throw new PersistenciaException("Error al consuultar carrera" + ex.getMessage());
        }
        return null;
    }
}
