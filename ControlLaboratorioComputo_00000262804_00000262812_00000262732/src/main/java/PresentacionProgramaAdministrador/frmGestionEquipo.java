/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PresentacionProgramaAdministrador;

import Entidades.Computadora;
import Negocio.ICentroComputoBO;
import Negocio.IComputadoraBO;
import Negocio.NegocioException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.Timer;

public class frmGestionEquipo extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmGestionEquipo.class.getName());

    private final ICentroComputoBO centroComputoBO;
    private final IComputadoraBO computadoraBO;

    private final Map<Integer, Computadora> computadorasPorNumero = new HashMap<>();
    private final Map<JCheckBox, Integer> numeroPorCheckbox = new HashMap<>();

    private boolean actualizandoChecks = false;
    private Timer timerActualizacion;

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
        this.computadoraBO = ctrl.getComputadoraBO();

        initComponents();
        configurarCheckboxes();
        cargarEstatusComputadoras();
        iniciarActualizacionAutomatica();
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {
        setTitle("Gestión de Equipo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(900, 600));

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                detenerTimer();
            }
        });

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

        panelMaquinas.add(crearFilaMaquina("Máquina 1", chk1));
        panelMaquinas.add(crearFilaMaquina("Máquina 6", chk6));
        panelMaquinas.add(crearFilaMaquina("Máquina 2", chk2));
        panelMaquinas.add(crearFilaMaquina("Máquina 7", chk7));
        panelMaquinas.add(crearFilaMaquina("Máquina 3", chk3));
        panelMaquinas.add(crearFilaMaquina("Máquina 8", chk8));
        panelMaquinas.add(crearFilaMaquina("Máquina 4", chk4));
        panelMaquinas.add(crearFilaMaquina("Máquina 9", chk9));

        JPanel fila5 = crearFilaMaquina("Máquina 5", chk5);

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
                JCheckBox checkBox = entry.getKey();
                int numeroMaquina = entry.getValue();

                Computadora computadora = computadoraBO.obtenerComputadoraPorNumero(numeroMaquina);

                if (computadora != null) {
                    computadorasPorNumero.put(numeroMaquina, computadora);
                }

    
                checkBox.setSelected(computadora != null && !computadora.isEstatus());
                checkBox.setEnabled(computadora != null);
            }

        } catch (NegocioException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(
                    this,
                    "Error al consultar computadoras:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {
            actualizandoChecks = false;
        }
    }

    private void procesarCambioEstatus(JCheckBox checkbox) {
        if (actualizandoChecks) {
            return;
        }

        Integer numeroMaquina = numeroPorCheckbox.get(checkbox);
        Computadora computadora = computadorasPorNumero.get(numeroMaquina);

        boolean checkboxMarcado = checkbox.isSelected();

       
        boolean nuevoEstatus = !checkboxMarcado;

        if (computadora == null) {
            checkbox.setSelected(!checkboxMarcado);
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró la máquina " + numeroMaquina + ".",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!solicitarContrasenaMaestra()) {
            checkbox.setSelected(!checkboxMarcado);
            return;
        }

        try {
            computadoraBO.actualizarEstatus(computadora.getIdComputadora(), nuevoEstatus);

            computadora.setEstatus(nuevoEstatus);

            String accion = checkboxMarcado ? "bloqueada" : "desbloqueada";

            JOptionPane.showMessageDialog(
                    this,
                    "Máquina " + numeroMaquina + " " + accion + " correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarEstatusComputadoras();

        } catch (NegocioException ex) {
            checkbox.setSelected(!checkboxMarcado);

            logger.log(Level.SEVERE, ex.getMessage(), ex);

            JOptionPane.showMessageDialog(
                    this,
                    "Error al actualizar la máquina:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private boolean solicitarContrasenaMaestra() {
        JPasswordField campoContrasena = new JPasswordField();

        int opcion = JOptionPane.showConfirmDialog(
                this,
                campoContrasena,
                "Ingrese la contraseña maestra",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcion != JOptionPane.OK_OPTION) {
            return false;
        }

        char[] contrasenaChars = campoContrasena.getPassword();
        String contrasena = new String(contrasenaChars);
        Arrays.fill(contrasenaChars, '\0');

        try {
            boolean valida = centroComputoBO.validarContraseniaMaestra(contrasena);

            if (!valida) {
                JOptionPane.showMessageDialog(
                        this,
                        "Contraseña maestra incorrecta.",
                        "Acceso denegado",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            return valida;

        } catch (NegocioException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);

            JOptionPane.showMessageDialog(
                    this,
                    "Error al validar contraseña maestra:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        }
    }

    private void iniciarActualizacionAutomatica() {
        timerActualizacion = new Timer(5000, e -> cargarEstatusComputadoras());
        timerActualizacion.start();
    }

    private void detenerTimer() {
        if (timerActualizacion != null) {
            timerActualizacion.stop();
        }
    }

    private void regresarMenu() {
        detenerTimer();

        frmMenuGestion ventana = new frmMenuGestion();
        ventana.setVisible(true);
        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.dispose();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new frmGestionEquipo().setVisible(true));
    }
}