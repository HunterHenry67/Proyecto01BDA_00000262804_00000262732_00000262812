/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PresentacionProgramaApartado;

import Entidades.Alumno;
import Entidades.Carrera;
import Entidades.Computadora;
import Entidades.Reserva;
import Negocio.CarreraBO;
import Negocio.ComputadoraBO;
import Negocio.ICarreraBO;
import Negocio.IComputadoraBO;
import Negocio.IReservaBO;
import Negocio.NegocioException;
import Negocio.ReservaBO;
import Persistencia.CarreraDAO;
import Persistencia.ComputadoraDAO;
import Persistencia.ConexionBD;
import Persistencia.ReservaDAO;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalTime;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

/**
 *
 * @author BALAMRUSH
 */
public class frmSeleccionEquipo extends javax.swing.JFrame {
    private Alumno alumno;
    private IComputadoraBO computadoraBO;
    private IReservaBO reservaBO;
    private ICarreraBO carreraBO;
    private Carrera carrera;
    private Timer timerActualizacion;
    
    public frmSeleccionEquipo() {
        initComponents();
        this.prepararPantalla();
        this.inicializarNegocio();
        this.configurarPanelComputadoras();
        this.cargarComputadoras();
        this.iniciarActualizacionAutomatica();
    }
    
    public frmSeleccionEquipo(Alumno alumno) {
        initComponents();
        this.alumno = alumno;
        this.prepararPantalla();
        this.inicializarNegocio();
        this.configurarPanelComputadoras();
        this.cargarComputadoras();
        this.cargarDatosAlumno();
        this.cargarTiempoUsoAlumno();
        this.iniciarActualizacionAutomatica();
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
        this.carreraBO = new CarreraBO(new CarreraDAO(conexion));
    }

    private void configurarPanelComputadoras() {
        pnlComputadoras.setLayout(new GridLayout(0, 3, 100, 70));
        pnlComputadoras.setBackground(new Color(220, 220, 220));
    }

