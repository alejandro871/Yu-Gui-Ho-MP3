package VISTA;

import CONTROLADOR.controladorJuego;
import cartas.Carta;
import cartas.CartaMagica;
import cartas.CartaTrampa;
import cartas.Monstruo;
import juego.Juego;
import jugadores.Jugador;
import jugadores.Mazo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaDuelo extends JFrame implements vistaJuego {

    private static final Color FONDO_OSCURO = new Color(8,   8,  25);
    private static final Color FONDO_PANEL = new Color(15, 15,  40);
    private static final Color FONDO_PANEL_ENEMIGO = new Color(30,  8,   8);
    private static final Color FONDO_PANEL_JUGADOR = new Color( 8, 20,   8);
    private static final Color COLOR_DORADO = new Color(255, 215,  0);
    private static final Color COLOR_DORADO_OSCURO_COLOR = new Color(160, 120,  0);
    private static final Color COLOR_TEXTO = new Color(210, 210, 240);
    private static final Color COLOR_LP_NORMAL = new Color( 50, 200,  50);
    private static final Color COLOR_LP_BAJO = new Color(220,  50,  50);
    private static final Color COLOR_LP_MEDIO = new Color(220, 180,   0);
    private static final Color COLOR_CARTA_MONSTRUO = new Color( 20,  40,  80);
    private static final Color COLOR_CARTA_MAGIA = new Color( 10,  60,  30);
    private static final Color COLOR_CARTA_TRAMPA = new Color( 50,  10,  50);
    private static final Color COLOR_BORDE = new Color( 80,  60,  10);
    private static final Color COLOR_LOG_FONDO = new Color(  5,   5,  20);
    private static final Color COLOR_DORADO_OSCURO = new Color(160, 120,  0);

    private final Juego juego;
    private final Jugador jugador1;
    private final Jugador jugador2;
    private final controladorJuego controlador;

    private JLabel labelNombreJ1, labelLpJ1, labelMazoJ1;
    private JLabel labelNombreJ2, labelLpJ2, labelMazoJ2;
    private JLabel labelTurnoActual, labelFase;
    private JLabel labelTrampasOponente;
    private JPanel panelCampoOponente, panelCampoJugador, panelManoJugador;
    private JTextArea areaLog;
    private JButton botonRobar, botonTerminarTurno;

    public VentanaDuelo(String nombre1, String nombre2) {
        super("Yu-Gi-Oh! — " + nombre1 + " VS " + nombre2);

        jugador1 = new Jugador(nombre1);
        jugador2 = new Jugador(nombre2);
        Mazo.repartir(jugador1, jugador2);
        juego = new Juego(jugador1, jugador2);

        controlador = new controladorJuego(juego, this);

        inicializarVentana();
        construirUI();
        actualizarEstado(juego);

        registrarEnLog("════════════════════════════════════════");
        registrarEnLog("   ¡¡ DUELO INICIADO !!  ");
        registrarEnLog("  " + nombre1 + " VS " + nombre2);
        registrarEnLog("  ¡" + juego.getJugadorActual().getNombre() + " va primero!");
        registrarEnLog("════════════════════════════════════════");
        registrarEnLog("  → Presiona 'Robar Carta' para comenzar");
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        registrarEnLog(mensaje);
    }

    @Override
    public void actualizarEstado(Juego juego) {
        actualizarPanelLP();
        actualizarCampoOponente();
        actualizarCampoJugador();
        actualizarManoJugador();
        labelTurnoActual.setText("TURNO: " + juego.getJugadorActual().getNombre().toUpperCase());
    }

    @Override
    public int elegirOpcionMenu(String titulo, String[] opciones) {
        String elegida = (String) JOptionPane.showInputDialog(
                this, titulo, "Acción",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (elegida == null) return -1;
        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(elegida)) return i;
        }
        return -1;
    }

    @Override
    public Monstruo elegirMonstruo(List<Monstruo> lista, String titulo) {
        if (lista.isEmpty()) return null;

        String[] ops = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            Monstruo m = lista.get(i);
            String pos = m.isEnPosicionAtaque() ? "ATQ" : "DEF";
            ops[i] = m.getNombre() + "  Lv." + m.getNivel()
                    + " [" + pos + "]  ATK:" + m.getAtk() + " DEF:" + m.getDef();
        }

        String elegida = (String) JOptionPane.showInputDialog(
                this, titulo, "Elige monstruo",
                JOptionPane.QUESTION_MESSAGE, null, ops, ops[0]);
        if (elegida == null) return null;

        for (int i = 0; i < ops.length; i++) {
            if (ops[i].equals(elegida)) return lista.get(i);
        }
        return null;
    }

    @Override
    public CartaTrampa elegirTrampa(List<CartaTrampa> lista, String titulo) {
        if (lista.isEmpty()) return null;

        String[] ops = new String[lista.size() + 1];
        ops[0] = "No activar ninguna trampa";
        for (int i = 0; i < lista.size(); i++) {
            ops[i + 1] = lista.get(i).getNombre() + " — " + lista.get(i).getDescripcion();
        }

        String elegida = (String) JOptionPane.showInputDialog(
                this, titulo, "¡¡ TRAMPA !!",
                JOptionPane.WARNING_MESSAGE, null, ops, ops[0]);

        if (elegida == null || elegida.equals(ops[0])) return null;

        for (int i = 0; i < lista.size(); i++) {
            if (ops[i + 1].equals(elegida)) return lista.get(i);
        }
        return null;
    }

    @Override
    public boolean confirmar(String titulo, String mensaje) {
        int resp = JOptionPane.showConfirmDialog(this, mensaje, titulo, JOptionPane.YES_NO_OPTION);
        return resp == JOptionPane.YES_OPTION;
    }

    @Override
    public void mostrarGanador(Juego juego) {
        String ganador    = juego.getNombreGanador();
        Jugador jGan      = juego.getGanador();
        Jugador jPerdedor = (jGan == jugador1) ? jugador2 : jugador1;

        registrarEnLog("");
        registrarEnLog("════════════════════════════════════════");
        registrarEnLog("   FIN DEL DUELO ");
        registrarEnLog("  ¡¡ " + ganador + " GANA EL DUELO !!");
        registrarEnLog("  LP finales: " + jGan.getNombre() + " → " + jGan.getVida());
        registrarEnLog("  LP finales: " + jPerdedor.getNombre() + " → " + jPerdedor.getVida());
        registrarEnLog("════════════════════════════════════════");

        botonRobar.setEnabled(false);
        botonTerminarTurno.setEnabled(false);

        JOptionPane.showMessageDialog(this,
                "  FIN DEL DUELO  \n\n"
                + "¡¡ " + ganador.toUpperCase() + " GANA EL DUELO !!\n\n"
                + jGan.getNombre() + " termina con " + jGan.getVida() + " LP\n"
                + jPerdedor.getNombre() + " termina con " + jPerdedor.getVida() + " LP\n\n"
                + "\"GG buena partida\"\n— Yugioh",
                "¡Duelo Terminado!", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public boolean ofrecerNuevoDuelo() {
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Quieres iniciar un nuevo duelo?",
                "Nuevo Duelo", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            this.dispose();
            new PantallaInicio().setVisible(true);
            return true;
        }
        System.exit(0);
        return false;
    }

    @Override
    public void mostrarInfo(String titulo, String contenido) {
        JTextArea area = new JTextArea(contenido);
        area.setEditable(false);
        area.setBackground(COLOR_LOG_FONDO);
        area.setForeground(COLOR_TEXTO);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(380, 280));
        JOptionPane.showMessageDialog(this, scroll, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    private void accionRobar() {
        boolean ok = controlador.accionRobar();
        if (!ok) {
            controlador.verificarFin();
            return;
        }
        botonRobar.setEnabled(false);
        labelFase.setText("[Fase Principal]");
        if (controlador.verificarFin()) return;
        ofrecerNuevoDuelo();
    }

    private void accionJugarCartaDeMano(Carta carta) {
        if (!controlador.isYaRoboEsteTurno()) {
            JOptionPane.showMessageDialog(this, "Primero debes robar tu carta",
                    "Acción no disponible", JOptionPane.WARNING_MESSAGE);
            return;
        }
        controlador.accionJugarCarta(carta);
        if (controlador.verificarFin()) ofrecerNuevoDuelo();
    }

    private void accionAtacar() {
        controlador.accionAtacar();
        if (controlador.verificarFin()) ofrecerNuevoDuelo();
    }

    private void accionTerminarTurno() {
        if (!controlador.isYaRoboEsteTurno()) {
            int ok = JOptionPane.showConfirmDialog(this,
                    "¡No has robado tu carta este turno!  ¿Seguro que quieres terminar?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (ok != JOptionPane.YES_OPTION) return;
        }

        controlador.accionTerminarTurno();

        botonRobar.setEnabled(true);
        labelFase.setText("[Inicio del Turno]");

        JOptionPane.showMessageDialog(this,
                "¡Fin del turno!\n\nAhora le toca a: "
                        + juego.getJugadorActual().getNombre(),
                "Cambio de Turno", JOptionPane.INFORMATION_MESSAGE);
    }

    private void accionCementerio() {
        Jugador actual  = juego.getJugadorActual();
        Jugador enemigo = juego.getJugadorEnemigo();
        StringBuilder sb = new StringBuilder();
        sb.append("=== Cementerio de ").append(actual.getNombre()).append(" ===\n");
        appendCementerio(sb, actual);
        sb.append("\n=== Cementerio de ").append(enemigo.getNombre()).append(" ===\n");
        appendCementerio(sb, enemigo);
        mostrarInfo(" Cementerio", sb.toString());
    }

    private void appendCementerio(StringBuilder sb, Jugador j) {
        if (j.getCementerio().isEmpty()) { sb.append("  (vacío)\n"); return; }
        for (Carta c : j.getCementerio()) {
            sb.append("  · ").append(c.getNombre()).append(" [").append(c.getTipo()).append("]\n");
        }
    }

    private void inicializarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 780);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        getContentPane().setBackground(FONDO_OSCURO);
        setLayout(new BorderLayout(4, 4));
    }

    private void construirUI() {
        add(crearPanelSuperior(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setBackground(FONDO_OSCURO);
        split.setBorder(null);
        split.setDividerSize(4);
        split.setDividerLocation(700);
        split.setLeftComponent(crearPanelCampoBatalla());
        split.setRightComponent(crearPanelDerechoLog());
        add(split, BorderLayout.CENTER);

        add(crearPanelMano(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(new Color(12, 12, 35));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_DORADO_OSCURO),
                new EmptyBorder(8, 12, 8, 12)));

        // Jugador 1 (izquierda)
        JPanel pJ1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pJ1.setBackground(new Color(12, 12, 35));
        labelNombreJ1 = makeLabel("J1", Font.BOLD, 14, COLOR_DORADO);
        labelLpJ1     = makeLabel("LP: 8000", Font.BOLD, 16, COLOR_LP_NORMAL);
        labelMazoJ1   = makeLabel("Mazo: 20", Font.PLAIN, 12, COLOR_TEXTO);
        pJ1.add(labelNombreJ1); pJ1.add(labelLpJ1); pJ1.add(labelMazoJ1);

        // Centro — turno
        JPanel pCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        pCentro.setBackground(new Color(12, 12, 35));
        labelTurnoActual = makeLabel("TURNO DE: ...", Font.PLAIN, 18, new Color(255, 255, 180));
        labelTurnoActual.setFont(new Font("Impact", Font.PLAIN, 18));
        labelFase = makeLabel("", Font.ITALIC, 13, new Color(180, 180, 220));
        pCentro.add(labelTurnoActual); pCentro.add(labelFase);

        // Jugador 2 (derecha)
        JPanel pJ2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pJ2.setBackground(new Color(12, 12, 35));
        labelNombreJ2 = makeLabel("J2", Font.BOLD, 14, new Color(200, 100, 100));
        labelLpJ2     = makeLabel("LP: 8000", Font.BOLD, 16, COLOR_LP_NORMAL);
        labelMazoJ2   = makeLabel("Mazo: 20", Font.PLAIN, 12, COLOR_TEXTO);
        pJ2.add(labelMazoJ2); pJ2.add(labelLpJ2); pJ2.add(labelNombreJ2);

        panel.add(pJ1,     BorderLayout.WEST);
        panel.add(pCentro, BorderLayout.CENTER);
        panel.add(pJ2,     BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelCampoBatalla() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 6));
        panel.setBackground(FONDO_OSCURO);
        panel.setBorder(new EmptyBorder(6, 6, 6, 3));

        JPanel zonaOp = new JPanel(new BorderLayout(0, 4));
        zonaOp.setBackground(FONDO_PANEL_ENEMIGO);
        zonaOp.setBorder(titledBorder(" Campo del Oponente", new Color(200, 80, 80)));

        labelTrampasOponente = makeLabel("  Trampas ocultas: 0  ", Font.ITALIC, 11, new Color(180, 100, 180));
        labelTrampasOponente.setHorizontalAlignment(SwingConstants.RIGHT);
        labelTrampasOponente.setBorder(new EmptyBorder(2, 0, 2, 6));

        panelCampoOponente = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panelCampoOponente.setBackground(FONDO_PANEL_ENEMIGO);
        panelCampoOponente.setPreferredSize(new Dimension(0, 120));
        panelCampoOponente.add(placeholderLabel("(Campo vacío)", new Color(100, 60, 60)));

        zonaOp.add(labelTrampasOponente, BorderLayout.NORTH);
        zonaOp.add(panelCampoOponente,   BorderLayout.CENTER);

        // Mi campo
        JPanel zonaJug = new JPanel(new BorderLayout(0, 4));
        zonaJug.setBackground(FONDO_PANEL_JUGADOR);
        zonaJug.setBorder(titledBorder(" Mi Campo", new Color(80, 200, 80)));

        panelCampoJugador = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panelCampoJugador.setBackground(FONDO_PANEL_JUGADOR);
        panelCampoJugador.setPreferredSize(new Dimension(0, 120));
        panelCampoJugador.add(placeholderLabel("(Campo vacío)", new Color(60, 100, 60)));

        zonaJug.add(panelCampoJugador, BorderLayout.CENTER);

        panel.add(zonaOp);
        panel.add(zonaJug);
        return panel;
    }

    private JPanel crearPanelDerechoLog() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(FONDO_OSCURO);
        panel.setBorder(new EmptyBorder(6, 3, 0, 6));

        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setBackground(COLOR_LOG_FONDO);
        areaLog.setForeground(new Color(180, 220, 180));
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setBorder(new EmptyBorder(6, 6, 6, 6));

        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(titledBorder(" Log del Duelo", COLOR_DORADO_OSCURO));
        scrollLog.setPreferredSize(new Dimension(0, 300));

        panel.add(scrollLog,            BorderLayout.CENTER);
        panel.add(crearPanelBotones(),  BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 6, 6));
        panel.setBackground(FONDO_OSCURO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                titledBorder("Acciones", COLOR_DORADO_OSCURO),
                new EmptyBorder(4, 4, 4, 4)));

        botonRobar = botonAccion("Robar Carta", new Color(0, 80, 120));
        botonRobar.addActionListener(e -> accionRobar());

        JButton botonAtacar = botonAccion("Atacar", new Color(100, 20, 20));
        botonAtacar.addActionListener(e -> accionAtacar());

        JButton botonCementerio = botonAccion("Cementerio", new Color(40, 40, 40));
        botonCementerio.addActionListener(e -> accionCementerio());

        botonTerminarTurno = botonAccion("Terminar Turno", new Color(60, 40, 0));
        botonTerminarTurno.addActionListener(e -> accionTerminarTurno());

        panel.add(botonRobar);
        panel.add(botonAtacar);
        panel.add(botonCementerio);
        panel.add(botonTerminarTurno);
        return panel;
    }

    private JPanel crearPanelMano() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO_OSCURO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, COLOR_DORADO_OSCURO),
                new EmptyBorder(4, 6, 6, 6)));

        JLabel tituloMano = makeLabel("   MI MANO  ", Font.BOLD, 12, COLOR_DORADO);
        panel.add(tituloMano, BorderLayout.WEST);

        panelManoJugador = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panelManoJugador.setBackground(FONDO_OSCURO);
        panelManoJugador.setPreferredSize(new Dimension(0, 105));

        JScrollPane scroll = new JScrollPane(panelManoJugador, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(null);
        scroll.setBackground(FONDO_OSCURO);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void actualizarPanelLP() {
        labelNombreJ1.setText(jugador1.getNombre());
        labelLpJ1.setText("LP: " + jugador1.getVida());
        labelLpJ1.setForeground(colorLP(jugador1.getVida()));
        labelMazoJ1.setText(" Mazo: " + jugador1.getCartasMazo());

        labelNombreJ2.setText(jugador2.getNombre());
        labelLpJ2.setText("LP: " + jugador2.getVida());
        labelLpJ2.setForeground(colorLP(jugador2.getVida()));
        labelMazoJ2.setText("Mazo: " + jugador2.getCartasMazo() + "  ");
    }

    private void actualizarCampoOponente() {
        Jugador enemigo = juego.getJugadorEnemigo();
        panelCampoOponente.removeAll();

        if (enemigo.getCampo().isEmpty()) {
            panelCampoOponente.add(placeholderLabel("(Campo vacío)", new Color(100, 60, 60)));
        } else {
            for (Monstruo m : enemigo.getCampo()) {
                JButton btn = cartaMonstruoBtn(m, false);
                btn.addActionListener(e -> infoMonstruo(m));
                panelCampoOponente.add(btn);
            }
        }
        labelTrampasOponente.setText("Trampas ocultas: " + enemigo.getTrampas().size() + "  ");
        panelCampoOponente.revalidate();
        panelCampoOponente.repaint();
    }

    private void actualizarCampoJugador() {
        Jugador actual = juego.getJugadorActual();
        panelCampoJugador.removeAll();

        if (actual.getCampo().isEmpty()) {
            panelCampoJugador.add(placeholderLabel("(Campo vacío)", new Color(60, 100, 60)));
        } else {
            for (Monstruo m : actual.getCampo()) {
                JButton btn = cartaMonstruoBtn(m, true);
                btn.addActionListener(e -> infoMonstruo(m));
                panelCampoJugador.add(btn);
            }
        }
        panelCampoJugador.revalidate();
        panelCampoJugador.repaint();
    }

    private void actualizarManoJugador() {
        Jugador actual = juego.getJugadorActual();
        panelManoJugador.removeAll();

        if (actual.getMano().isEmpty()) {
            panelManoJugador.add(placeholderLabel("(Sin cartas en mano)", new Color(100, 100, 100)));
        } else {
            for (Carta c : actual.getMano()) {
                JButton btn = cartaManoBtn(c);
                Carta ref = c;
                btn.addActionListener(e -> accionJugarCartaDeMano(ref));
                panelManoJugador.add(btn);
            }
        }
        panelManoJugador.revalidate();
        panelManoJugador.repaint();
    }

    private JButton cartaMonstruoBtn(Monstruo m, boolean esMio) {
        String pos     = m.isEnPosicionAtaque() ? "ATQ" : "DEF";
        String yaAtaco = (esMio && !m.puedeAtacar()) ? "<br><font color='#FF6666'>ya atacó</font>" : "";
        String html    = "<html><center><b>" + m.getNombre() + "</b><br>"
                + "Lv." + m.getNivel() + " [" + pos + "]<br>"
                + "ATK:" + m.getAtk() + " DEF:" + m.getDef() + yaAtaco
                + "</center></html>";

        JButton btn = new JButton(html);
        btn.setPreferredSize(new Dimension(110, 90));
        btn.setFont(new Font("Arial", Font.PLAIN, 10));
        btn.setForeground(COLOR_TEXTO);
        btn.setBackground(COLOR_CARTA_MONSTRUO);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (esMio && !m.puedeAtacar()) {
            btn.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
            btn.setBackground(new Color(15, 25, 50));
        } else {
            Color borde = esMio ? COLOR_LP_NORMAL : new Color(180, 60, 60);
            btn.setBorder(BorderFactory.createLineBorder(borde, 2));
        }
        return btn;
    }

    private JButton cartaManoBtn(Carta c) {
        Color fondo;
        String info;
        if (c instanceof Monstruo) {
            Monstruo m = (Monstruo) c;
            fondo = COLOR_CARTA_MONSTRUO;
            info  = "ATK:" + m.getAtk() + " DEF:" + m.getDef()
                    + " Lv." + m.getNivel()
                    + (m.necesitaSacrificio() ? " SACR" : "");
        } else if (c instanceof CartaMagica) {
            fondo = COLOR_CARTA_MAGIA;
            info  = "Efecto mágico";
        } else {
            fondo = COLOR_CARTA_TRAMPA;
            info  = "Trampa";
        }

        String html = "<html><center><b>" + c.getNombre() + "</b><br>"
                + "<font size='2'>[" + c.getTipo() + "]<br>" + info + "</font>"
                + "</center></html>";

        JButton btn = new JButton(html);
        btn.setPreferredSize(new Dimension(100, 85));
        btn.setFont(new Font("Arial", Font.PLAIN, 10));
        btn.setForeground(COLOR_TEXTO);
        btn.setBackground(fondo);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 2));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("<html><b>" + c.getNombre() + "</b><br>" + c.getDescripcion() + "</html>");

        Color original = fondo;
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(fondo.brighter());
                btn.setBorder(BorderFactory.createLineBorder(COLOR_DORADO, 2));
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(original);
                btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 2));
            }
        });
        return btn;
    }

    private void registrarEnLog(String msg) {
        areaLog.append(msg + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    private void infoMonstruo(Monstruo m) {
        String info = "Nombre:  " + m.getNombre() + "\n"
                + "Nivel:  " + m.getNivel() + "\n"
                + "ATK:  " + m.getAtk()
                + (m.getAtk() != m.getAtkBase() ? " (base:" + m.getAtkBase() + ")" : "") + "\n"
                + "DEF:  " + m.getDef()
                + (m.getDef() != m.getDefBase() ? " (base:" + m.getDefBase() + ")" : "") + "\n"
                + "Posición:   " + (m.isEnPosicionAtaque() ? "ATAQUE" : "DEFENSA") + "\n"
                + "Descripción:   " + m.getDescripcion();
        JOptionPane.showMessageDialog(this, info, "Info: " + m.getNombre(), JOptionPane.INFORMATION_MESSAGE);
    }

    private Color colorLP(int lp) {
        if (lp > 4000) return COLOR_LP_NORMAL;
        if (lp > 2000) return COLOR_LP_MEDIO;
        return COLOR_LP_BAJO;
    }

    private JLabel makeLabel(String texto, int estilo, int tam, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Arial", estilo, tam));
        l.setForeground(color);
        return l;
    }

    private JLabel placeholderLabel(String texto, Color color) {
        JLabel l = new JLabel(texto);
        l.setForeground(color);
        l.setFont(new Font("Arial", Font.ITALIC, 12));
        return l;
    }

    private JButton botonAccion(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setForeground(COLOR_TEXTO);
        btn.setBackground(fondo);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(fondo.brighter()); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(fondo); }
        });
        return btn;
    }

    private TitledBorder titledBorder(String titulo, Color color) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(color, 1),
                titulo,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 11), color);
    }
}
