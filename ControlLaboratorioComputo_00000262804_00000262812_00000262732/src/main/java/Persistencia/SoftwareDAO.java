/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Dtos.ObtenerCatalogoSoftwareDTO;
import Entidades.Computadora;
import Entidades.Software;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andre
 */
public class SoftwareDAO implements ISoftwareDAO {

    private IConexionBD conexion;

    public SoftwareDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<Software> obtenerCatalogoSoftware(ObtenerCatalogoSoftwareDTO obtenerCatalogo) throws PersistenciaException {

        List<Software> catalogo = new ArrayList<>();
        try (Connection conexion = this.conexion.crearConexion()) {

            String comandoSQL = """
            SELECT s.idSoftware,
                   s.nombre
            FROM software s
            INNER JOIN computadora_software cs
                ON s.idSoftware = cs.idSoftware
            WHERE cs.idComputadora = ?;
            """;

            PreparedStatement statement = conexion.prepareStatement(comandoSQL);
            statement.setInt(1, obtenerCatalogo.getIdComputadora()); // ← usa tu DTO

            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                catalogo.add(new Software(
                        resultado.getInt("idSoftware"),
                        resultado.getString("nombre")
                ));
            }
            return catalogo;

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener el catalogo de software: " + ex.getMessage());
        }
    }

}
