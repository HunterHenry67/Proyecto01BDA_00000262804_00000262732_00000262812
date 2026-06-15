package PresentacionProgramaAdministrador;

import Negocio.IComputadoraBO;
import Negocio.NegocioException;
import java.awt.Font;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class frmMonitoreoEquipos extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmMonitoreoEquipos.class.getName());

    private final IComputadoraBO computadoraBO;

    private DefaultTableModel modeloTabla;

    private int paginaActual = 0;
    private int registrosPorPagina = 10;
    private int totalRegistros = 0;

    private Timer timerActualizacion;

    public frmMonitoreoEquipos() {
        this.computadoraBO = ControlFormsProgramaAdminstrador.getInstance().getComputadoraBO();

        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        inicializarPantalla();
        iniciarActualizacionAutomatica();
    }

    private void initComponents() {
        txtTitulo = new javax.swing.JLabel();
        txtSubtitulo = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        cboFiltro = new javax.swing.JComboBox<>();
        lblFiltro = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlumnos = new javax.swing.JTable();
        btnAnterior = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();
        lblRegistros = new javax.swing.JLabel();
        cboRegistros = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                detenerTimer();
            }
        });

        txtTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18));
        txtTitulo.setText("Panel de Administración");

        txtSubtitulo.setFont(new java.awt.Font("Segoe UI", 1, 24));
        txtSubtitulo.setForeground(new java.awt.Color(153, 153, 153));
        txtSubtitulo.setText("Monitoreo Equipo");

        cboFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Todos",
            "Máquina",
            "ID Alumno",
            "Nombre",
            "Carrera",
            "Unidad Académica",
            "Centro de Cómputo",
            "Estado"
        }));

        lblFiltro.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblFiltro.setText("Filtro de búsqueda");

        tblAlumnos.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Máquina",
                    "ID Alumno",
                    "Nombre",
                    "Estado",
                    "Carrera",
                    "Unidad Académica",
                    "Centro de Cómputo",
                    "Hora Apartado",
                    "Hora Inicio",
                    "Hora Fin"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        jScrollPane1.setViewportView(tblAlumnos);

        btnAnterior.setText("⬅ Anterior");
        btnAnterior.addActionListener(e -> btnAnteriorActionPerformed());

        btnSiguiente.setText("Siguiente ➡");
        btnSiguiente.addActionListener(e -> btnSiguienteActionPerformed());

        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(e -> btnRegresarActionPerformed());

        lblRegistros.setText("Cantidad de registros:");

        cboRegistros.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "10", "20", "30", "50"
        }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnRegresar)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lblRegistros)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cboRegistros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(9, 9, 9)
                                                .addComponent(btnAnterior)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnSiguiente))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtTitulo)
                                                        .addComponent(txtSubtitulo)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(15, 15, 15)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(lblFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(cboFiltro, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(43, 43, 43))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSubtitulo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cboFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblFiltro)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAnterior)
                                        .addComponent(btnSiguiente)
                                        .addComponent(btnRegresar)
                                        .addComponent(lblRegistros)
                                        .addComponent(cboRegistros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void inicializarPantalla() {
        modeloTabla = (DefaultTableModel) tblAlumnos.getModel();
        modeloTabla.setRowCount(0);

        configurarTabla();

        txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                reiniciarYCargar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                reiniciarYCargar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                reiniciarYCargar();
            }
        });

        cboFiltro.addActionListener(e -> reiniciarYCargar());

        cboRegistros.addActionListener(e -> {
            registrosPorPagina = Integer.parseInt(cboRegistros.getSelectedItem().toString());
            reiniciarYCargar();
        });

        cargarPagina();
    }

    private void configurarTabla() {
        tblAlumnos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblAlumnos.setRowHeight(28);
        tblAlumnos.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = tblAlumnos.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void reiniciarYCargar() {
        paginaActual = 0;
        cargarPagina();
    }

    private void cargarPagina() {
        try {
            modeloTabla.setRowCount(0);

            String busqueda = txtBusqueda.getText().trim();
            String filtro = cboFiltro.getSelectedItem().toString();

            int offset = paginaActual * registrosPorPagina;

            totalRegistros = computadoraBO.contarMonitoreoEquipos(busqueda, filtro);

            List<Object[]> filas = computadoraBO.obtenerMonitoreoEquipos(
                    busqueda,
                    filtro,
                    registrosPorPagina,
                    offset
            );

            for (Object[] fila : filas) {
                fila[7] = formatearFecha(fila[7]);
                fila[8] = formatearFecha(fila[8]);
                fila[9] = formatearFecha(fila[9]);

                modeloTabla.addRow(fila);
            }

            actualizarBotones();

        } catch (NegocioException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar monitoreo:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String formatearFecha(Object fecha) {
        if (fecha == null) {
            return "N/A";
        }

        if (fecha instanceof Timestamp timestamp) {
            return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
        }

        return fecha.toString();
    }

    private void actualizarBotones() {
        btnAnterior.setEnabled(paginaActual > 0);
        btnSiguiente.setEnabled((paginaActual + 1) * registrosPorPagina < totalRegistros);
    }

    private void btnAnteriorActionPerformed() {
        if (paginaActual > 0) {
            paginaActual--;
            cargarPagina();
        }
    }

    private void btnSiguienteActionPerformed() {
        if ((paginaActual + 1) * registrosPorPagina < totalRegistros) {
            paginaActual++;
            cargarPagina();
        }
    }

    private void btnRegresarActionPerformed() {
        detenerTimer();

        frmMenuGestion ventana = new frmMenuGestion();
        ventana.setVisible(true);
        ventana.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        this.dispose();
    }

    private void iniciarActualizacionAutomatica() {
        timerActualizacion = new Timer(5000, e -> cargarPagina());
        timerActualizacion.start();
    }

    private void detenerTimer() {
        if (timerActualizacion != null) {
            timerActualizacion.stop();
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new frmMonitoreoEquipos().setVisible(true));
    }

    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JComboBox<String> cboFiltro;
    private javax.swing.JComboBox<String> cboRegistros;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JLabel lblRegistros;
    private javax.swing.JTable tblAlumnos;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JLabel txtSubtitulo;
    private javax.swing.JLabel txtTitulo;
}