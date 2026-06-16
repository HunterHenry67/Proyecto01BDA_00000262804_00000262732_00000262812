/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package enriquemadridalvarez;

import PresentacionProgramaApartado.frmPantallaPrincipal;
import javax.swing.SwingUtilities;

/**
 *
 * @author BALAMRUSH
 */
public class MainApartado {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frmPantallaPrincipal pantalla = new frmPantallaPrincipal();
            pantalla.setVisible(true);
        });
    }
    
}
