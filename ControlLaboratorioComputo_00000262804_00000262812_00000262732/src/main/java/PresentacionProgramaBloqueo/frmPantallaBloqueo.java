/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PresentacionProgramaBloqueo;

import Dtos.CancelarReservaDTO;
import Entidades.Alumno;
import Entidades.Reserva;
import Negocio.IAlumnoBO;
import Negocio.IReservaBO;
import Negocio.NegocioException;
import Persistencia.PersistenciaException;
import javax.swing.JFrame;

/**
 *
 * @author user
 */
public class frmPantallaBloqueo extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmPantallaBloqueo.class.getName());
    private IAlumnoBO alumnoNegocio;
    private Integer idAlumnoReservaActual = null;
    private IReservaBO reservaNegocio; 
    private Integer idDeEstaMaquinaFisica;
    private Integer idReservaActual = null;
    private java.time.LocalDateTime horaDeApartado = null;
    
    private javax.swing.Timer timerUso;
    private int segundosUso = 0;
    
    private javax.swing.Timer timerActualizacion;
    private int segundosContador = 30;
    
   
    public frmPantallaBloqueo() throws PersistenciaException { 
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        timerUso = new javax.swing.Timer(1000, e -> {
        segundosUso++;
        });
        timerUso.start();
        
        this.idDeEstaMaquinaFisica = 1;
        lblNumeroPC.setText(String.format("%02d", idDeEstaMaquinaFisica));
        
        try{
            Persistencia.IConexionBD conexion = new Persistencia.ConexionBD();
            this.reservaNegocio = new Negocio.ReservaBO(new Persistencia.ReservaDAO(conexion));
            this.alumnoNegocio = new Negocio.AlumnoBO(new Persistencia.AlumnoDAO(conexion));
        
        }catch (Exception ex) {
            System.getLogger(frmPantallaBloqueo.class.getName()).log(System.Logger.Level.ERROR, "Error al conectar a BD", ex);
        }
        limpiarCamposADisponible();
        consultarBaseDeDatos();
        this.iniciarMonitoreoPC();
    }
    
    private void iniciarMonitoreoPC() {
        timerActualizacion = new javax.swing.Timer(1000, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                segundosContador--;
                
                lblTimerContador.setText(segundosContador + " segundos");
                
                if (horaDeApartado != null) {
                    java.time.Duration tiempoTranscurrido = java.time.Duration.between(horaDeApartado, java.time.LocalDateTime.now());
                    
                    if (tiempoTranscurrido.toSeconds() >= 600) {
                        try {
                             CancelarReservaDTO dto = new CancelarReservaDTO(idReservaActual);
                             reservaNegocio.cancelar(dto);
                            
                            javax.swing.JOptionPane.showMessageDialog(null, 
                                "Tiempo límite de espera agotado (10 min).\nLa reservación ha sido cancelada.", 
                                "Tiempo Agotado", javax.swing.JOptionPane.WARNING_MESSAGE);
                            
                            limpiarCamposADisponible();
                            
                        } catch (Exception ex) {
                            System.getLogger(frmPantallaBloqueo.class.getName());
                        }
                    }
                }
                
                if (segundosContador <= 0) {
                    segundosContador = 30; 
                    consultarBaseDeDatos();
                }
            }
        });
        timerActualizacion.start();
    }
    
    private void consultarBaseDeDatos() {
        try {          
            Reserva reservaActiva = reservaNegocio.consultarReservaActivaPorComputadora(this.idDeEstaMaquinaFisica);       
            if (reservaActiva == null) {
                limpiarCamposADisponible();
            }       
            else {
                this.idAlumnoReservaActual = reservaActiva.getIdAlumno();
                this.idReservaActual = reservaActiva.getIdReserva();
                
                if (this.horaDeApartado == null) {
                    this.horaDeApartado = java.time.LocalDateTime.now(); 
                }             
                this.idAlumnoReservaActual = reservaActiva.getIdAlumno();
                this.idReservaActual = reservaActiva.getIdReserva();
                try {
                    Alumno alumno = alumnoNegocio.consultarAlumnoPorID(this.idAlumnoReservaActual);
                    if (alumno != null) {
                        lblEstadoAlumno.setText(alumno.getNombres() + " " + alumno.getApellidoPaterno());
                    } else {
                        lblEstadoAlumno.setText("RESERVADO");
                    }
                } catch (NegocioException ex) {
                    lblEstadoAlumno.setText("RESERVADO");
                }               
                lblNumeroPC.setForeground(java.awt.Color.RED); 
                JTextContrasena.setVisible(true);
                btnIngresar.setVisible(true);
                lblContrasena.setVisible(true);
            }
            
        } catch (NegocioException ex) {
            limpiarCamposADisponible();
        } catch (Exception e) {
            System.getLogger(frmPantallaBloqueo.class.getName()).log(System.Logger.Level.ERROR, "Error crítico", e);
        }
    }
    
    private void limpiarCamposADisponible() {
        this.idAlumnoReservaActual = null;
        this.idReservaActual = null;
        this.horaDeApartado = null;
        
        lblEstadoAlumno.setText("DISPONIBLE");
        lblNumeroPC.setForeground(java.awt.Color.GREEN); 
        
        JTextContrasena.setText("");
        JTextContrasena.setVisible(false);
        btnIngresar.setVisible(false);
        lblContrasena.setVisible(false);
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblNumeroPC = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblPCApartada = new javax.swing.JLabel();
        lblEstadoAlumno = new javax.swing.JLabel();
        lblIniciaSesion = new javax.swing.JLabel();
        lblUnidadAcademica = new javax.swing.JLabel();
        lblLaboratorio = new javax.swing.JLabel();
        lblNombreLab = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lblTiempoAct = new javax.swing.JLabel();
        lblTimerContador = new javax.swing.JLabel();
        lblTextoSegundos = new javax.swing.JLabel();
        lblContrasena = new javax.swing.JLabel();
        JTextContrasena = new javax.swing.JTextField();
        btnIngresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblTitulo.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        lblTitulo.setText("Bloqueador de PC");
        lblTitulo.setName("lblTitulo"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 41;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 218, 0, 0);
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        lblNumeroPC.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 100)); // NOI18N
        lblNumeroPC.setForeground(new java.awt.Color(0, 204, 51));
        lblNumeroPC.setText("XX");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(lblNumeroPC)
                .addContainerGap(77, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblNumeroPC, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 71;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        getContentPane().add(jPanel2, gridBagConstraints);

        lblPCApartada.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        lblPCApartada.setText("Computadora Apartada Por: ");

        lblEstadoAlumno.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        lblEstadoAlumno.setText("DISPONIBLE");

        lblIniciaSesion.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        lblIniciaSesion.setText("INICIA SESIÓN");

        lblUnidadAcademica.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblUnidadAcademica.setText("Unidad Académica: ITSON Nainari");

        lblLaboratorio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblLaboratorio.setText("Laboratorio:");

        lblNombreLab.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNombreLab.setText("CISCO");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblPCApartada)
                            .addComponent(lblEstadoAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblIniciaSesion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUnidadAcademica)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lblLaboratorio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNombreLab, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(lblPCApartada)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblEstadoAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84)
                .addComponent(lblIniciaSesion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUnidadAcademica, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLaboratorio)
                    .addComponent(lblNombreLab))
                .addGap(0, 89, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 11;
        gridBagConstraints.ipady = 89;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 0, 0, 0);
        getContentPane().add(jPanel3, gridBagConstraints);

        lblTiempoAct.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        lblTiempoAct.setText("Tiempo de Actualización: ");

        lblTimerContador.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTimerContador.setText("30 ");

        lblTextoSegundos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTextoSegundos.setText("segundos");

        lblContrasena.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        lblContrasena.setText("Contraseña:");

        JTextContrasena.addActionListener(this::JTextContrasenaActionPerformed);

        btnIngresar.setBackground(new java.awt.Color(0, 0, 0));
        btnIngresar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnIngresar.setForeground(new java.awt.Color(255, 255, 255));
        btnIngresar.setText("Ingresar");
        btnIngresar.addActionListener(this::btnIngresarActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(lblTiempoAct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(102, 102, 102)
                                .addComponent(lblTimerContador)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTextoSegundos))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblContrasena)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JTextContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnIngresar)
                .addGap(51, 51, 51))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTiempoAct)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTimerContador)
                    .addComponent(lblTextoSegundos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblContrasena)
                    .addComponent(JTextContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnIngresar)
                .addGap(45, 45, 45))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = 158;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 0, 6);
        getContentPane().add(jPanel5, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JTextContrasenaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTextContrasenaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTextContrasenaActionPerformed

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
        // TODO add your handling code here:
        
        try {
            if (this.idAlumnoReservaActual == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Esta computadora está DISPONIBLE, debes apartala primero para poder iniciar sesión", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return; 
            }

            String contrasena = JTextContrasena.getText();
            
            Alumno alumnoLogueado = alumnoNegocio.validarCredenciales(this.idAlumnoReservaActual, contrasena);
            
            javax.swing.JOptionPane.showMessageDialog(this, "¡Bienvenido, " + alumnoLogueado.getNombres() + "!", "Ingreso válido", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            frmPantallaWindows relojWidget = new frmPantallaWindows(alumnoLogueado.getNombres(), this.idReservaActual);
            relojWidget.setVisible(true);
            this.dispose(); 
            
        } catch (NegocioException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Dato Incorrecto", javax.swing.JOptionPane.ERROR_MESSAGE);
            
            JTextContrasena.setText(""); 
        }
    }//GEN-LAST:event_btnIngresarActionPerformed

   

    public static void main(String args[]) {    
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>

        java.awt.EventQueue.invokeLater(() -> {
            try {
                new frmPantallaBloqueo().setVisible(true);
            } catch (PersistenciaException ex) {
                System.getLogger(frmPantallaBloqueo.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
    }
    
    public void configurarEstadoPC(boolean estaDisponible, String nombreAlumno) {
        if (estaDisponible) {
            lblEstadoAlumno.setText("DISPONIBLE");
            lblNumeroPC.setForeground(java.awt.Color.GREEN);
            
            JTextContrasena.setVisible(true);
            btnIngresar.setVisible(true);
            lblContrasena.setVisible(true); 
        } else {
            lblEstadoAlumno.setText(nombreAlumno);
            lblNumeroPC.setForeground(java.awt.Color.RED); 
            
            JTextContrasena.setVisible(true);
            btnIngresar.setVisible(true);
            lblContrasena.setVisible(true);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField JTextContrasena;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lblContrasena;
    private javax.swing.JLabel lblEstadoAlumno;
    private javax.swing.JLabel lblIniciaSesion;
    private javax.swing.JLabel lblLaboratorio;
    private javax.swing.JLabel lblNombreLab;
    private javax.swing.JLabel lblNumeroPC;
    private javax.swing.JLabel lblPCApartada;
    private javax.swing.JLabel lblTextoSegundos;
    private javax.swing.JLabel lblTiempoAct;
    private javax.swing.JLabel lblTimerContador;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUnidadAcademica;
    // End of variables declaration//GEN-END:variables
}
