package PresentacionProgramaAdministrador;

import Entidades.Alumno;
import Entidades.Bloqueo;
import Negocio.IAlumnoBO;
import Negocio.IBloqueoBO;
import Negocio.ICentroComputoBO;
import Negocio.NegocioException;

import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public class frmGestionAlumnos extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(frmGestionAlumnos.class.getName());

   
    private final IAlumnoBO alumnoBO;
    private final IBloqueoBO bloqueoBO;
    private final ICentroComputoBO centroComputoBO;


    private List<Alumno> listaCompleta = new ArrayList<>();
    private int paginaActual = 0;
    private static final int REGISTROS_POR_PAGINA = 10;

    private DefaultTableModel modeloTabla;
    private boolean actualizandoTabla = false;

    
    public frmGestionAlumnos() {
        ControlFormsProgramaAdminstrador ctrl = ControlFormsProgramaAdminstrador.getInstance();
        this.alumnoBO  = ctrl.getAlumnoBO();
        this.bloqueoBO = ctrl.getBloqueoBO();
        this.centroComputoBO = ctrl.getCentroComputoBO();

        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        inicializarTabla();
        inicializarEventosBusqueda();
        cargarAlumnos("");
    }

    
    private void inicializarTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Apellido Paterno", "Apellido Materno", "Estado", "Bloquear", "Motivo"},
                0
        ) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 5;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 ? Boolean.class : String.class;
            }
        };
        tblAlumnos.setModel(modeloTabla);

        modeloTabla.addTableModelListener(e -> {
            if (!actualizandoTabla && e.getType() == TableModelEvent.UPDATE && e.getColumn() == 5) {
                procesarCambioCheckboxAlumno(e.getFirstRow());
            }
        });

        tblAlumnos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    accionBloqueoEnFilaSeleccionada();
                }
            }
        });
    }

    private void inicializarEventosBusqueda() {
        btnSiguiente.addActionListener(this::btnSiguienteActionPerformed);
        cboFiltro.addActionListener(e -> buscarEnTiempoReal());
        txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buscarEnTiempoReal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buscarEnTiempoReal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buscarEnTiempoReal();
            }
        });
    }

    
    private void cargarAlumnos(String filtro) {
        try {
            listaCompleta = alumnoBO.consultar(filtro == null ? "" : filtro);
            paginaActual = 0;
            mostrarPagina();
        } catch (NegocioException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this,
                    "Error al consultar alumnos:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

  
    private void mostrarPagina() {
    actualizandoTabla = true;
    modeloTabla.setRowCount(0);

    int inicio = paginaActual * REGISTROS_POR_PAGINA;
    int fin = Math.min(inicio + REGISTROS_POR_PAGINA, listaCompleta.size());

    List<Bloqueo> bloqueosActivos = new ArrayList<>();

    try {
        bloqueosActivos = bloqueoBO.consultarBloqueosActivos();
    } catch (NegocioException ex) {
        logger.log(Level.SEVERE, ex.getMessage(), ex);
    }

    for (int i = inicio; i < fin; i++) {
        Alumno a = listaCompleta.get(i);

        boolean bloqueado = false;
        String motivo = "";

        for (Bloqueo bloqueo : bloqueosActivos) {
            if (bloqueo.getIdAlumno() == a.getIdAlumno()) {
            bloqueado = true;
            motivo = bloqueo.getMotivo();
            break;
}
        }

        modeloTabla.addRow(new Object[]{
            a.getIdAlumno(),
            a.getNombres(),
            a.getApellidoPaterno(),
            a.getApellidoMaterno(),
            a.isEstatus() ? "Activo" : "Inactivo",
            bloqueado,
            motivo
        });
    }

    actualizandoTabla = false;
    actualizarBotonesPaginacion();
}

    private void actualizarBotonesPaginacion() {
        btnAnterior.setEnabled(paginaActual > 0);
        btnSiguiente.setEnabled((paginaActual + 1) * REGISTROS_POR_PAGINA < listaCompleta.size());
    }

    
    private void accionBloqueoEnFilaSeleccionada() {
        int fila = tblAlumnos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer idAlumno = (Integer) modeloTabla.getValueAt(fila, 0);
        boolean bloqueado = Boolean.TRUE.equals(modeloTabla.getValueAt(fila, 5));

        if (bloqueado) {
           
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Desea desbloquear al alumno con ID " + idAlumno + "?",
                    "Confirmar Desbloqueo", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION && solicitarContrasenaMaestra()) {
                desbloquearAlumno(idAlumno);
            }
        } else {
           
            if (!solicitarContrasenaMaestra()) {
                return;
            }
            String motivo = JOptionPane.showInputDialog(this,
                    "Ingrese el motivo del bloqueo:", "Bloquear Alumno",
                    JOptionPane.PLAIN_MESSAGE);
            if (motivo != null && !motivo.isBlank()) {
                bloquearAlumno(idAlumno, motivo.trim());
            } else if (motivo != null) {
                JOptionPane.showMessageDialog(this, "El motivo no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void procesarCambioCheckboxAlumno(int fila) {
        if (fila < 0 || fila >= modeloTabla.getRowCount()) {
            return;
        }

        Integer idAlumno = (Integer) modeloTabla.getValueAt(fila, 0);
        boolean bloquear = Boolean.TRUE.equals(modeloTabla.getValueAt(fila, 5));

        if (!solicitarContrasenaMaestra()) {
            revertirCheckboxAlumno(fila, !bloquear);
            return;
        }

        if (bloquear) {
            String motivo = JOptionPane.showInputDialog(this,
                    "Ingrese el motivo del bloqueo:", "Bloquear Alumno",
                    JOptionPane.PLAIN_MESSAGE);
            if (motivo == null) {
                revertirCheckboxAlumno(fila, false);
                return;
            }
            if (motivo.isBlank()) {
                JOptionPane.showMessageDialog(this, "El motivo no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                revertirCheckboxAlumno(fila, false);
                return;
            }
            bloquearAlumno(idAlumno, motivo.trim());
        } else {
            desbloquearAlumno(idAlumno);
        }
    }

    private void revertirCheckboxAlumno(int fila, boolean valorAnterior) {
        actualizandoTabla = true;
        modeloTabla.setValueAt(valorAnterior, fila, 5);
        actualizandoTabla = false;
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

    private void bloquearAlumno(Integer idAlumno, String motivo) {
        try {
            bloqueoBO.bloquearAlumno(idAlumno, motivo);
            JOptionPane.showMessageDialog(this, "Alumno bloqueado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            refrescarFiltroActual();
        } catch (NegocioException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Error al bloquear alumno:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desbloquearAlumno(Integer idAlumno) {
        try {
            bloqueoBO.desbloquearAlumno(idAlumno);
            JOptionPane.showMessageDialog(this, "Alumno desbloqueado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            refrescarFiltroActual();
        } catch (NegocioException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Error al desbloquear alumno:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void refrescarFiltroActual() {
        String filtro = construirFiltroActual();
        try {
            listaCompleta = alumnoBO.consultar(filtro);
            mostrarPagina();
        } catch (NegocioException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

   
    private String construirFiltroActual() {
        String texto = txtBusqueda.getText().trim();
        String criterio = (String) cboFiltro.getSelectedItem();

        // Filtros especiales de estado de bloqueo
        if ("Bloqueado".equals(criterio) || "No Bloqueado".equals(criterio)) {
            return criterio; // el DAO o BO puede interpretar estas cadenas
        }
        return texto;
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();
        txtTitulo = new javax.swing.JLabel();
        txtSubtitulo = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        cboFiltro = new javax.swing.JComboBox<>();
        lblFiltro = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlumnos = new javax.swing.JTable();
        btnAnterior = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        btnRegresar1 = new javax.swing.JButton();

        jButton2.setBackground(new java.awt.Color(45, 45, 45));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Gestión de Monitoreo de Equipo");
        jButton2.setBorderPainted(false);
        jButton2.setFocusPainted(false);
        jButton2.addActionListener(this::jButton2ActionPerformed);

        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(this::btnRegresarActionPerformed);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtTitulo.setText("Panel de Administración");

        txtSubtitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        txtSubtitulo.setForeground(new java.awt.Color(153, 153, 153));
        txtSubtitulo.setText("Gestión Alumnos");

        txtBusqueda.addActionListener(this::txtBusquedaActionPerformed);

        cboFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nombre", "Apellido Paterno", "Apellido Materno", "Estado", "Bloqueado", "No Bloqueado" }));

        lblFiltro.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblFiltro.setText("Filtro de Búsqueda");

        tblAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Apellido Paterno", "Apellido Materno", "Estado", "Bloquear", "Motivo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblAlumnos);

        btnAnterior.setText("⬅ Anterior");
        btnAnterior.addActionListener(this::btnAnteriorActionPerformed);

        btnSiguiente.setText("Siguiente ➡");

        btnRegresar1.setText("Regresar");
        btnRegresar1.addActionListener(this::btnRegresar1ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(248, 248, 248)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(txtSubtitulo))
                                    .addComponent(txtTitulo)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboFiltro, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnRegresar1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAnterior)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSiguiente)))))
                .addGap(95, 95, 95))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSubtitulo)
                    .addComponent(lblFiltro))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnterior)
                    .addComponent(btnSiguiente)
                    .addComponent(btnRegresar1))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // botón de prueba heredado del diseño — sin acción
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaActionPerformed
        buscarEnTiempoReal();
    }//GEN-LAST:event_txtBusquedaActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        if (paginaActual > 0) {
            paginaActual--;
            mostrarPagina();
        }
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {
        if ((paginaActual + 1) * REGISTROS_POR_PAGINA < listaCompleta.size()) {
            paginaActual++;
            mostrarPagina();
        }
    }

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        frmMenuGestion ventana = new frmMenuGestion();
        ventana.setVisible(true);
        ventana.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void btnRegresar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresar1ActionPerformed
        frmMenuGestion ventana = new frmMenuGestion();
        ventana.setVisible(true);
        ventana.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        this.dispose();
    }//GEN-LAST:event_btnRegresar1ActionPerformed

   
    private void buscarEnTiempoReal() {
        String filtro = construirFiltroActual();
        cargarAlumnos(filtro);
    }

   
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
        java.awt.EventQueue.invokeLater(() -> new frmGestionAlumnos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JButton btnRegresar1;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JComboBox<String> cboFiltro;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JTable tblAlumnos;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JLabel txtSubtitulo;
    private javax.swing.JLabel txtTitulo;
    // End of variables declaration//GEN-END:variables
}
