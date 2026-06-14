/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author BALAMRUSH
 */
public class ConexionBD implements IConexionBD{
    
    private final String SERVER = "127.0.0.1";
    private final String BASEDATOS = "proyectoCISCO";
    private final String URL = "jdbc:mysql://" + SERVER + "/" + BASEDATOS;
    private final String USER = "root";
    private final String PASSWORD = "Andreiy2006#";
    

    @Override
    public Connection crearConexion() throws SQLException {
        Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        return conexion;
    }
    
}
