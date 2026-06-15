/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PresentacionProgramaAdministrador;

import Entidades.Computadora;
import Negocio.ICentroComputoBO;
import Negocio.NegocioException;
import Persistencia.ConexionBD;
import Persistencia.IConexionBD;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

public class frmGestionEquipo extends JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(frmGestionEquipo.class.getName());

    private final ICentroComputoBO centroComputoBO;
    private final IConexionBD conexionBD;

    private final Map<Integer, Computadora> computadorasPorNumero = new HashMap<>();
    private final Map<JCheckBox, Integer> numeroPorCheckbox = new HashMap<>();
    private boolean actualizandoChecks = false;

    private JCheckBox chk1;
    private JCheckBox chk2;
    private JCheckBox chk3;
    private JCheckBox chk4;
    private JCheckBox chk5;
    private JCheckBox chk6;
    private JCheckBox chk7;
    private JCheckBox chk8;
    private JCheckBox chk9;

    private JButton btnRegresar;

    public frmGestionEquipo() {
        ControlFormsProgramaAdminstrador ctrl = ControlFormsProgramaAdminstrador.getInstance();
        this.centroComputoBO = ctrl.getCentroComputoBO();
        this.conexionBD = new ConexionBD();

        initComponents();
        configurarCheckboxes();
        cargarEstatusComputadoras();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {
        setTitle("Gestión de Equipo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(900, 600));

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(238, 238, 238));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 60, 40, 60));

        JLabel lblTitulo = new JLabel("Panel de Administración", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel lblSubtitulo = new JLabel("Gestión de Equipo", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblSubtitulo.setForeground(new Color(150, 150, 150));

        JPanel panelTitulos = new JPanel(new GridLayout(2, 1, 0, 5));
        panelTitulos.setOpaque(false);
        panelTitulos.add(lblTitulo);
        panelTitulos.add(lblSubtitulo);

        panelPrincipal.add(panelTitulos, BorderLayout.NORTH);

        chk1 = new JCheckBox();
        chk2 = new JCheckBox();
        chk3 = new JCheckBox();
        chk4 = new JCheckBox();
        chk5 = new JCheckBox();
        chk6 = new JCheckBox();
        chk7 = new JCheckBox();
        chk8 = new JCheckBox();
        chk9 = new JCheckBox();

        JPanel panelMaquinas = new JPanel(new GridLayout(5, 2, 80, 25));
        panelMaquinas.setOpaque(false);
        panelMaquinas.setBorder(BorderFactory.createEmptyBorder(90, 80, 80, 80));

        panelMaquinas.add(crearFilaMaquina("Maquina 1", chk1));
        panelMaquinas.add(crearFilaMaquina("Maquina 6", chk6));

        panelMaquinas.add(crearFilaMaquina("Maquina 2", chk2));
        panelMaquinas.add(crearFilaMaquina("Maquina 7", chk7));

        panelMaquinas.add(crearFilaMaquina("Maquina 3", chk3));
        panelMaquinas.add(crearFilaMaquina("Maquina 8", chk8));

        panelMaquinas.add(crearFilaMaquina("Maquina 4", chk4));
        panelMaquinas.add(crearFilaMaquina("Maquina 9", chk9));

        JPanel fila5 = crearFilaMaquina("Maquina 5", chk5);
        JPanel panelRegresar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelRegresar.setOpaque(false);

        btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(e -> regresarMenu());
        panelRegresar.add(btnRegresar);

        panelMaquinas.add(panelRegresar);
        panelMaquinas.add(fila5);

        panelPrincipal.add(panelMaquinas, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearFilaMaquina(String texto, JCheckBox checkbox) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setOpaque(false);

        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));

        checkbox.setOpaque(false);

        panel.add(label);
        panel.add(checkbox);

        return panel;
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

                // Checkbox marcado = bloqueada
                // Checkbox desmarcado = desbloqueada
                entry.getKey().setSelected(computadora != null && !computadora.isEstatus());
                entry.getKey().setEnabled(computadora != null);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this,
                    "Error al consultar computadoras:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
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

        boolean checkboxMarcado = checkbox.isSelected();

        // Marcado = bloquear = estatus false
        // Desmarcado = desbloquear = estatus true
        boolean nuevoEstatus = !checkboxMarcado;

        if (computadora == null) {
            checkbox.setSelected(!checkboxMarcado);
            JOptionPane.showMessageDialog(this,
                    "No se encontro la maquina " + numeroMaquina + ".",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!solicitarContrasenaMaestra()) {
            checkbox.setSelected(!checkboxMarcado);
            return;
        }

        try {
            actualizarEstatusComputadora(computadora.getIdComputadora(), nuevoEstatus);
            computadora.setEstatus(nuevoEstatus);

            String accion = checkboxMarcado ? "bloqueada" : "desbloqueada";

            JOptionPane.showMessageDialog(this,
                    "Maquina " + numeroMaquina + " " + accion + " correctamente.",
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            checkbox.setSelected(!checkboxMarcado);
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar la maquina:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean solicitarContrasenaMaestra() {
        JPasswordField campoContrasena = new JPasswordField();

        int opcion = JOptionPane.showConfirmDialog(this,
                campoContrasena,
                "Ingrese la contraseña maestra",
                JOptionPane.OK_CANCEL_OPTION,
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
                JOptionPane.showMessageDialog(this,
                        "Contraseña maestra incorrecta.",
                        "Acceso denegado",
                        JOptionPane.ERROR_MESSAGE);
            }

            return valida;

        } catch (NegocioException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this,
                    "Error al validar contraseña maestra:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void regresarMenu() {
        frmMenuGestion ventana = new frmMenuGestion();
        ventana.setVisible(true);
        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.dispose();
    }

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info :
                    javax.swing.UIManager.getInstalledLookAndFeels()) {

                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException |
                 javax.swing.UnsupportedLookAndFeelException ex) {

            logger.log(Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new frmGestionEquipo().setVisible(true));
    }
}