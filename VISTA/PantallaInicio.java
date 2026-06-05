package VISTA;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class PantallaInicio extends JFrame {

    private static final Color COLOR_FONDO = new Color(10, 10, 30);
    private static final Color COLOR_PANEL_OSCURO = new Color(20, 20, 50);
    private static final Color COLOR_DORADO = new Color(255, 215, 0);
    private static final Color COLOR_DORADO_OSCURO = new Color(180, 140, 0);
    private static final Color COLOR_TEXTO_CLARO = new Color(220, 220, 255);
    private static final Color COLOR_BOTON_FONDO = new Color(40, 0, 80);
    private static final Color COLOR_BOTON_HOVER = new Color(80, 0, 140);
    private static final Color COLOR_BORDE_DORADO = new Color(200, 160, 0);

    private JTextField campoDuelista1;
    private JTextField campoDuelista2;
    private JButton botonIniciar;
    private JLabel labelError;

    public PantallaInicio() {
        super("Yu-Gi-Oh! — Pantalla de Inicio");
        inicializarUI();
    }

    private void inicializarUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(0, 0));

        add(crearPanelLogo(), BorderLayout.NORTH);
        add(crearPanelFormulario(), BorderLayout.CENTER);
        add(crearPanelFooter(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelLogo() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_FONDO);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 20, 10, 20));

        JLabel titulo = new JLabel("  YU-GI-OH!  ", SwingConstants.CENTER);
        titulo.setFont(new Font("Impact", Font.BOLD, 52));
        titulo.setForeground(COLOR_DORADO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("SIMULADOR DE DUELO ", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.BOLD, 16));
        subtitulo.setForeground(COLOR_TEXTO_CLARO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel linea = new JLabel("════════════════════════════", SwingConstants.CENTER);
        linea.setFont(new Font("Arial", Font.PLAIN, 14));
        linea.setForeground(COLOR_DORADO_OSCURO);
        linea.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel reglas = new JLabel(
                "<html><center>8000 LP · 25 cartas · Monstruos, Magia y Trampas<br>" + "¡El primero en llegar a 0 LP o agotar su mazo pierde!</center></html>",
                SwingConstants.CENTER);
        reglas.setFont(new Font("Arial", Font.ITALIC, 12));
        reglas.setForeground(new Color(160, 160, 200));
        reglas.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(subtitulo);
        panel.add(Box.createVerticalStrut(6));
        panel.add(linea);
        panel.add(Box.createVerticalStrut(10));
        panel.add(reglas);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel exterior = new JPanel(new GridBagLayout());
        exterior.setBackground(COLOR_FONDO);

        JPanel form = new JPanel();
        form.setBackground(COLOR_PANEL_OSCURO);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_DORADO, 2),
                new EmptyBorder(25, 40, 25, 40)));
        form.setPreferredSize(new Dimension(420, 300));

        JLabel formTitulo = new JLabel("— Ingresa los nombres —", SwingConstants.CENTER);
        formTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        formTitulo.setForeground(COLOR_DORADO);
        formTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelD1 = crearLabel("Duelista 1:");
        campoDuelista1 = crearCampo("Nombre del Duelista 1...");
        campoDuelista1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelD2 = crearLabel("Duelista 2:");
        campoDuelista2 = crearCampo("Nombre del Duelista 2...");
        campoDuelista2.setAlignmentX(Component.LEFT_ALIGNMENT);

        labelError = new JLabel(" ");
        labelError.setFont(new Font("Arial", Font.ITALIC, 12));
        labelError.setForeground(Color.RED);
        labelError.setAlignmentX(Component.CENTER_ALIGNMENT);

        botonIniciar = crearBoton("  INICIAR DUELO  ");
        botonIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonIniciar.setMaximumSize(new Dimension(300, 45));
        botonIniciar.addActionListener(e -> validarYArrancar());

        ActionListener enter = e -> validarYArrancar();
        campoDuelista1.addActionListener(enter);
        campoDuelista2.addActionListener(enter);

        form.add(formTitulo);
        form.add(Box.createVerticalStrut(20));
        form.add(labelD1);
        form.add(Box.createVerticalStrut(5));
        form.add(campoDuelista1);
        form.add(Box.createVerticalStrut(15));
        form.add(labelD2);
        form.add(Box.createVerticalStrut(5));
        form.add(campoDuelista2);
        form.add(Box.createVerticalStrut(12));
        form.add(labelError);
        form.add(Box.createVerticalStrut(8));
        form.add(botonIniciar);

        exterior.add(form);
        return exterior;
    }

    private JPanel crearPanelFooter() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(5, 10, 15, 10));

        JLabel cita = new JLabel("\"Confía en el corazón de las cartas\" — Yugi Muto");
        cita.setFont(new Font("Arial", Font.ITALIC, 12));
        cita.setForeground(COLOR_DORADO_OSCURO);
        panel.add(cita);
        return panel;
    }

    private void validarYArrancar() {
        String n1 = campoDuelista1.getText().trim();
        String n2 = campoDuelista2.getText().trim();

        if (n1.isEmpty() || n1.equals("Nombre del Duelista 1...")) {
            labelError.setText("¡Debes ingresar el nombre del Duelista 1!");
            campoDuelista1.requestFocus();
            return;
        }
        if (n2.isEmpty() || n2.equals("Nombre del Duelista 2...")) {
            labelError.setText("¡Debes ingresar el nombre del Duelista 2!");
            campoDuelista2.requestFocus();
            return;
        }
        if (n1.equalsIgnoreCase(n2)) {
            labelError.setText("¡Los duelistas no pueden tener el mismo nombre!");
            return;
        }

        labelError.setText(" ");
        this.dispose();

        VentanaDuelo ventana = new VentanaDuelo(n1, n2);
        ventana.setVisible(true);
    }
    
    private JLabel crearLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Arial", Font.BOLD, 13));
        l.setForeground(COLOR_TEXTO_CLARO);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField crearCampo(String placeholder) {
        JTextField campo = new JTextField(20);
        campo.setBackground(new Color(30, 30, 60));
        campo.setForeground(new Color(100, 100, 140));
        campo.setCaretColor(COLOR_DORADO);
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_DORADO, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        campo.setText(placeholder);

        campo.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(COLOR_TEXTO_CLARO);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (campo.getText().trim().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(new Color(100, 100, 140));
                }
            }
        });
        return campo;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(COLOR_DORADO);
        btn.setBackground(COLOR_BOTON_FONDO);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_DORADO, 2),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_BOTON_HOVER); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(COLOR_BOTON_FONDO); }
        });
        return btn;
    }
}
