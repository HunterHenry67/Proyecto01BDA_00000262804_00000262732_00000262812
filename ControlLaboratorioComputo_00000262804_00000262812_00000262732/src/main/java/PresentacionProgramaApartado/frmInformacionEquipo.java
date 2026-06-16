/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PresentacionProgramaApartado;

import Dtos.ComputadoraDTO;
import Dtos.GuardarReservaDTO;
import Entidades.Alumno;
import Entidades.Carrera;
import Entidades.CentroComputo;
import Entidades.Computadora;
import Entidades.Reserva;
import Entidades.Software;
import Entidades.UnidadAcademica;
import Negocio.CarreraBO;
import Negocio.CentroComputoBO;
import Negocio.ComputadoraBO;
import Negocio.ICarreraBO;
import Negocio.ICentroComputoBO;
import Negocio.IComputadoraBO;
import Negocio.IReservaBO;
import Negocio.IUnidadAcademicaBO;
import Negocio.NegocioException;
import Negocio.ReservaBO;
import Persistencia.CarreraDAO;
import Persistencia.CentroComputoDAO;
import Persistencia.ComputadoraDAO;
import Persistencia.ConexionBD;
import Persistencia.ReservaDAO;
import Negocio.UnidadAcademicaBO;
import Persistencia.UnidadAcademicaDAO;
import java.awt.BorderLayout;
import java.awt.Font;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author BALAMRUSH
 */
public class frmInformacionEquipo extends javax.swing.JFrame {

    private final Alumno alumno;
    private final Integer idComputadora;
    private boolean apartada;
    private IComputadoraBO computadoraBO;
    private IReservaBO reservaBO;
    private ICarreraBO carreraBO;
    private ICentroComputoBO centroComputoBO;
    private IUnidadAcademicaBO unidadAcademicaBO;
    private Computadora computadora;
    private Carrera carrera;
    private CentroComputo centroComputo;
    private UnidadAcademica unidadAcademica;

    public frmInformacionEquipo(Alumno alumno, Integer idComputadora, boolean apartada) {
        initComponents();
        this.alumno = alumno;
        this.idComputadora = idComputadora;
        this.apartada = apartada;
        this.setLocationRelativeTo(null);
        this.inicializarNegocio();
        this.cargarDatosAlumno();
        this.cargarInformacionEquipo();
        this.configurarBotonSeleccionar();
        this.lblTiempoActual.setText("00:00:00");
    }

