/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dtos.ConsultarCarreraCentroComputoDTO;
import Dtos.ConsultarCentroComputoDTO;
import Dtos.ConsultarHoraInicioCentroComputoDTO;
import Dtos.ConsultarIDAlumnoCentroComputoDTO;
import Dtos.ConsultarMaquinaCentroComputoDTO;
import Dtos.ConsultarNombreAlumnoCentroComputoDTO;
import Dtos.ConsultarUnidadAcademicaCentroComputoDTO;
import Entidades.CentroComputo;
import com.sun.jdi.IntegerValue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.logging.Logger;

/**
 *
 * @author Andre
 */
public class CentroComputoDAO implements ICentroComputoDAO {

    private static final Logger LOGGER = Logger.getLogger(CentroComputoDAO.class.getName());
    private IConexionBD conexion;

    public CentroComputoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public CentroComputo consultarPorID(ConsultarIDAlumnoCentroComputoDTO dto) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                    SELECT cc.idCentroComputo,
                           cc.horaInicio,
                           cc.horaFin,
                           cc.contrasenaMaestra,
                           cc.idUnidadAcademica
                    FROM centroComputo cc
                    INNER JOIN computadora c ON cc.idCentroComputo = c.idCentroComputo
                    INNER JOIN reserva r ON c.idComputadora = r.idComputadora
                    WHERE r.idAlumno = ?
                      AND r.fechaHoraFinal IS NULL
                    """;
            PreparedStatement ps = conexion.prepareStatement(comandoSQL);
            ps.setInt(1, dto.getIdAlumno());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CentroComputo(
                        rs.getInt("idCentroComputo"),
                        rs.getTime("horaInicio"),
                        rs.getTime("horaFin"),
                        rs.getString("contrasenaMaestra"),
                        rs.getInt("idUnidadAcademica"));
            }
            return null;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar centro de cómputo por ID de alumno: " + ex.getMessage());
        }
    }

    @Override
    public CentroComputo consultarPorNombre(ConsultarNombreAlumnoCentroComputoDTO dto) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                    SELECT cc.idCentroComputo,
                           cc.horaInicio,
                           cc.horaFin,
                           cc.contrasenaMaestra,
                           cc.idUnidadAcademica
                    FROM centroComputo cc
                    INNER JOIN computadora c ON cc.idCentroComputo = c.idCentroComputo
                    INNER JOIN reserva r ON c.idComputadora = r.idComputadora
                    INNER JOIN alumno a ON r.idAlumno = a.idAlumno
                    WHERE a.nombre LIKE ?
                      AND r.fechaHoraFinal IS NULL
                    """;
            PreparedStatement ps = conexion.prepareStatement(comandoSQL);
            ps.setString(1, "%" + dto.getNombreAlumno() + "%");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CentroComputo(
                        rs.getInt("idCentroComputo"),
                        rs.getTime("horaInicio"),
                        rs.getTime("horaFin"),
                        rs.getString("contrasenaMaestra"),
                        rs.getInt("idUnidadAcademica"));
            }
            return null;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar centro de cómputo por nombre: " + ex.getMessage());
        }
    }

    @Override
    public CentroComputo consultarPorMaquina(ConsultarMaquinaCentroComputoDTO dto) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                    SELECT cc.idCentroComputo,
                           cc.horaInicio,
                           cc.horaFin,
                           cc.contrasenaMaestra,
                           cc.idUnidadAcademica
                    FROM centroComputo cc
                    INNER JOIN computadora c ON cc.idCentroComputo = c.idCentroComputo
                    WHERE c.numeroMaquina = ?
                    """;
            PreparedStatement ps = conexion.prepareStatement(comandoSQL);
            ps.setInt(1, dto.getMaquina());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CentroComputo(
                        rs.getInt("idCentroComputo"),
                        rs.getTime("horaInicio"),
                        rs.getTime("horaFin"),
                        rs.getString("contrasenaMaestra"),
                        rs.getInt("idUnidadAcademica"));
            }
            return null;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar centro de cómputo por máquina: " + ex.getMessage());
        }
    }

    @Override
    public CentroComputo consultarPorCarrera(ConsultarCarreraCentroComputoDTO dto) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                    SELECT cc.idCentroComputo,
                           cc.horaInicio,
                           cc.horaFin,
                           cc.contrasenaMaestra,
                           cc.idUnidadAcademica
                    FROM centroComputo cc
                    INNER JOIN computadora c ON cc.idCentroComputo = c.idCentroComputo
                    INNER JOIN reserva r ON c.idComputadora = r.idComputadora
                    INNER JOIN alumno a ON r.idAlumno = a.idAlumno
                    WHERE a.idCarrera = ?
                      AND r.fechaHoraFinal IS NULL
                    """;
            PreparedStatement ps = conexion.prepareStatement(comandoSQL);
            ps.setString(1, dto.getCarrera());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CentroComputo(
                        rs.getInt("idCentroComputo"),
                        rs.getTime("horaInicio"),
                        rs.getTime("horaFin"),
                        rs.getString("contrasenaMaestra"),
                        rs.getInt("idUnidadAcademica"));
            }
            return null;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar centro de cómputo por carrera: " + ex.getMessage());
        }
    }

    @Override
    public CentroComputo consultarPorUnidadAcademica(ConsultarUnidadAcademicaCentroComputoDTO dto) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                    SELECT cc.idCentroComputo,
                           cc.horaInicio,
                           cc.horaFin,
                           cc.contrasenaMaestra,
                           cc.idUnidadAcademica
                    FROM centroComputo cc
                    WHERE cc.idUnidadAcademica = ?
                    """;
            PreparedStatement ps = conexion.prepareStatement(comandoSQL);
            ps.setString(1, dto.getUnidadAcademica());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CentroComputo(
                        rs.getInt("idCentroComputo"),
                        rs.getTime("horaInicio"),
                        rs.getTime("horaFin"),
                        rs.getString("contrasenaMaestra"),
                        rs.getInt("idUnidadAcademica"));
            }
            return null;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar centro de cómputo por unidad académica: " + ex.getMessage());
        }
    }

    @Override
    public CentroComputo consultarPorCentroComputo(ConsultarCentroComputoDTO dto) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                    SELECT cc.idCentroComputo,
                           cc.horaInicio,
                           cc.horaFin,
                           cc.contrasenaMaestra,
                           cc.idUnidadAcademica
                    FROM centroComputo cc
                    WHERE cc.idCentroComputo = ?
                    """;
            PreparedStatement ps = conexion.prepareStatement(comandoSQL);
            ps.setString(1, dto.getCentroDeComputo());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CentroComputo(
                        rs.getInt("idCentroComputo"),
                        rs.getTime("horaInicio"),
                        rs.getTime("horaFin"),
                        rs.getString("contrasenaMaestra"),
                        rs.getInt("idUnidadAcademica"));
            }
            return null;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar centro de cómputo: " + ex.getMessage());
        }
    }

    @Override
    public CentroComputo consultarPorHoraInicio(ConsultarHoraInicioCentroComputoDTO dto) throws PersistenciaException {
        try (Connection conexion = this.conexion.crearConexion()) {
            String comandoSQL = """
                    SELECT cc.idCentroComputo,
                           cc.horaInicio,
                           cc.horaFin,
                           cc.contrasenaMaestra,
                           cc.idUnidadAcademica
                    FROM centroComputo cc
                    WHERE cc.horaInicio = ?
                    """;
            PreparedStatement ps = conexion.prepareStatement(comandoSQL);
            ps.setTime(1, Time.valueOf(dto.getHoraInicio()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CentroComputo(
                        rs.getInt("idCentroComputo"),
                        rs.getTime("horaInicio"),
                        rs.getTime("horaFin"),
                        rs.getString("contrasenaMaestra"),
                        rs.getInt("idUnidadAcademica"));
            }
            return null;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al consultar centro de cómputo por hora inicio: " + ex.getMessage());
        }
    }

    @Override
    public CentroComputo consultarPorIDAlumno(ConsultarIDAlumnoCentroComputoDTO id) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public CentroComputo consultarPorNombreAlumno(ConsultarNombreAlumnoCentroComputoDTO nombre) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

