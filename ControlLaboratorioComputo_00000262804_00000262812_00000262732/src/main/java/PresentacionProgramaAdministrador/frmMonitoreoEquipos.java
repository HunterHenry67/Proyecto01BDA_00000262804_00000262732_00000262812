package PresentacionProgramaAdministrador;

import Persistencia.ConexionBD;
import Persistencia.IConexionBD;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class frmMonitoreoEquipos extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmMonitoreoEquipos.class.getName());

    private DefaultTableModel modeloTabla;
    private Timer timerActualizacion;
    private final IConexionBD conexion;

    public frmMonitoreoEquipos() {
        this.conexion = new ConexionBD();
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
        btnRegresar = new javax.swing.JButton();

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

        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(e -> btnRegresarActionPerformed());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtTitulo)
                                        .addComponent(txtSubtitulo)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cboFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblFiltro)))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
                                        .addComponent(btnRegresar))
                                .addGap(45, 45, 45))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(txtTitulo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSubtitulo)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cboFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblFiltro)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(btnRegresar)
                                .addGap(25, 25, 25))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void inicializarPantalla() {
        modeloTabla = (DefaultTableModel) tblAlumnos.getModel();
        configurarTabla();

        txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                cargarMonitoreo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                cargarMonitoreo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                cargarMonitoreo();
            }
        });

        cboFiltro.addActionListener(e -> cargarMonitoreo());

        cargarMonitoreo();
    }

    private void configurarTabla() {
        tblAlumnos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblAlumnos.setRowHeight(28);
        tblAlumnos.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = tblAlumnos.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void cargarMonitoreo() {
        modeloTabla.setRowCount(0);

        String busqueda = txtBusqueda.getText().trim();
        String filtro = cboFiltro.getSelectedItem().toString();

        String sql = crearSQL(filtro, busqueda);

        try (Connection conn = conexion.crearConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!busqueda.isBlank()) {
                llenarParametros(ps, filtro, busqueda);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modeloTabla.addRow(new Object[]{
                        rs.getInt("numeroMaquina"),
                        rs.getObject("idAlumno") == null ? "N/A" : rs.getInt("idAlumno"),
                        rs.getString("alumno"),
                        rs.getString("estado"),
                        rs.getString("carrera"),
                        rs.getString("unidadAcademica"),
                        rs.getString("centroComputo"),
                        formatearFecha(rs.getTimestamp("fechaHoraApartado")),
                        formatearFecha(rs.getTimestamp("fechaHoraInicio")),
                        formatearFecha(rs.getTimestamp("fechaHoraFinal"))
                    });
                }
            }

        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar monitoreo:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String crearSQL(String filtro, String busqueda) {
        String sqlBase = """
            SELECT
                c.numeroMaquina,
                a.idAlumno,
                COALESCE(CONCAT(a.nombre, ' ', a.apellidoPaterno, ' ', a.apellidoMaterno), 'Disponible') AS alumno,
                CASE
                    WHEN c.estatus = 1 THEN 'Disponible'
                    ELSE 'Bloqueada'
                END AS estado,
                COALESCE(ca.nombre, 'N/A') AS carrera,
                COALESCE(ua.nombre, 'N/A') AS unidadAcademica,
                CONCAT('Centro ', cc.idCentroComputo) AS centroComputo,
                r.fechaHoraApartado,
                r.fechaHoraInicio,
                r.fechaHoraFinal
            FROM computadora c
            LEFT JOIN reserva r ON c.idComputadora = r.idComputadora
            LEFT JOIN alumno a ON r.idAlumno = a.idAlumno
            LEFT JOIN carrera ca ON a.idCarrera = ca.idCarrera
            LEFT JOIN centrocomputo cc ON c.idCentroComputo = cc.idCentroComputo
            LEFT JOIN unidadacademica ua ON cc.idUnidadAcademica = ua.idUnidadAcademica
        """;

        if (busqueda == null || busqueda.isBlank()) {
            return sqlBase + " ORDER BY c.numeroMaquina, r.fechaHoraApartado";
        }

        return sqlBase + crearWhere(filtro) + " ORDER BY c.numeroMaquina, r.fechaHoraApartado";
    }

    private String crearWhere(String filtro) {
        if (filtro == null || filtro.equals("Todos")) {
            return """
                WHERE
                    CAST(c.numeroMaquina AS CHAR) LIKE ?
                    OR CAST(a.idAlumno AS CHAR) LIKE ?
                    OR CONCAT(a.nombre, ' ', a.apellidoPaterno, ' ', a.apellidoMaterno) LIKE ?
                    OR ca.nombre LIKE ?
                    OR ua.nombre LIKE ?
                    OR CONCAT('Centro ', cc.idCentroComputo) LIKE ?
                    OR CASE WHEN c.estatus = 1 THEN 'Disponible' ELSE 'Bloqueada' END LIKE ?
            """;
        }

        return switch (filtro) {
            case "Máquina" -> " WHERE CAST(c.numeroMaquina AS CHAR) LIKE ? ";
            case "ID Alumno" -> " WHERE CAST(a.idAlumno AS CHAR) LIKE ? ";
            case "Nombre" -> " WHERE CONCAT(a.nombre, ' ', a.apellidoPaterno, ' ', a.apellidoMaterno) LIKE ? ";
            case "Carrera" -> " WHERE ca.nombre LIKE ? ";
            case "Unidad Académica" -> " WHERE ua.nombre LIKE ? ";
            case "Centro de Cómputo" -> " WHERE CONCAT('Centro ', cc.idCentroComputo) LIKE ? ";
            case "Estado" -> " WHERE CASE WHEN c.estatus = 1 THEN 'Disponible' ELSE 'Bloqueada' END LIKE ? ";
            default -> " WHERE CAST(c.numeroMaquina AS CHAR) LIKE ? ";
        };
    }

    private void llenarParametros(PreparedStatement ps, String filtro, String busqueda) throws Exception {
        String valor = "%" + busqueda.trim() + "%";

        if (filtro == null || filtro.equals("Todos")) {
            for (int i = 1; i <= 7; i++) {
                ps.setString(i, valor);
            }
        } else {
            ps.setString(1, valor);
        }
    }

    private String formatearFecha(Timestamp fecha) {
        if (fecha == null) {
            return "N/A";
        }

        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(fecha);
    }

    private void iniciarActualizacionAutomatica() {
        timerActualizacion = new Timer(5000, e -> cargarMonitoreo());
        timerActualizacion.start();
    }

    private void detenerTimer() {
        if (timerActualizacion != null) {
            timerActualizacion.stop();
        }
    }

    private void btnRegresarActionPerformed() {
        detenerTimer();

        frmMenuGestion ventana = new frmMenuGestion();
        ventana.setVisible(true);
        ventana.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        this.dispose();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new frmMonitoreoEquipos().setVisible(true));
    }

    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cboFiltro;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JTable tblAlumnos;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JLabel txtSubtitulo;
    private javax.swing.JLabel txtTitulo;
}