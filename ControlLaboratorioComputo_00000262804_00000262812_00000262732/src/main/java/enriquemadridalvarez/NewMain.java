/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package enriquemadridalvarez;

import Entidades.Computadora;
import Negocio.ComputadoraBO;
import Negocio.IComputadoraBO;
import Persistencia.*;
import PresentacionProgramaApartado.frmPantallaPrincipal;
import PresentacionProgramaBloqueo.frmPantallaBloqueo;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Pruebas manuales de todos los métodos DAO. Ajusta las constantes según los
 * datos que tengas en tu BD.
 */
public class NewMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                IConexionBD conexion = new ConexionBD();
                IComputadoraBO computadoraBO = new ComputadoraBO(new ComputadoraDAO(conexion));
                String direccionIP = obtenerDireccionIP();
                Computadora computadoraActual = computadoraBO.obtenerPCPorIP(direccionIP);
                if (computadoraActual == null) {
                    JOptionPane.showMessageDialog(
                            null,
                            "No existe una computadora registrada con la IP:\n" + direccionIP,
                            "Computadora no registrada",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                String tipo = computadoraActual.getTipo();
                if (tipo.equalsIgnoreCase("Kiosco Apartado")) {
                    frmPantallaPrincipal pantallaApartado = new frmPantallaPrincipal();
                    pantallaApartado.setVisible(true);
                } else if (tipo.equalsIgnoreCase("Servicio Alumno")) {
                    frmPantallaBloqueo pantallaBloqueo = new frmPantallaBloqueo(computadoraActual);
                    pantallaBloqueo.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Tipo de computadora no reconocido:\n" + tipo,
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Error al iniciar el sistema.\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private static String obtenerDireccionIP() throws UnknownHostException {
        InetAddress equipoLocal = InetAddress.getLocalHost();
        return equipoLocal.getHostAddress();
    }

}
