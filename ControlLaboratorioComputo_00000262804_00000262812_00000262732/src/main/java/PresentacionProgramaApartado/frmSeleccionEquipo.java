/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PresentacionProgramaApartado;

import Entidades.Alumno;
import Entidades.Reserva;
import Negocio.ComputadoraBO;
import Negocio.IComputadoraBO;
import Negocio.IReservaBO;
import Negocio.NegocioException;
import Negocio.ReservaBO;
import Persistencia.ComputadoraDAO;
import Persistencia.ConexionBD;
import Persistencia.ReservaDAO;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

/**
 *
 * @author BALAMRUSH
 */
public class frmSeleccionEquipo extends javax.swing.JFrame {

    private Alumno alumno;
    private IComputadoraBO computadoraBO;
    private IReservaBO reservaBO;

    public frmSeleccionEquipo() {
        initComponents();
        this.prepararPantalla();
        this.inicializarNegocio();
        this.configurarPanelComputadoras();
        this.cargarComputadoras();
    }

    public frmSeleccionEquipo(Alumno alumno) {
        initComponents();
        this.alumno = alumno;
        this.prepararPantalla();
        this.inicializarNegocio();
        this.configurarPanelComputadoras();
        this.cargarComputadoras();
    }

    private void prepararPantalla() {
        this.setSize(1200, 750);
        this.setLocationRelativeTo(null);
        this.pnlComputadoras.setPreferredSize(new java.awt.Dimension(1050, 550));
    }

    private void inicializarNegocio() {
        ConexionBD conexion = new ConexionBD();
        this.computadoraBO = new ComputadoraBO(new ComputadoraDAO(conexion));
        this.reservaBO = new ReservaBO(new ReservaDAO(conexion));
    }

    private void configurarPanelComputadoras() {
        pnlComputadoras.setLayout(new GridLayout(3, 3, 100, 70));
        pnlComputadoras.setBackground(new Color(220, 220, 220));
    }


    private void cargarComputadoras() {
        pnlComputadoras.removeAll();
        for (int idComputadora = 1; idComputadora <= 9; idComputadora++) {         
            boolean bloqueada = this.computadoraBloqueada(idComputadora);
            boolean apartada = this.computadoraApartada(idComputadora);
            this.agregarComputadora(idComputadora, apartada, bloqueada);
        }
        pnlComputadoras.revalidate();
        pnlComputadoras.repaint();
    }
    
    private boolean computadoraBloqueada(Integer idComputadora) {
        try {
            this.computadoraBO.validarComputadoraDisponible(idComputadora);
            return false;
        } catch (NegocioException ex) {
            return true;
        }
    }

    private boolean computadoraApartada(Integer idComputadora) {
        try {
            List reservasActivas = this.reservaBO.consultarReservasActivas();
            for (Object objeto : reservasActivas) {
                Reserva reserva = (Reserva) objeto;
                if (reserva.getIdComputadora() != null&& reserva.getIdComputadora().equals(idComputadora)) {
                    return true;
                }
            }
            return false;
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog( this,ex.getMessage(), "Error al consultar reservas activas",JOptionPane.ERROR_MESSAGE );
            return false;
        }
    }

    private void agregarComputadora(Integer idComputadora, boolean apartada, boolean bloqueada) {
        JButton btnComputadora = new JButton();

        String numeroTexto = String.format("%02d", idComputadora);

        btnComputadora.setText(numeroTexto);
        btnComputadora.setFont(new Font("Arial", Font.BOLD, 24));
        btnComputadora.setFocusPainted(false);
        btnComputadora.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnComputadora.setBackground(new Color(230, 230, 230));
        btnComputadora.setOpaque(true);

        if (bloqueada) {
            btnComputadora.setBorder(new LineBorder(new Color(90, 90, 90), 4, true));
            btnComputadora.setToolTipText("Computadora bloqueada o deshabilitada");
        } else if (apartada) {
            btnComputadora.setBorder(new LineBorder(new Color(190, 0, 0), 4, true));
            btnComputadora.setToolTipText("Computadora apartada");
        } else {
            btnComputadora.setBorder(new LineBorder(new Color(0, 170, 90), 4, true));
            btnComputadora.setToolTipText("Computadora disponible");
        }
        btnComputadora.addActionListener(e -> {
            if (bloqueada) {
                JOptionPane.showMessageDialog(this,"La computadora " + numeroTexto + " está bloqueada o deshabilitada.", "Computadora no disponible",JOptionPane.WARNING_MESSAGE);
                return;
            }
            this.abrirInformacionEquipo(idComputadora, apartada);
        });

        this.pnlComputadoras.add(btnComputadora);
    }
    
    private void abrirInformacionEquipo(Integer idComputadora, boolean apartada) {
        if (this.alumno == null) {
            JOptionPane.showMessageDialog(this,"No hay alumno iniciado. Regresa al inicio de sesión.","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        frmInformacionEquipo pantallaInformacion = new frmInformacionEquipo(this.alumno, idComputadora, apartada);              
        pantallaInformacion.setVisible(true);
        this.dispose();
    }

        
        @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtTitulo = new javax.swing.JLabel();
        txtSubtitulo = new javax.swing.JLabel();
        pnlComputadoras = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtTitulo.setText("Centro de Apartado CISCO");

        txtSubtitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        txtSubtitulo.setForeground(new java.awt.Color(153, 153, 153));
        txtSubtitulo.setText("Apartado de Máquina");

        javax.swing.GroupLayout pnlComputadorasLayout = new javax.swing.GroupLayout(pnlComputadoras);
        pnlComputadoras.setLayout(pnlComputadorasLayout);
        pnlComputadorasLayout.setHorizontalGroup(
            pnlComputadorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 607, Short.MAX_VALUE)
        );
        pnlComputadorasLayout.setVerticalGroup(
            pnlComputadorasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 262, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(pnlComputadoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(475, 475, 475)
                        .addComponent(txtTitulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(462, 462, 462)
                        .addComponent(txtSubtitulo)))
                .addContainerGap(488, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtSubtitulo)
                .addGap(30, 30, 30)
                .addComponent(pnlComputadoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(343, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
         * @param args the command line arguments
         */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlComputadoras;
    private javax.swing.JLabel txtSubtitulo;
    private javax.swing.JLabel txtTitulo;
    // End of variables declaration//GEN-END:variables
}
