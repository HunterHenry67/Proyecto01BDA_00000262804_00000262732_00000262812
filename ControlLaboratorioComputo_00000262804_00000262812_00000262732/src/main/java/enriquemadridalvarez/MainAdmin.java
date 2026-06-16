/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package enriquemadridalvarez;

import PresentacionProgramaAdministrador.frmMenuGestion;
import javax.swing.SwingUtilities;

/**
 *
 * @author BALAMRUSH
 */
public class MainAdmin {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frmMenuGestion pantalla = new frmMenuGestion();
            pantalla.setVisible(true);
        });
    }
  
}
