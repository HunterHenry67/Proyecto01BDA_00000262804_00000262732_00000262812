/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dtos.FinalizarReservaDTO;
import Dtos.GuardarReservaDTO;
import Entidades.Reserva;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Andre
 */
public class ReservaDAO implements IReservaDAO {

    private static final Logger LOGGER = Logger.getLogger(ReservaDAO.class.getName());

    private IConexionBD conexion;
    private Connection transaccion;

    public ReservaDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public int registrarReserva(GuardarReservaDTO reserva) throws PersistenciaException {
        try {
            String comandoSQL = """
                                INSERT INTO reserva(
                                    fechaHoraApartado,
                                    fechaHoraInicio,
                                    fechaHoraFinal,
                                    tiempoUso,
                                    idAlumno,
                                    idComputadora
                                ) VALUES (?, ?, ?, ?, ?, ?);
                                """;
            PreparedStatement statement = this.transaccion.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS);
            statement.setTimestamp(1, Timestamp.valueOf(reserva.getFechaHoraApartado()));
            if (reserva.getFechaHoraInicio() != null) {
                statement.setTimestamp(2, Timestamp.valueOf(reserva.getFechaHoraApartado()));
            } else {
                statement.setNull(3, Types.TIMESTAMP);
            }
            if (reserva.getTiempoUso() != null) {
                statement.setInt(4, reserva.getTiempoUso());
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            statement.setInt(5, reserva.getIdAlumno());
            statement.setInt(6, reserva.getIdComputadora());
            statement.executeUpdate();
            ResultSet resultado = statement.getGeneratedKeys();
            if (resultado.next()) {
               return resultado.getInt(1);
            }
            throw new PersistenciaException("No fue posbile obtener el id de la reserva generada.");
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al registrar la Reserva: " + ex.getMessage());
        }
    }

