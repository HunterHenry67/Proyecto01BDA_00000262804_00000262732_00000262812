package PresentacionProgramaAdministrador;

import Entidades.Computadora;
import Negocio.ICentroComputoBO;
import Negocio.NegocioException;
import Persistencia.ConexionBD;
import Persistencia.IConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class frmGestionEquipo extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(frmGestionEquipo.class.getName());

    private final ICentroComputoBO centroComputoBO;
    private final IConexionBD conexionBD;
    private final Map<Integer, Computadora> computadorasPorNumero = new HashMap<>();
    private final Map<JCheckBox, Integer> numeroPorCheckbox = new HashMap<>();
    private boolean actualizandoChecks = false;

    public frmGestionEquipo() {
        ControlFormsProgramaAdminstrador ctrl = ControlFormsProgramaAdminstrador.getInstance();
        this.centroComputoBO = ctrl.getCentroComputoBO();
        this.conexionBD = new ConexionBD();

        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        configurarCheckboxes();
        cargarEstatusComputadoras();
    }

    private void configurarCheckboxes() {
        registrarCheckbox(chk1, 1);
        registrarCheckbox(chk2, 2);
        registrarCheckbox(chk3, 3);
        registrarCheckbox(chk4, 4);
        registrarCheckbox(chk5, 5);
        registrarCheckbox(chk6, 6);
        registrarCheckbox(chk7, 7);
        registrarCheckbox(chk8, 8);
        registrarCheckbox(chk9, 9);
    }

    private void registrarCheckbox(JCheckBox checkbox, int numeroMaquina) {
        numeroPorCheckbox.put(checkbox, numeroMaquina);
        checkbox.addActionListener(e -> procesarCambioEstatus((JCheckBox) e.getSource()));
    }

    private void cargarEstatusComputadoras() {
        computadorasPorNumero.clear();
        actualizandoChecks = true;
        try {
            for (Map.Entry<JCheckBox, Integer> entry : numeroPorCheckbox.entrySet()) {
                Computadora computadora = consultarComputadoraPorNumero(entry.getValue());
                if (computadora != null) {
                    computadorasPorNumero.put(entry.getValue(), computadora);
                }
                entry.getKey().setSelected(computadora != null && computadora.isEstatus());
                entry.getKey().setEnabled(computadora != null);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this,
                    "Error al consultar computadoras:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            actualizandoChecks = false;
        }
    }

    private Computadora consultarComputadoraPorNumero(int numeroMaquina) throws SQLException {
        String sql = """
                     SELECT idComputadora,
                            numeroMaquina,
                            direccionIP,
                            estatus,
                            tipo,
                            idCentroComputo
                     FROM computadora
                     WHERE numeroMaquina = ?
                     """;
        try (Connection conexion = conexionBD.crearConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, numeroMaquina);
            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    Computadora computadora = new Computadora();
                    computadora.setIdComputadora(resultado.getInt("idComputadora"));
                    computadora.setNumeroMaquina(resultado.getInt("numeroMaquina"));
                    computadora.setIp(resultado.getString("direccionIP"));
                    computadora.setEstatus(resultado.getBoolean("estatus"));
                    computadora.setTipo(resultado.getString("tipo"));
                    computadora.setIdCentroComputo(resultado.getInt("idCentroComputo"));
                    return computadora;
                }
            }
        }
        return null;
    }

    private void actualizarEstatusComputadora(Integer idComputadora, boolean estatus) throws SQLException {
        String sql = """
                     UPDATE computadora
                     SET estatus = ?
                     WHERE idComputadora = ?
                     """;
        try (Connection conexion = conexionBD.crearConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setBoolean(1, estatus);
            statement.setInt(2, idComputadora);
            statement.executeUpdate();
        }
    }

    private void procesarCambioEstatus(JCheckBox checkbox) {
        if (actualizandoChecks) {
            return;
        }

        Integer numeroMaquina = numeroPorCheckbox.get(checkbox);
        Computadora computadora = computadorasPorNumero.get(numeroMaquina);
        boolean nuevoEstatus = checkbox.isSelected();
        if (computadora == null) {
            checkbox.setSelected(!nuevoEstatus);
            JOptionPane.showMessageDialog(this, "No se encontro la maquina " + numeroMaquina + ".",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!solicitarContrasenaMaestra()) {
            checkbox.setSelected(!nuevoEstatus);
            return;
        }

        try {
            actualizarEstatusComputadora(computadora.getIdComputadora(), nuevoEstatus);
            computadora.setEstatus(nuevoEstatus);
            JOptionPane.showMessageDialog(this,
                    nuevoEstatus
                            ? "Maquina " + numeroMaquina + " desbloqueada correctamente."
                            : "Maquina " + numeroMaquina + " bloqueada correctamente.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            checkbox.setSelected(!nuevoEstatus);
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar la maquina:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean solicitarContrasenaMaestra() {
        JPasswordField campoContrasena = new JPasswordField();
        int opcion = JOptionPane.showConfirmDialog(this, campoContrasena,
                "Ingrese la contraseña maestra", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return false;
        }

        char[] contrasenaChars = campoContrasena.getPassword();
        String contrasena = new String(contrasenaChars);
        Arrays.fill(contrasenaChars, '\0');

        try {
            boolean valida = centroComputoBO.validarContraseniaMaestra(contrasena);
            if (!valida) {
                JOptionPane.showMessageDialog(this, "Contraseña maestra incorrecta.",
                        "Acceso denegado", JOptionPane.ERROR_MESSAGE);
            }
            return valida;
        } catch (NegocioException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Error al validar contraseña maestra:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        cboMaquina6 = new javax.swing.JCheckBox();
        txtTitulo = new javax.swing.JLabel();
        txtSubtitulo = new javax.swing.JLabel();
        lblMaquina2 = new javax.swing.JLabel();
        lblMaquina1 = new javax.swing.JLabel();
        lblMaquina4 = new javax.swing.JLabel();
        lblMaquina3 = new javax.swing.JLabel();
        lblMaquina7 = new javax.swing.JLabel();
        lblMaquina6 = new javax.swing.JLabel();
        lblMaquina9 = new javax.swing.JLabel();
        lblMaquina8 = new javax.swing.JLabel();
        lblMaquina5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        chk1 = new javax.swing.JCheckBox();
        chk2 = new javax.swing.JCheckBox();
        chk3 = new javax.swing.JCheckBox();
        chk4 = new javax.swing.JCheckBox();
        chk5 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        chk8 = new javax.swing.JCheckBox();
        chk9 = new javax.swing.JCheckBox();
        chk7 = new javax.swing.JCheckBox();
        chk6 = new javax.swing.JCheckBox();

        jButton2.setBackground(new java.awt.Color(45, 45, 45));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Gestión de Monitoreo de Equipo");
        jButton2.setBorderPainted(false);
        jButton2.setFocusPainted(false);
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Maquina 1");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("Maquina 1");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setText("Maquina 3");

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        cboMaquina6.addActionListener(this::cboMaquina6ActionPerformed);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtTitulo.setText("Panel de Administración");

        txtSubtitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        txtSubtitulo.setForeground(new java.awt.Color(153, 153, 153));
        txtSubtitulo.setText("Gestión de Equipo");

        lblMaquina2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMaquina2.setText("Maquina 2");

        lblMaquina1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMaquina1.setText("Maquina 1");

        lblMaquina4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMaquina4.setText("Maquina 4");

        lblMaquina3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMaquina3.setText("Maquina 3");

        lblMaquina7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMaquina7.setText("Maquina 7");

        lblMaquina6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMaquina6.setText("Maquina 6");

        lblMaquina9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMaquina9.setText("Maquina 9");

        lblMaquina8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMaquina8.setText("Maquina 8");

        lblMaquina5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMaquina5.setText("Maquina 5");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chk1)
                    .addComponent(chk2)
                    .addComponent(chk3)
                    .addComponent(chk4))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chk1)
                .addGap(30, 30, 30)
                .addComponent(chk2)
                .addGap(31, 31, 31)
                .addComponent(chk3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(chk4)
                .addContainerGap())
        );

        chk5.addActionListener(this::chk5ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 713, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        chk8.addActionListener(this::chk8ActionPerformed);

        chk9.addActionListener(this::chk9ActionPerformed);

        chk7.addActionListener(this::chk7ActionPerformed);

        chk6.addActionListener(this::chk6ActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chk8)
                    .addComponent(chk7)
                    .addComponent(chk6)
                    .addComponent(chk9))
                .addGap(0, 103, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(58, Short.MAX_VALUE)
                .addComponent(chk6)
                .addGap(32, 32, 32)
                .addComponent(chk7)
                .addGap(29, 29, 29)
                .addComponent(chk8)
                .addGap(26, 26, 26)
                .addComponent(chk9)
                .addGap(73, 73, 73))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(274, 274, 274)
                                .addComponent(lblMaquina5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chk5))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(125, 125, 125)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblMaquina4)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblMaquina3)
                                        .addComponent(lblMaquina2)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblMaquina9, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblMaquina7, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblMaquina6, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblMaquina8, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(243, 243, 243)
                                .addComponent(txtSubtitulo))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(256, 256, 256)
                                .addComponent(txtTitulo)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(126, 126, 126)
                    .addComponent(lblMaquina1)
                    .addContainerGap(479, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSubtitulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblMaquina6)
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblMaquina2)
                                    .addComponent(lblMaquina7))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblMaquina3)
                                    .addComponent(lblMaquina8))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblMaquina4))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblMaquina9))))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblMaquina5)
                            .addComponent(chk5))
                        .addGap(87, 87, 87))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(112, 112, 112)
                    .addComponent(lblMaquina1)
                    .addContainerGap(279, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void chk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chk5ActionPerformed

    private void cboMaquina6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMaquina6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboMaquina6ActionPerformed

    private void chk7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chk7ActionPerformed

    private void chk8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chk8ActionPerformed

    private void chk9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chk9ActionPerformed

    private void chk6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chk6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new frmGestionEquipo().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cboMaquina6;
    private javax.swing.JCheckBox chk1;
    private javax.swing.JCheckBox chk2;
    private javax.swing.JCheckBox chk3;
    private javax.swing.JCheckBox chk4;
    private javax.swing.JCheckBox chk5;
    private javax.swing.JCheckBox chk6;
    private javax.swing.JCheckBox chk7;
    private javax.swing.JCheckBox chk8;
    private javax.swing.JCheckBox chk9;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblMaquina1;
    private javax.swing.JLabel lblMaquina2;
    private javax.swing.JLabel lblMaquina3;
    private javax.swing.JLabel lblMaquina4;
    private javax.swing.JLabel lblMaquina5;
    private javax.swing.JLabel lblMaquina6;
    private javax.swing.JLabel lblMaquina7;
    private javax.swing.JLabel lblMaquina8;
    private javax.swing.JLabel lblMaquina9;
    private javax.swing.JLabel txtSubtitulo;
    private javax.swing.JLabel txtTitulo;
    // End of variables declaration//GEN-END:variables
}
