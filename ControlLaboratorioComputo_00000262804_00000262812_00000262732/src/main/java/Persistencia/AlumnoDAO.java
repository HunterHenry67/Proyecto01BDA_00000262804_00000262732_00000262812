/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.Alumno;
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
public class AlumnoDAO implements IAlumnoDAO {

    private static final Logger LOGGER = Logger.getLogger(AlumnoDAO.class.getName());

    private IConexionBD conexion;
    private Connection transaccion;

    public AlumnoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean validarCredenciales(int idAlumno, String contrasena) {
        String sql = "SELECT idAlumno FROM ALUMNO WHERE idAlumno = ? AND contrasena = ?";

        try (Connection conn = conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAlumno);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error en AlumnoDAO.validarCredenciales: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Alumno> consultar(String filtro) throws PersistenciaException {
        List<Alumno> listaAlumnos = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                                SELECT 
                                    idAlumno,
                                    nombre,
                                    apellidoPaterno,
                                    apellidoMaterno,
                                    estatus,
                                    contrasena,
                                    idCarrera
                                FROM alumno
                                WHERE nombre LIKE ?
                                   OR estatus LIKE ?;
                            """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            String busquedaFiltro = "%"+ filtro + "%";
            statement.setString(1, busquedaFiltro);
            statement.setString(2, busquedaFiltro);
            ResultSet resultado = statement.executeQuery();
            if(resultado.next()){
                listaAlumnos.add(new Alumno(resultado.getInt("idAlumno"),
                                            resultado.getString("nombre"),
                                            resultado.getString("apellidoPaterno"),
                                            resultado.getString("apellidoPaterno"),
                                            resultado.getBoolean("estatus"),
                                            resultado.getString("contrasena"),
                                            resultado.getInt("idCarrera")));
            }
            return listaAlumnos;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar los alumnos por filtro: " + ex.getMessage());
        }
    }

    @Override
    public Alumno consultarAlumnoPorID(int idAlumno) throws PersistenciaException {
        try(Connection conexion = this.conexion.crearConexion()){
            String comandoSQL = """
                                SELECT 
                                    idAlumno,
                                    nombre,
                                    apellidoPaterno,
                                    apellidoMaterno,
                                    estatus,
                                    contrasena,
                                    idCarrera
                                FROM alumno
                                WHERE idAlumno LIKE ?
                                """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            statement.setInt(1, idAlumno);
            ResultSet resultado = statement.executeQuery();
            
            if(resultado.next()){
                return new Alumno(resultado.getInt("idAlumno"),
                                    resultado.getString("nombre"),
                                    resultado.getString("apellidoPaterno"),
                                    resultado.getString("apellidoMaterno"),
                                    resultado.getBoolean("estatus"),
                                    resultado.getString("contrasena"),
                                    resultado.getInt("idCarrera"));    
            }
            return null;
        }catch(SQLException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar el alumno por ID: " +ex.getMessage());
        }
    }

    @Override
    public boolean estaBloqueado(int idAlumno) throws PersistenciaException {
        try(Connection conexion = this.conexion.crearConexion()){
            String comandoSQL = """
                                
                                """;
        }catch(SQLException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al identificar si el alumno está bloqueado: "+ex.getMessage());
        }
        
    }

    @Override
    public Alumno consultarCredenciales(int idAlumno, String contrasena) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