    private void cargarComputadoras() {
        try {
            pnlComputadoras.removeAll();
            List<Computadora> computadoras = this.computadoraBO.consultarComputadoras();
            for (Computadora computadora : computadoras) {
                boolean bloqueada = !computadora.isEstatus();
                boolean apartada = this.computadoraApartada(computadora.getIdComputadora());
                this.agregarComputadora(computadora.getIdComputadora(), computadora.getNumeroMaquina(), apartada, bloqueada);
            }
            pnlComputadoras.revalidate();
            pnlComputadoras.repaint();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar computadoras", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean computadoraApartada(Integer idComputadora) {
        try {
            List reservasActivas = this.reservaBO.consultarReservasActivas();
            for (Object objeto : reservasActivas) {
                Reserva reserva = (Reserva) objeto;
                if (reserva.getIdComputadora() != null && reserva.getIdComputadora().equals(idComputadora)) {
                    return true;
                }
            }
            return false;
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al consultar reservas activas", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void agregarComputadora(Integer idComputadora, Integer numeroMaquina, boolean apartada, boolean bloqueada) {
        JButton btnComputadora = new JButton();
        String numeroTexto = String.format("%02d", numeroMaquina);
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
                JOptionPane.showMessageDialog(this,
                        "La computadora " + numeroTexto + " está bloqueada o deshabilitada.",
                        "Computadora no disponible",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            this.abrirInformacionEquipo(idComputadora, apartada);
        });
        this.pnlComputadoras.add(btnComputadora);
    }

    private void abrirInformacionEquipo(Integer idComputadora, boolean apartada) {
        if (this.alumno == null) {
            JOptionPane.showMessageDialog(this, "No hay alumno iniciado. Regresa al inicio de sesión.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.detenerActualizacionAutomatica();
        frmInformacionEquipo pantallaInformacion = new frmInformacionEquipo(this.alumno, idComputadora, apartada);
        pantallaInformacion.setVisible(true);
        this.dispose();
    }

    private void cargarDatosAlumno() {
        try {
            if (this.alumno == null) {
                return;
            }
            String nombreCompleto = this.alumno.getNombres()
                    + " "
                    + this.alumno.getApellidoPaterno()
                    + " "
                    + this.alumno.getApellidoMaterno();
            this.lblNombreAlumno.setText(nombreCompleto);
            this.lblIdAlumno.setText(String.format("%010d", this.alumno.getIdAlumno()));
            this.carrera = this.carreraBO.consultarCarrera(this.alumno.getIdCarrera());
            this.lblCarrera.setText(this.carrera.getNombre());
            this.lblTiempoMaximo.setText(this.formatearTiempoDiario(this.carrera.getTiempoDiario()));
            this.lblIconoReloj.setText("⏱");

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar datos del alumno", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatearTiempoDiario(LocalTime tiempoDiario) {
        if (tiempoDiario == null) {
            return "00:00:00";
        }
        return tiempoDiario.toString();
    }

    private void iniciarActualizacionAutomatica() {
        if (this.timerActualizacion != null && this.timerActualizacion.isRunning()) {
            this.timerActualizacion.stop();
        }
        this.timerActualizacion = new javax.swing.Timer(30000, e -> {
            this.cargarComputadoras();
            if (this.alumno != null) {
                this.cargarTiempoUsoAlumno();
            }
        });
        this.timerActualizacion.start();
    }

    private void detenerActualizacionAutomatica() {
        if (this.timerActualizacion != null && this.timerActualizacion.isRunning()) {
            this.timerActualizacion.stop();
        }
    }

    private void cargarTiempoUsoAlumno() {
        try {
            Integer minutosUsados = this.reservaBO.consultarMinutosUsadosPorAlumno(
                    this.alumno.getIdAlumno()
            );
            this.lblTiempoActual.setText(this.formatearMinutos(minutosUsados));
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar tiempo usado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatearMinutos(Integer minutosTotales) {
        if (minutosTotales == null) {
            return "00:00:00";
        }
        int horas = minutosTotales / 60;
        int minutos = minutosTotales % 60;
        return String.format("%02d:%02d:00", horas, minutos);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtTitulo = new javax.swing.JLabel();
        txtSubtitulo = new javax.swing.JLabel();
        pnlComputadoras = new javax.swing.JPanel();
        lblNombreAlumno = new javax.swing.JLabel();
        lblIdAlumno = new javax.swing.JLabel();
        lblCarrera = new javax.swing.JLabel();
        lblIconoReloj = new javax.swing.JLabel();
        lblTiempoMaximo = new javax.swing.JLabel();
        lblTiempoActual = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Selección de Equipo");

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

        lblNombreAlumno.setText("jLabel1");

        lblIdAlumno.setText("jLabel2");

        lblCarrera.setText("jLabel3");

        lblIconoReloj.setText("jLabel4");

        lblTiempoMaximo.setText("jLabel5");

        lblTiempoActual.setText("jLabel6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(pnlComputadoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 549, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblNombreAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                            .addComponent(lblIdAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(272, 272, 272)
                        .addComponent(txtTitulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCarrera)
                            .addComponent(lblIconoReloj))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblTiempoMaximo, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                            .addComponent(lblTiempoActual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(234, 234, 234)
                        .addComponent(txtSubtitulo)))
                .addContainerGap(488, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(txtSubtitulo))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblNombreAlumno)
                        .addGap(8, 8, 8)
                        .addComponent(lblIdAlumno)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCarrera)
                            .addComponent(lblTiempoMaximo))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIconoReloj)
                    .addComponent(lblTiempoActual))
                .addGap(13, 13, 13)
                .addComponent(pnlComputadoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(323, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblCarrera;
    private javax.swing.JLabel lblIconoReloj;
    private javax.swing.JLabel lblIdAlumno;
    private javax.swing.JLabel lblNombreAlumno;
    private javax.swing.JLabel lblTiempoActual;
    private javax.swing.JLabel lblTiempoMaximo;
    private javax.swing.JPanel pnlComputadoras;
    private javax.swing.JLabel txtSubtitulo;
    private javax.swing.JLabel txtTitulo;
    // End of variables declaration//GEN-END:variables
}
