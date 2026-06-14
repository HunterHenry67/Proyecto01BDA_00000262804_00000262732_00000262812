/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dtos.CancelarReservaDTO;
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
                statement.setTimestamp(2, Timestamp.valueOf(reserva.getFechaHoraInicio()));
            } else {
                statement.setNull(2, Types.TIMESTAMP);
            }
            statement.setNull(3, Types.TIMESTAMP);
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
            statement.setString(6, busquedaFiltro);
            statement.setString(7, busquedaFiltro);
            statement.setString(8, busquedaFiltro);
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
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar la lista de reservas activas: " + ex.getMessage());
        }

    }

    @Override
    public void finalizarReserva(FinalizarReservaDTO reserva) throws PersistenciaException {
        try {
            String comandoSQL = """
                                UPDATE reserva
                                    SET fechaHoraFinal = ?,
                                        tiempoUso = TIMESTAMPDIFF(
                                            MINUTE,
                                            COALESCE(fechaHoraInicio, fechaHoraApartado),
                                            ?
                                        )
                                    WHERE idReserva = ?
                                      AND fechaHoraFinal IS NULL;
                                """;
            PreparedStatement statement = this.transaccion.prepareStatement(comandoSQL);
            Timestamp fechaFinal = Timestamp.valueOf(reserva.getFechaHoraFinal());

            statement.setTimestamp(1, fechaFinal);
            statement.setTimestamp(2, fechaFinal);
            statement.setInt(3, reserva.getIdReserva());

            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenciaException("Reserva ya finalizada.");
            }
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al finalizar reserva: " + ex.getMessage());
        }
    }

    @Override
    public void cancelarReserva(int idReserva) throws PersistenciaException {
        try {
            String comandoSQL = """
                                UPDATE reserva
                                    SET fechaHoraFinal = NOW(),
                                        tiempoUso = 0
                                    WHERE idReserva = ?
                                      AND fechaHoraFinal IS NULL;
                                """;
            PreparedStatement statement = this.transaccion.prepareStatement(comandoSQL);
            statement.setInt(1, idReserva);
            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenciaException("No se pudo cancelar la reserva. Puede que ya esté finalizada.");
            }
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al cancelar la reserva: " + ex.getMessage());
        }
    }

    @Override
    public Reserva consultarReservaPorID(int idReserva) throws PersistenciaException {
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
                                WHERE idReserva = ?;
                                """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            statement.setInt(1, idReserva);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                Timestamp fechaInicio = resultado.getTimestamp("fechaHoraInicio");
                Timestamp fechaFinal = resultado.getTimestamp("fechaHoraFinal");

                return new Reserva(
                        resultado.getInt("idReserva"),
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
            throw new PersistenciaException("Error al consultar la reserva por ID: " + ex.getMessage());
        }
    }

    @Override
    public int consultarMinutosUsadosPorAlumno(int idAlumno) throws PersistenciaException {
        String comandoSQL = """
                            SELECT COALESCE(SUM(tiempoUso), 0) AS minutosUsados
                                    FROM reserva
                                    WHERE idAlumno = ?
                                      AND DATE(fechaHoraApartado) = CURDATE()
                                      AND fechaHoraFinal IS NOT NULL;
                        """;
        try (Connection conexionBD = this.conexion.crearConexion(); PreparedStatement comando = conexionBD.prepareStatement(comandoSQL)) {
            comando.setInt(1, idAlumno);
            ResultSet resultado = comando.executeQuery();
            if (resultado.next()) {
                return resultado.getInt("minutosUsados");
            }
            return 0;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar minutos usados por alumno: " + ex.getMessage());
        }
    }

    @Override
    public Reserva guardar(GuardarReservaDTO reserva) throws PersistenciaException {
        try {
            this.transaccion = conexion.crearConexion();
            this.transaccion.setAutoCommit(false);
            int idRegistroGenerado = this.registrarReserva(reserva);
            this.transaccion.commit();
            return new Reserva(idRegistroGenerado,
                    reserva.getFechaHoraApartado(),
                    null,
                    null,
                    null,
                    reserva.getIdAlumno(),
                    reserva.getIdComputadora());

        } catch (Exception ex) {
            if (this.transaccion != null) {
                try {
                    this.transaccion.rollback();
                    System.out.println("Rollback realizado");
                } catch (SQLException x) {
                    throw new PersistenciaException("No se pudo hacer un rollback :" + ex.getMessage());
                }
            }
            throw new PersistenciaException("La transacción fue abortada: " + ex.getMessage());
        } finally {
            if (this.transaccion != null) {
                try {
                    this.transaccion.close();
                } catch (SQLException ex) {
                    throw new PersistenciaException("Error al crear la conexión:" + ex.getMessage());
                }
            }
            this.transaccion = null;
        }
    }

    @Override
    public Reserva cancelar(CancelarReservaDTO reserva) throws PersistenciaException {
        try {
            this.transaccion = this.conexion.crearConexion();
            this.transaccion.setAutoCommit(false);
            Reserva reservaD = this.consultarReservaPorID(reserva.getIdReserva());
            if (reservaD == null) {
                throw new PersistenciaException("No existe la reserva.");
            }
            this.cancelarReserva(reserva.getIdReserva());

            ComputadoraDAO computadoraDAO = new ComputadoraDAO(this.conexion);
            computadoraDAO.mostrarComputadoraComoDisponible(reservaD.getIdComputadora(),this.transaccion);
            this.transaccion.commit();
            reservaD.setFechaHoraFinal(LocalDateTime.now());
            reservaD.setTiempoUso(0);
            return reservaD;

        } catch (Exception e) {
            if (this.transaccion != null) {
                try {
                    this.transaccion.rollback();
                    System.out.println("Rollback realizado");
                } catch (SQLException x) {
                    System.out.println("No se pudo hacer un rollback");
                }
            }
            throw new PersistenciaException("La transacción fue abortada: " + e.getMessage());
        } finally {
            if (this.transaccion != null) {
                try {
                    this.transaccion.close();
                } catch (SQLException ex) {
                    throw new PersistenciaException("Error al crear la conexión:" + ex.getMessage());
                }
            }
            this.transaccion = null;
        }
    }

    @Override
    public Reserva finalizar(FinalizarReservaDTO reserva) throws PersistenciaException {
        try {
            this.transaccion = this.conexion.crearConexion();
            this.transaccion.setAutoCommit(false);
            Reserva reservaD = this.consultarReservaPorID(reserva.getIdReserva());
            if (reservaD == null) {
                throw new PersistenciaException("No existe ninguna reserva.");
            }
            this.finalizarReserva(reserva);
            ComputadoraDAO computadoraDAO = new ComputadoraDAO(this.conexion);
            computadoraDAO.mostrarComputadoraComoDisponible(reservaD.getIdComputadora(),this.transaccion);
            this.transaccion.commit();
            reservaD.setFechaHoraFinal(reserva.getFechaHoraFinal());
            reservaD.setTiempoUso(calcularTiempoUso(reservaD.getFechaHoraInicio(), reservaD.getFechaHoraApartado(), reserva.getFechaHoraFinal()));
            return reservaD;
        } catch (Exception e) {
            if (this.transaccion != null) {
                try {
                    this.transaccion.rollback();
                    System.out.println("Rollback realizado");
                } catch (SQLException x) {
                    throw new PersistenciaException("No se pudo hacer el rollback" + x.getMessage());
                }
            }
            throw new PersistenciaException("La transacción fue abortado: " + e.getMessage());
        } finally {
            if (this.transaccion != null) {
                try {
                    this.transaccion.close();
                } catch (SQLException ex) {
                    throw new PersistenciaException("Error al crear la conexión:" + ex.getMessage());
                }
            }
            this.transaccion = null;
        }
    }

    private int calcularTiempoUso(LocalDateTime fechaInicio, LocalDateTime fechaApartado, LocalDateTime fechaFinal) {
        LocalDateTime inicioReal = fechaInicio != null ? fechaInicio : fechaApartado;
        return (int) java.time.Duration.between(inicioReal, fechaFinal).toMinutes();
    }
}
