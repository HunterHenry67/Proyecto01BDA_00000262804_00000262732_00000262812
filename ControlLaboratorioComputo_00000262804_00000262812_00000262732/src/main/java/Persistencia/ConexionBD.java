/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Implementacion de la interfaz para poder establecer una conexion con la base de datos
 *esta clase maneja las credenciales de acceso a esta BD
 * @author BALAMRUSH
 */
public class ConexionBD implements IConexionBD{
    
    private final String SERVER = "127.0.0.1";
    private final String BASEDATOS = "proyectocisco";
    private final String URL = "jdbc:mysql://" + SERVER + "/" + BASEDATOS;
    private final String USER = "root";
    private final String PASSWORD = "1234";
    

    /**
     * Establece una conexion en la BD usando las credenciales puestas en la clase
     * 
     * @return un objeto link que nos permite hacer operaciones con una base de datos
     * @throws SQLException la lanzaria en caso de haber un error en la conexion o credenciales
     */
    @Override
    public Connection crearConexion() throws SQLException {
        Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        return conexion;
    }
    
}