    @Override
    public Reserva consultarResrevaActivaPorAlumno(int idAlumno) throws PersistenciaException {
        List<Reserva> listaReservasActivas = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                                SELECT 
                                  idReserva,
                                  fechaHoraApartado,
                                  fechaHoraInicio,
                                  fechaHoraFinal,
                                  tiempoUso,
                                  idAlumno,
                                  idComputadora
                              FROM reserva
                              WHERE idAlumno = ?
                                AND fechaHoraFinal IS NULL;
                                """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            statement.setInt(1, idAlumno);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                Timestamp fechaInicio = resultado.getTimestamp("fechaHoraInicio");
                Timestamp fechaFinal = resultado.getTimestamp("fechaHoraFinal");
                return new Reserva(resultado.getInt("idReserva"),
                        resultado.getTimestamp("fechaHoraApartado").toLocalDateTime(),
                        fechaInicio != null ? fechaInicio.toLocalDateTime() : null,
                        fechaFinal != null ? fechaFinal.toLocalDateTime() : null,
                        resultado.getObject("tiempoUso") != null ? resultado.getInt("tiempoUso") : null,
                        resultado.getInt("idAlumno"),
                        resultado.getInt("idComputadora"));
            }
            return null;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar las reservas activas: " + ex.getMessage());
        }
    }

    @Override
    public Reserva consultarReservaActivaPorComputadora(int idComputadora) throws PersistenciaException {
        String comandoSQL = """
                      SELECT 
                      idReserva,
                      fechaHoraApartado,
                      fechaHoraInicio,
                      fechaHoraFinal,
                      tiempoUso,
                      idAlumno,
                      idComputadora
                  FROM reserva
                  WHERE idComputadora = ?
                    AND fechaHoraFinal IS NULL;
            """;
        try (Connection conn = conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {
            ps.setInt(1, idComputadora);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reserva reserva = new Reserva();
                    reserva.setIdReserva(rs.getInt("idReserva"));
                    reserva.setFechaHoraApartado(rs.getTimestamp("fechaHoraApartado").toLocalDateTime());
                    reserva.setIdAlumno(rs.getInt("idAlumno"));
                    reserva.setIdComputadora(rs.getInt("idComputadora"));
                    return reserva;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar reservas activas por computadora: " + e.getMessage());
        }

    }

    @Override
    public List<Reserva> consultar(String filtro) throws PersistenciaException {
        List<Reserva> listaFiltroReserva = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                                SELECT 
                                   r.idReserva,
                                   r.fechaHoraApartado,
                                   r.fechaHoraInicio,
                                   r.fechaHoraFinal,
                                   r.tiempoUso,
                                   r.idAlumno,
                                   r.idComputadora,
                                   CONCAT(a.nombre, ' ', a.apellidoPaterno, ' ', a.apellidoMaterno) AS alumnoNombre,
                                   c.numeroMaquina,
                                   c.direccionIP,
                                   c.estatus AS estatusComputadora,
                                   c.tipo
                               FROM reserva r
                               INNER JOIN alumno a ON r.idAlumno = a.idAlumno
                               INNER JOIN computadora c ON r.idComputadora = c.idComputadora
                               WHERE DATE_FORMAT(r.fechaHoraApartado, '%Y-%m-%d %H:%i:%s') LIKE ?
                                  OR DATE_FORMAT(r.fechaHoraInicio, '%Y-%m-%d %H:%i:%s') LIKE ?
                                  OR DATE_FORMAT(r.fechaHoraFinal, '%Y-%m-%d %H:%i:%s') LIKE ?
                                  OR CONCAT(a.nombre, ' ', a.apellidoPaterno, ' ', a.apellidoMaterno) LIKE ?
                                  OR CAST(c.numeroMaquina AS CHAR) LIKE ?
                                  OR c.direccionIP LIKE ?
                                  OR c.estatus LIKE ?
                                  OR c.tipo LIKE ?;
                                """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            String busquedaFiltro = "%" + filtro + "%";
            statement.setString(1, busquedaFiltro);
            statement.setString(2, busquedaFiltro);
            statement.setString(3, busquedaFiltro);
            statement.setString(4, busquedaFiltro);
            statement.setString(5, busquedaFiltro);
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                Timestamp fechaInicio = resultado.getTimestamp("fechaHoraInicio");
                Timestamp fechaFinal = resultado.getTimestamp("fechaHoraFinal");
                listaFiltroReserva.add(new Reserva(resultado.getInt("idReserva"),
                        resultado.getTimestamp("fechaHoraApartado").toLocalDateTime(),
                        fechaInicio != null ? fechaInicio.toLocalDateTime() : null,
                        fechaFinal != null ? fechaFinal.toLocalDateTime() : null,
                        resultado.getObject("tiempoUso") != null ? resultado.getInt("tiempoUso") : null,
                        resultado.getInt("idAlumno"),
                        resultado.getInt("idComputadora")
                ));
            }
            return listaFiltroReserva;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar las reservas por filtro: " + ex.getMessage());
        }
    }

    @Override
    public List<Reserva> consultarReservasActivas() throws PersistenciaException {
        List<Reserva> listaReservas = new ArrayList<>();
        String sql = """
                     SELECT 
                     r.idReserva,
                     r.fechaHoraApartado,
                     r.fechaHoraInicio,
                     r.fechaHoraFinal,
                     r.tiempoUso,
                     r.idAlumno,
                     r.idComputadora,
                     CONCAT(a.nombre, ' ', a.apellidoPaterno, ' ', a.apellidoMaterno) AS alumnoNombre
                     FROM reserva r
                     INNER JOIN alumno a ON r.idAlumno = a.idAlumno
                     WHERE r.fechaHoraFinal IS NULL
                     """;
        try (Connection conn = this.conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setIdReserva(rs.getInt("idReserva"));
                reserva.setFechaHoraApartado(rs.getObject("fechaHoraApartado", java.time.LocalDateTime.class));
                reserva.setFechaHoraInicio(rs.getObject("fechaHoraInicio", java.time.LocalDateTime.class));
                reserva.setFechaHoraFinal(rs.getObject("fechaHoraFinal", java.time.LocalDateTime.class));
                reserva.setTiempoUso(rs.getInt("tiempoUso"));
                reserva.setIdAlumno(rs.getInt("idAlumno"));
                reserva.setIdComputadora(rs.getInt("idComputadora"));

                listaReservas.add(reserva);
            }
            return listaReservas;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar la lista de reservas activas: " + ex.getMessage());
        }

    }

    @Override
    public void finalizarReserva(FinalizarReservaDTO reserva) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void cancelarReserva(CancelarReservaDTO ) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int consultarMinutosUsadosPorAlumno(int idAlumno) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
