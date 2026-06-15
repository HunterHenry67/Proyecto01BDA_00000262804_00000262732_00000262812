/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.Bloqueo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase es la responsable de gestionar todas las operaciones de acceso a datos 
 * relacionadas con la tabla de bloqueos en la base de datos. 
 * Se encarga de ejecutar las consultas, inserciones y actualizaciones necesarias 
 * para mantener el historial y el estado actual de los bloqueos de los alumnos.
 * * @author Andre
 */
public class BloqueoDAO implements IBloqueoDAO {

    private IConexionBD conexion;
    private Connection transaccion;

    public BloqueoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }
    /**
     * Registra un nuevo bloqueo en la base de datos
     * Este método utiliza una transacción para garantizar la integridad de los datos
     * Si la inserción se realiza correctamente, recupera automáticamente el id 
     * de la base de datos y lo asigna al objeto agregado 
     * En caso de error, realiza un rollback para revertir cambios.
     * * @param bloqueo El objeto Bloqueo que contiene la información a registrar 
     * (fecha de inicio, fecha de fin, motivo e ID del alumno).
     * @return El mismo objeto Bloqueo con su identificador ID actualizado tras la inserción.
     * @throws PersistenciaException Si ocurre un error en la conexión, una violación 
     * de integridad en SQL o si la transacción falla.
     */
    @Override
    public Bloqueo registrarBloqueo(Bloqueo bloqueo) throws PersistenciaException {
        String comandoSQL = """
                            INSERT INTO bloqueo (fechaHoraInicioBloqueo, fechaHoraFinBloqueo, motivo, idAlumno)
                            VALUES (?, ?, ?, ?)
                            """;
        try (Connection conn = this.conexion.crearConexion()) {
            try {
                conn.setAutoCommit(false);
                try (PreparedStatement statement = conn.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS)) {

                    statement.setObject(1, bloqueo.getFechaHoraIncioBloqueo());
                    statement.setObject(2, bloqueo.getFechaHoraFinalBloqueo());
                    statement.setString(3, bloqueo.getMotivo());
                    statement.setInt(4, bloqueo.getIdAlumno());
                    int filasAfectadas = statement.executeUpdate();
                    if (filasAfectadas == 0) {
                        throw new SQLException("La inserción falló");
                    }
                    try (ResultSet llavesGeneradas = statement.getGeneratedKeys()) {
                        if (llavesGeneradas.next()) {
                            bloqueo.setIdBloqueo(llavesGeneradas.getInt(1));
                        }
                    }
                    conn.commit();
                    return bloqueo;
                }
            } catch (SQLException ex) {
                conn.rollback();
                throw new PersistenciaException("Error al registrar bloqueo en bloqueoDAO " + ex.getMessage());
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error en la conexion, no se pudo registrar el bloqueo: " + ex.getMessage());
        }
    }
    
    /**
     * Finaliza el bloqueo de un alumno marcando la hora actual como fin.
     * @param idAlumno El ID del alumno que quieres desbloquear.
     * @throws PersistenciaException Si no se encuentra el bloqueo o falla la base de datos.
     */
    @Override
    public void desbloquearAlumno(int idAlumno) throws PersistenciaException {
        String comandoSQL = """
                            UPDATE bloqueo 
                            SET fechaHoraFinBloqueo = NOW() 
                            WHERE idAlumno = ? 
                              AND (fechaHoraFinBloqueo > NOW() OR fechaHoraFinBloqueo IS NULL)
                            """;

        try (Connection conn = this.conexion.crearConexion()) {

            try {
                conn.setAutoCommit(false);

                try (PreparedStatement statement = conn.prepareStatement(comandoSQL)) {
                    statement.setInt(1, idAlumno);

                    int filasAfectadas = statement.executeUpdate();
                    if (filasAfectadas == 0) {
                        throw new PersistenciaException("No se encontró ningún bloqueopara este alumno.");
                    }
                    conn.commit();
                }

            } catch (SQLException ex) {
                conn.rollback();
                throw new PersistenciaException("Error en desbloqueo de alumno de BloqueoDAO");
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error en la conexion, no se pudo desbloquear al alumno BloqueoDAO: " + ex.getMessage());
        }
    }

    /**
     * Busca bloqueos en la base de datos permitiendo aplicar un filtro de texto.
     * Es ideal para cuando necesitas buscar bloqueos específicos basándote en la fecha de inicio/fin 
     * o en el motivo escrito.
     * * @param filtro El texto, fecha o fragmento que quieres utilizar para filtrar los resultados de búsqueda.
     * @return Una lista con todos los objetos Bloqueo que coinciden con el criterio de búsqueda.
     * @throws PersistenciaException Si hubo algún error técnico al realizar la consulta a la base de datos.
     */
    @Override
    public List<Bloqueo> consultar(String filtro) throws PersistenciaException {
        List<Bloqueo> listaBloqueosFiltrada = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                                SELECT
                                    idBloqueo,
                                    fechaHoraInicioBloqueo,
                                    fechaHoraFinBloqueo,
                                    motivo,
                                    idAlumno
                                FROM bloqueo
                                WHERE DATE_FORMAT(fechaHoraInicioBloqueo, '%Y-%m-%d %H:%i:%s') LIKE ?
                                           OR DATE_FORMAT(fechaHoraFinBloqueo, '%Y-%m-%d %H:%i:%s') LIKE ?;
                                """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            String busquedaFiltro = "%" + filtro + "%";
            statement.setString(1, busquedaFiltro);
            statement.setString(2, busquedaFiltro);

            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                Timestamp fechaFinal = resultado.getTimestamp("fechaHoraFinBloqueo");
                listaBloqueosFiltrada.add(new Bloqueo(
                        resultado.getInt("idBloqueo"),
                        resultado.getTimestamp("fechaHoraInicioBloqueo").toLocalDateTime(),
                        fechaFinal != null ? fechaFinal.toLocalDateTime() : null,
                        resultado.getString("motivo"),
                        resultado.getInt("idAlumno")
                ));
            }
            return listaBloqueosFiltrada;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar los bloqueos por filtro: " + ex.getMessage());
        }
    }

    /**
     * Recupera una lista completa de todos los bloqueos que están activos en este preciso momento.
     * El sistema considera que un bloqueo está activo si la fecha de fin es posterior a la hora 
     * actual o si todavía no se ha registrado una fecha de finalización.
     * * @return Una lista con todos los objetos Bloqueo que se encuentran vigentes actualmente.
     * @throws PersistenciaException Si ocurre un error de comunicación con la base de datos durante la consulta.
     */
    @Override
    public List<Bloqueo> consultarBloqueosActivos() throws PersistenciaException {
        List<Bloqueo> listaBloqueos = new ArrayList<>();
        String comandoSQL = """
                            SELECT 
                                idBloqueo,
                                fechaHoraInicioBloqueo,
                                fechaHoraFinBloqueo,
                                motivo,
                                idAlumno
                            FROM bloqueo
                            WHERE fechaHoraFinBloqueo > NOW() OR fechaHoraFinBloqueo IS NULL
                            """;

        try (Connection conn = this.conexion.crearConexion(); 
            PreparedStatement statement = conn.prepareStatement(comandoSQL); ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {
                Bloqueo bloqueo = new Bloqueo();

                bloqueo.setIdBloqueo(resultado.getInt("idBloqueo"));
                bloqueo.setFechaHoraIncioBloqueo(resultado.getObject("fechaHoraInicioBloqueo", java.time.LocalDateTime.class));
                bloqueo.setFechaHoraFinalBloqueo(resultado.getObject("fechaHoraFinBloqueo", java.time.LocalDateTime.class));
                bloqueo.setMotivo(resultado.getString("motivo"));
                bloqueo.setIdAlumno(resultado.getInt("idAlumno"));

                listaBloqueos.add(bloqueo);
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al buscar los bloqueos activos: " + ex.getMessage());
        }

        return listaBloqueos;
    }
    
    

}
