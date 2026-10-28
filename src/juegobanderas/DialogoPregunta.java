package juegobanderas;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * JDialog personalizado que pregunta "¿Qué bandera es?".
 *
 * - Ventana sin decoración del sistema (undecorated) con cabecera propia
 *   en degradado; se puede arrastrar desde la cabecera.
 * - Opciones con JRadioButton dentro de un ButtonGroup (solo una marcada).
 * - "Aceptar" no se activa hasta que se marca una opción.
 * - Cuenta atrás: si llega a 0 se cierra y cuenta como fallo.
 * - Es MODAL: mostrar() bloquea hasta que el diálogo se cierra.
 */
public class DialogoPregunta extends JDialog {

    private static final long serialVersionUID = 1L;

    public static final int SEGUNDOS = 20;

    private static final Color AZUL_1 = new Color(30, 60, 114);
    private static final Color AZUL_2 = new Color(42, 82, 152);

    private final ButtonGroup grupo = new ButtonGroup();
    private final BotonPersonalizado btnAceptar;
    private final JLabel lblTiempo = new JLabel();
    private final JProgressBar barraTiempo = new JProgressBar(0, SEGUNDOS);
    private final Timer cuentaAtras;

    private String respuesta;            // null = sin respuesta
    private boolean tiempoAgotado;
    private int segundosRestantes = SEGUNDOS;
    private Point inicioArrastre;

    public DialogoPregunta(Frame owner, Pais pais, List<String> opciones) {
        super(owner, "¿Qué bandera es?", true);   // true = modal
        setUndecorated(true);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(Color.WHITE);
        raiz.setBorder(BorderFactory.createLineBorder(AZUL_1, 3));
        setContentPane(raiz);

        // ---------- Cabecera personalizada ----------
        JPanel cabecera = new JPanel(new BorderLayout()) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, AZUL_1, getWidth(), 0, AZUL_2));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        cabecera.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        JLabel lblTitulo = new JLabel("¿Qué bandera es?");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 21));
        lblTitulo.setForeground(Color.WHITE);
        lblTiempo.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblTiempo.setForeground(Color.WHITE);
        cabecera.add(lblTitulo, BorderLayout.WEST);
        cabecera.add(lblTiempo, BorderLayout.EAST);
        hacerArrastrable(cabecera);
        raiz.add(cabecera, BorderLayout.NORTH);

        // ---------- Centro: bandera + opciones ----------
        JPanel centro = new JPanel(new BorderLayout(25, 0));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(20, 22, 12, 22));

        JLabel lblBandera = new JLabel(new IconoBandera(pais, 210, 140));
        lblBandera.setVerticalAlignment(SwingConstants.TOP);
        centro.add(lblBandera, BorderLayout.WEST);

        btnAceptar = new BotonPersonalizado("Aceptar", new Color(40, 167, 69));
        btnAceptar.setEnabled(false);

        JPanel panelOpciones = new JPanel(new GridLayout(0, 1, 0, 6));
        panelOpciones.setOpaque(false);
        for (String opcion : opciones) {
            JRadioButton rb = new JRadioButton(opcion);
            rb.setActionCommand(opcion);                     // lo recuperamos al aceptar
            rb.setFont(new Font("SansSerif", Font.PLAIN, 17));
            rb.setOpaque(false);
            rb.setFocusPainted(false);
            rb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            rb.addActionListener(e -> btnAceptar.setEnabled(true));
            grupo.add(rb);
            panelOpciones.add(rb);
        }
        centro.add(panelOpciones, BorderLayout.CENTER);

        barraTiempo.setValue(SEGUNDOS);
        barraTiempo.setForeground(new Color(42, 82, 152));
        barraTiempo.setPreferredSize(new Dimension(10, 8));
        barraTiempo.setBorderPainted(false);
        centro.add(barraTiempo, BorderLayout.SOUTH);
        raiz.add(centro, BorderLayout.CENTER);

        // ---------- Botones ----------
        BotonPersonalizado btnCancelar = new BotonPersonalizado("Cancelar", new Color(108, 117, 125));
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        sur.setOpaque(false);
        sur.add(btnCancelar);
        sur.add(btnAceptar);
        raiz.add(sur, BorderLayout.SOUTH);

        btnAceptar.addActionListener(e -> {
            respuesta = grupo.getSelection().getActionCommand();
            cerrar();
        });
        btnCancelar.addActionListener(e -> cerrar());   // respuesta sigue a null

        getRootPane().setDefaultButton(btnAceptar);     // Intro = Aceptar
        getRootPane().registerKeyboardAction(e -> cerrar(),  // Esc = Cancelar
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        // ---------- Cuenta atrás ----------
        cuentaAtras = new Timer(1000, e -> {
            segundosRestantes--;
            actualizarTiempo();
            if (segundosRestantes <= 0) {
                tiempoAgotado = true;
                respuesta = null;
                cerrar();
            }
        });
        actualizarTiempo();

        pack();
        setLocationRelativeTo(owner);
    }

    /** Muestra el diálogo (bloquea por ser modal) y devuelve la opción elegida o null. */
    public String mostrar() {
        cuentaAtras.start();
        setVisible(true);       // aquí se queda parado hasta que se hace dispose()
        return respuesta;
    }

    private void actualizarTiempo() {
        lblTiempo.setText(segundosRestantes + " s");
        barraTiempo.setValue(segundosRestantes);
        if (segundosRestantes <= 5) {
            lblTiempo.setForeground(new Color(255, 120, 120));
            barraTiempo.setForeground(new Color(220, 53, 69));
        }
    }

    private void cerrar() {
        cuentaAtras.stop();
        dispose();
    }

    /** Permite mover la ventana arrastrando el componente indicado. */
    private void hacerArrastrable(JComponent zona) {
        MouseAdapter arrastre = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                inicioArrastre = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - inicioArrastre.x, p.y + e.getY() - inicioArrastre.y);
            }
        };
        zona.addMouseListener(arrastre);
        zona.addMouseMotionListener(arrastre);
        zona.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    public boolean isTiempoAgotado()   { return tiempoAgotado; }
    public int getSegundosRestantes()  { return segundosRestantes; }
}
