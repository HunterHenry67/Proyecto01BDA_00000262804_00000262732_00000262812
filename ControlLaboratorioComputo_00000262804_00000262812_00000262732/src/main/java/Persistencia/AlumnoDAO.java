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

    public AlumnoDAO() {

    }

    public AlumnoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public Alumno consultarCredenciales(int idAlumno, String contrasena) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                                SELECT idAlumno,
                                       nombre,
                                       apellidoPaterno,
                                       apellidoMaterno,
                                       estatus,
                                       contrasena,
                                       idCarrera
                                FROM alumno
                                WHERE idAlumno = ? 
                                    AND contrasena = ?;
                                """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            statement.setInt(1, idAlumno);
            statement.setString(2, contrasena);
            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                return new Alumno(resultado.getInt("idAlumno"),
                        resultado.getString("nombre"),
                        resultado.getString("apellidoPaterno"),
                        resultado.getString("apellidoMaterno"),
                        resultado.getBoolean("estatus"),
                        resultado.getString("contrasena"),
                        resultado.getInt("idCarrera"));
            }
            return null;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar las credenciales del alumno: " + ex.getMessage());
        }
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
                                   OR CAST(estatus AS CHAR) LIKE ?;
                            """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            String busquedaFiltro = "%" + filtro + "%";
            statement.setString(1, busquedaFiltro);
            statement.setString(2, busquedaFiltro);
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                listaAlumnos.add(new Alumno(
                        resultado.getInt("idAlumno"),
                        resultado.getString("nombre"),
                        resultado.getString("apellidoPaterno"),
                        resultado.getString("apellidoMaterno"),
                        resultado.getBoolean("estatus"),
                        resultado.getString("contrasena"),
                        resultado.getInt("idCarrera")
                ));
            }
            return listaAlumnos;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar los alumnos por filtro: " + ex.getMessage());
        }
    }

    @Override
    public Alumno consultarAlumnoPorID(int idAlumno) throws PersistenciaException {
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
                                WHERE idAlumno = ?
                                """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            statement.setInt(1, idAlumno);
            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                return new Alumno(resultado.getInt("idAlumno"),
                        resultado.getString("nombre"),
                        resultado.getString("apellidoPaterno"),
                        resultado.getString("apellidoMaterno"),
                        resultado.getBoolean("estatus"),
                        resultado.getString("contrasena"),
                        resultado.getInt("idCarrera"));
            }
            return null;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar el alumno por ID: " + ex.getMessage());
        }
    }

    public boolean estaBloqueado(int idAlumno) throws PersistenciaException {
        String comandoSQL = """
                        SELECT COUNT(*) AS total
                        FROM bloqueo
                        WHERE idAlumno = ?
                        AND (fechaHoraFinBloqueo IS NULL OR fechaHoraFinBloqueo > NOW());
                        """;

        try (Connection conexion = this.conexion.crearConexion(); 
             PreparedStatement statement = conexion.prepareStatement(comandoSQL)) {
            statement.setInt(1, idAlumno);
            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt("total")>0;
                }
            }
            return false;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al identificar si el alumno está bloqueado: "+ ex.getMessage());
        }
    }
}
