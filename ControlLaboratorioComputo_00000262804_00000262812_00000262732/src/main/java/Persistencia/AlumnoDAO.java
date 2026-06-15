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
    /**
     * Consulta las credenciales de un alumno en la base de datos.
     * @param idAlumno Identificador de alumno que intenta inciar sesión.
     * @param contrasena Contraseña ingresada por el alumno.
     * @return Alumno encontrado con las credenciales recibidads.
     * @throws PersistenciaException si ocurre un error al consultar la base de datos.
     */
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
    /**
     * Consulta los alumnos registrados aplicando un filtro de búsqueda.
     * @param filtro texto utilizado para buscar coincidencias entre los alumnos.
     * @return lista de alumnos que coinciden con el filtro indicado.
     * @throws PersistenciaException si ocurre un error al consultar los alumnos en la base de datos.
     */
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
    /**
     * Consulta un alumno específico mediante su identificador.
     * @param idAlumno identificador del alumno que se desea consultar.
     * @return alumno encontrado; null si no existe un alumno con ese identificador.
     * @throws PersistenciaException si ocurre un error al consultar el alumno en la base de datos.
     */
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
    /**
     * Verifica si un alumno tiene un bloqueo activo.
     * @param idAlumno identificador del alumno que se desea verificar.
     * @return true si el alumno tiene un bloqueo activo; false si no tiene bloqueos activos.
     * @throws PersistenciaException si ocurre un error al consultar los bloqueos del alumno en la base de datos.
     */
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