    private void inicializarNegocio() {
        ConexionBD conexion = new ConexionBD();
        this.computadoraBO = new ComputadoraBO(new ComputadoraDAO(conexion));
        this.reservaBO = new ReservaBO(new ReservaDAO(conexion));
        this.carreraBO = new CarreraBO(new CarreraDAO(conexion));
        this.centroComputoBO = new CentroComputoBO(new CentroComputoDAO(conexion));
        this.unidadAcademicaBO = new UnidadAcademicaBO(new UnidadAcademicaDAO(conexion));
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
            if (this.carrera.getTiempoDiario() != null) {
                this.lblTiempoMaximo.setText(this.carrera.getTiempoDiario().toString());
            } else {
                this.lblTiempoMaximo.setText("00:00:00");
            }
            this.lblIconoReloj.setText("⏱");
            this.lblTiempoActual.setText("00:00:00");
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error al cargar datos del alumno",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarInformacionEquipo() {
        try {
            this.computadora = this.computadoraBO.validarComputadoraDisponible(this.idComputadora);
            this.centroComputo = this.centroComputoBO.obtenerCentroPorComputadora(this.idComputadora);
            this.unidadAcademica = this.unidadAcademicaBO.consultarUnidadAcademicaPorID(
                    this.centroComputo.getIdUnidadAcademica()
            );
            String numeroMaquina = String.format("%02d", this.computadora.getNumeroMaquina());
            this.jLabel1.setText(numeroMaquina);
            this.jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 48));
            this.jLabel1.setHorizontalAlignment(JLabel.CENTER);
            this.jLabel2.setText("Máquina Seleccionada: " + numeroMaquina);
            this.jLabel3.setText("Unidad Académica: " + this.unidadAcademica.getNombre());
            this.jLabel4.setText("Centro de Cómputo ID: " + this.centroComputo.getIdCentroComputo());
            this.jLabel5.setText("Hora Inicio: " + this.centroComputo.getHoraInicio());
            this.jLabel6.setText(this.apartada ? "Estado: En uso" : "Estado: Disponible");
            if (this.apartada) {
                this.txtSubtitulo.setText("Información de Equipo (En uso)");
            } else {
                this.txtSubtitulo.setText("Información de Equipo (Disponible)");
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error al cargar información del equipo",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void mostrarSoftwareDisponible() {
        try {
            ComputadoraDTO computadoraDTO = this.computadoraBO.obtenerCatalogoSoftwarePC(this.idComputadora);
            if (computadoraDTO == null
                    || computadoraDTO.getCatalogoSoftware() == null
                    || computadoraDTO.getCatalogoSoftware().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Esta computadora no tiene software registrado.", "Software disponible", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            JDialog dialogo = new JDialog(this, "Mostrar Software", true);
            dialogo.setSize(420, 260);
            dialogo.setLocationRelativeTo(this);
            JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
            panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            JLabel lblTituloDialogo = new JLabel("Mostrar Software");
            lblTituloDialogo.setFont(new Font("Segoe UI", Font.BOLD, 16));
            JTextArea txtSoftware = new JTextArea();
            txtSoftware.setEditable(false);
            txtSoftware.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtSoftware.setLineWrap(true);
            txtSoftware.setWrapStyleWord(true);
            StringBuilder textoSoftware = new StringBuilder();
            List catalogo = computadoraDTO.getCatalogoSoftware();
            for (Object objeto : catalogo) {
                Software software = (Software) objeto;
                textoSoftware.append("• ").append(software.getNombre()).append("\n");
            }
            txtSoftware.setText(textoSoftware.toString());
            JButton btnConfirmar = new JButton("Confirmar");
            btnConfirmar.addActionListener(e -> dialogo.dispose());
            panelPrincipal.add(lblTituloDialogo, BorderLayout.NORTH);
            panelPrincipal.add(new JScrollPane(txtSoftware), BorderLayout.CENTER);
            panelPrincipal.add(btnConfirmar, BorderLayout.SOUTH);
            dialogo.add(panelPrincipal);
            dialogo.setVisible(true);
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al mostrar software", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarComputadora() {
        try {
            if (this.apartada) {
                return;
            }
            int opcion = JOptionPane.showConfirmDialog(this, "¿Deseas seleccionar la computadora " + String.format("%02d", this.computadora.getNumeroMaquina()) + "?", "Confirmar selección", JOptionPane.YES_NO_OPTION);
            if (opcion != JOptionPane.YES_OPTION) {
                return;
            }
            LocalDateTime ahora = LocalDateTime.now();
            GuardarReservaDTO reservaDTO = new GuardarReservaDTO(ahora, ahora, null, null, this.alumno.getIdAlumno(), this.idComputadora);

            Reserva reservaGuardada = this.reservaBO.guardar(reservaDTO);

            JOptionPane.showMessageDialog(this, "Selección exitosa.\nLa computadora ha sido seleccionada correctamente.\nID reserva: " + reservaGuardada.getIdReserva(), "Selección exitosa", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error al seleccionar computadora",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    
    private void configurarBotonSeleccionar() {
    if (this.apartada) {
        this.btnSeleccionar.setEnabled(false);
        this.btnSeleccionar.setToolTipText("Esta computadora ya está apartada.");
    } else {
        this.btnSeleccionar.setEnabled(true);
        this.btnSeleccionar.setToolTipText("Seleccionar esta computadora.");
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtTitulo = new javax.swing.JLabel();
        txtSubtitulo = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        btnSeleccionar = new javax.swing.JButton();
        btnMostrarSoftware = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTitulo1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblIdAlumno = new javax.swing.JLabel();
        lblCarrera = new javax.swing.JLabel();
        lblIconoReloj = new javax.swing.JLabel();
        lblTiempoMaximo = new javax.swing.JLabel();
        lblTiempoActual = new javax.swing.JLabel();
        lblNombreAlumno = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Información del Equipo");

        txtTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtTitulo.setText("Centro de Apartado CISCO");

        txtSubtitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        txtSubtitulo.setForeground(new java.awt.Color(153, 153, 153));
        txtSubtitulo.setText("Información de Equipo");

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnSeleccionar.setText("Seleccionar");
        btnSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarActionPerformed(evt);
            }
        });

        btnMostrarSoftware.setText("Mostrar Software");
        btnMostrarSoftware.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarSoftwareActionPerformed(evt);
            }
        });

        jLabel1.setText("Unidad Académica: ");

        jLabel2.setText("Laboratorio:");

        jLabel3.setText("Hora Inicio: ");

        txtTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtTitulo1.setText(".");

        jLabel4.setText("jLabel4");

        jLabel5.setText("jLabel5");

        jLabel6.setText("jLabel6");

        lblIdAlumno.setText("jLabel2");

        lblCarrera.setText("jLabel3");

        lblIconoReloj.setText("jLabel4");

        lblTiempoMaximo.setText("jLabel5");

        lblTiempoActual.setText("jLabel6");

        lblNombreAlumno.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(510, 510, 510)
                        .addComponent(btnMostrarSoftware, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(466, 466, 466)
                        .addComponent(txtTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(449, 449, 449)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblNombreAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblIdAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCarrera)
                                    .addComponent(lblIconoReloj))
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblTiempoMaximo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblTiempoActual, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(209, 209, 209)
                                .addComponent(txtSubtitulo))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(223, 223, 223)
                                .addComponent(txtTitulo)))))
                .addContainerGap(439, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSubtitulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(lblNombreAlumno)
                        .addGap(8, 8, 8)
                        .addComponent(lblIdAlumno)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCarrera)
                            .addComponent(lblTiempoMaximo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblIconoReloj)
                            .addComponent(lblTiempoActual))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addComponent(txtTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addGap(76, 76, 76)
                .addComponent(btnMostrarSoftware, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(135, 135, 135)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSeleccionar, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarActionPerformed
        this.seleccionarComputadora();
    }//GEN-LAST:event_btnSeleccionarActionPerformed

    private void btnMostrarSoftwareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarSoftwareActionPerformed
        this.mostrarSoftwareDisponible();
    }//GEN-LAST:event_btnMostrarSoftwareActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        frmSeleccionEquipo pantallaSeleccionEquipo = new frmSeleccionEquipo(this.alumno);
        pantallaSeleccionEquipo.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnMostrarSoftware;
    private javax.swing.JButton btnSeleccionar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lblCarrera;
    private javax.swing.JLabel lblIconoReloj;
    private javax.swing.JLabel lblIdAlumno;
    private javax.swing.JLabel lblNombreAlumno;
    private javax.swing.JLabel lblTiempoActual;
    private javax.swing.JLabel lblTiempoMaximo;
    private javax.swing.JLabel txtSubtitulo;
    private javax.swing.JLabel txtTitulo;
    private javax.swing.JLabel txtTitulo1;
    // End of variables declaration//GEN-END:variables
}
