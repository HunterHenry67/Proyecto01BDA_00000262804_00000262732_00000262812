/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dtos.ReservaDTO;
import Entidades.Reserva;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Andre
 */
public class ReservaDAO implements IReservaDAO{

    private static final Logger LOGGER = Logger.getLogger(ReservaDAO.class.getName());
    
    private IConexionBD conexion;
    private Connection transaccion;
    
    public ReservaDAO(IConexionBD conexion){
        this.conexion = conexion;
    }
    
    @Override
    public Reserva obtenerReservaActiva(int idComputadora) {
        String comandoSQL = """
            
            JOIN alumno a ON r.idAlumno = a.idAlumno
            WHERE r.idComputadora = ?
              AND r.fechaHoraFinal IS NULL
            """;
        
        
    }

    @Override
    public Reserva registrarReserva(Reserva reserva) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Reserva consultarResrevaActivaPorAlumno(int idAlumno) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Reserva consultarReservaActivaPorComputadora(int idComputadora) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Reserva> consultar(String filtro) throws PersistenciaException {
        List<Reserva> listaFiltroReserva = new ArrayList<>();
        try(Connection conexion = this.conexion.crearConexion()){
            String comandoSQL = """
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
                                    INNER JOIN computadora c ON r.idComputadora = c.idComputadora
                                    WHERE DATE_FORMAT(r.fechaHoraApartado, '%Y-%m-%d %H:%i:%s') LIKE ?
                                       OR DATE_FORMAT(r.fechaHoraInicio, '%Y-%m-%d %H:%i:%s') LIKE ?
                                       OR DATE_FORMAT(r.fechaHoraFinal, '%Y-%m-%d %H:%i:%s') LIKE ?
                                       OR CONCAT(a.nombre, ' ', a.apellidoPaterno, ' ', a.apellidoMaterno) LIKE ?
                                       OR c.nombre LIKE ?;
                                """;
            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            String busquedaFiltro = "%" + filtro + "%";
            statement.setString(1, busquedaFiltro);
            statement.setString(2, busquedaFiltro);
            statement.setString(3, busquedaFiltro);
            statement.setString(4, busquedaFiltro);
            statement.setString(5, busquedaFiltro);
            ResultSet resultado = statement.executeQuery();
            while(resultado.next()){
                Timestamp fechaInicio = resultado.getTimestamp("fechaHoraInicio");
                Timestamp fechaFinal = resultado.getTimestamp("fechaHoraFinal");
                listaFiltroReserva.add(new Reserva(resultado.getInt("idReserva"),
                                                   resultado.getTimestamp("fechaHoraApartado").toLocalDateTime(),
                                                   fechaInicio != null ? fechaInicio.toLocalDateTime(): null,
                                                   fechaFinal != null ? fechaFinal.toLocalDateTime(): null,
                                                   resultado.getObject("tiempoUso") != null ? resultado.getInt("tiempoUso"): null,
                                                   resultado.getInt("idAlumno"),
                                                   resultado.getInt("idComputadora")
                                                   ));
            }
            return listaFiltroReserva;
        }catch(SQLException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar las reservas por filtro: " + ex.getMessage());
        }
    }

    @Override
    public List<Reserva> consultarReservasActivas() throws PersistenciaException {
        List<Reserva> listaReservasActivas = new ArrayList<>();
        try(Connection conexion = this.conexion.crearConexion()){
            
        }catch(SQLException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar las reservas activas: "+ex.getMessage());
        }
    }

    @Override
    public void finalizarBloqueo(int idReserva, LocalDateTime fechaFinalizacion) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void cancelarBloqueo(int idReserva) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int consultarMinutosUsadosPorAlumno(int idAlumno) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
