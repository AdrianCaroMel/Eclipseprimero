package juegobanderas;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * Ventana principal del juego "Adivina la bandera".
 *
 * Requisitos:
 *  - Banderas en JLabel con un botón personalizado al lado de cada una.
 *  - Al pulsar: JDialog personalizado con JRadioButton "¿Qué bandera es?".
 *  - Acierto: suma puntos en el marcador + mensaje "¡Has acertado!".
 *  - Fallo: mensaje "Has fallado" con icono de aspa roja.
 *  - Marcador = JLabel personalizado (MarcadorLabel).
 *  - Confirmación al salir.
 *
 * Ampliaciones:
 *  - 8 banderas aleatorias de 20 en cada partida y opciones barajadas.
 *  - Cuenta atrás de 15 s: bonus por rapidez; si se agota, es fallo.
 *  - Racha: a partir de 3 aciertos seguidos los puntos valen el doble.
 *  - Dificultad: Fácil (3 opciones) / Difícil (4 opciones de banderas parecidas).
 *  - Cada bandera solo se responde una vez; el botón queda verde/rojo con el país.
 *  - Resumen final con nota sobre 10 y récord de la sesión; botón "Nueva partida".
 *  - Historial de respuestas en una JList con ListCellRenderer propio
 *    (RespuestaCellRenderer): miniatura de bandera, país, puntos y color.
 */
public class JuegoBanderas extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final int BANDERAS_POR_PARTIDA = 8;
    private static final int PUNTOS_BASE = 100;
    private static final int PUNTOS_POR_SEGUNDO = 10;
    private static final int RACHA_PARA_DOBLE = 3;

    private static final Color FONDO = new Color(236, 240, 245);
    private static final Color VERDE = new Color(40, 167, 69);
    private static final Color ROJO = new Color(220, 53, 69);

    private final MarcadorLabel marcador = new MarcadorLabel();
    private final JPanel panelBanderas = new JPanel(new GridLayout(0, 2, 18, 18));
    private final JComboBox<String> comboDificultad =
            new JComboBox<>(new String[]{"Fácil (3 opciones)", "Difícil (4 opciones parecidas)"});
    private final JLabel lblRecord = new JLabel();

    // Historial: JList + modelo + ListCellRenderer personalizado
    private final DefaultListModel<Respuesta> modeloHistorial = new DefaultListModel<>();
    private final JList<Respuesta> listaHistorial = new JList<>(modeloHistorial);
    private final Random random = new Random();

    private int respondidas;
    private int record;

    public JuegoBanderas() {
        super("Adivina la bandera");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);   // controlamos nosotros el cierre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });

        JPanel raiz = new JPanel(new BorderLayout(18, 16));
        raiz.setBackground(FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        setContentPane(raiz);

        raiz.add(crearCabecera(), BorderLayout.NORTH);

        panelBanderas.setOpaque(false);
        raiz.add(panelBanderas, BorderLayout.CENTER);

        raiz.add(crearPanelHistorial(), BorderLayout.EAST);
        raiz.add(crearBarraInferior(), BorderLayout.SOUTH);

        nuevaPartida();
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    // =====================================================================
    //  Construcción de la interfaz
    // =====================================================================

    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout(0, 10));
        cabecera.setOpaque(false);

        JLabel titulo = new JLabel("Adivina la bandera", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(30, 60, 114));

        cabecera.add(titulo, BorderLayout.NORTH);
        cabecera.add(marcador, BorderLayout.CENTER);
        return cabecera;
    }

    private JPanel crearBarraInferior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        izquierda.setOpaque(false);
        JLabel lblDif = new JLabel("Dificultad:");
        lblDif.setFont(new Font("SansSerif", Font.BOLD, 14));
        izquierda.add(lblDif);
        izquierda.add(comboDificultad);
        lblRecord.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRecord.setForeground(new Color(150, 110, 0));
        izquierda.add(Box.createHorizontalStrut(15));
        izquierda.add(lblRecord);
        actualizarRecord();

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        derecha.setOpaque(false);
        BotonPersonalizado btnNueva = new BotonPersonalizado("Nueva partida", new Color(255, 152, 0));
        BotonPersonalizado btnSalir = new BotonPersonalizado("Salir", new Color(108, 117, 125));
        btnNueva.addActionListener(e -> nuevaPartida());
        btnSalir.addActionListener(e -> confirmarSalida());
        derecha.add(btnNueva);
        derecha.add(btnSalir);

        barra.add(izquierda, BorderLayout.WEST);
        barra.add(derecha, BorderLayout.EAST);
        return barra;
    }

    /** Panel lateral con la JList del historial y su ListCellRenderer. */
    private JPanel crearPanelHistorial() {
        listaHistorial.setCellRenderer(new RespuestaCellRenderer());   // <-- el renderer
        listaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaHistorial.setFixedCellWidth(220);
        listaHistorial.setBackground(Color.WHITE);

        // Doble clic en una fila: muestra el detalle de esa respuesta
        listaHistorial.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Respuesta r = listaHistorial.getSelectedValue();
                if (e.getClickCount() == 2 && r != null) {
                    String elegida = r.getRespuestaDada() == null ? "(sin respuesta)" : r.getRespuestaDada();
                    JOptionPane.showMessageDialog(JuegoBanderas.this,
                            "Bandera: " + r.getPais().getNombre() + "\nTu respuesta: " + elegida
                                    + "\nPuntos: " + r.getPuntos(),
                            "Detalle", JOptionPane.PLAIN_MESSAGE,
                            new IconoBandera(r.getPais(), 120, 80));
                }
            }
        });

        JLabel titulo = new JLabel("Historial");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setForeground(new Color(30, 60, 114));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 2, 6, 0));

        JScrollPane scroll = new JScrollPane(listaHistorial);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 225), 2));
        scroll.setPreferredSize(new Dimension(240, 10));   // el alto lo marca el BorderLayout

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    /** Tarjeta = JLabel con la bandera + botón personalizado a su lado. */
    private JPanel crearTarjeta(int numero, Pais pais) {
        JPanel tarjeta = new JPanel(new BorderLayout(16, 0));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(bordeTarjeta(new Color(210, 215, 225)));

        JLabel lblBandera = new JLabel(new IconoBandera(pais, 150, 100));
        lblBandera.setToolTipText("Bandera nº " + numero);

        BotonPersonalizado btn = new BotonPersonalizado("¿Cuál es?");
        btn.setPreferredSize(new Dimension(150, 44));
        btn.addActionListener(e -> preguntar(pais, btn, tarjeta));

        JPanel contenedorBoton = new JPanel(new GridBagLayout());  // centra el botón en vertical
        contenedorBoton.setOpaque(false);
        contenedorBoton.add(btn);

        tarjeta.add(lblBandera, BorderLayout.WEST);
        tarjeta.add(contenedorBoton, BorderLayout.CENTER);
        return tarjeta;
    }

    private static javax.swing.border.Border bordeTarjeta(Color color) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 3, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // =====================================================================
    //  Lógica del juego
    // =====================================================================

    private void nuevaPartida() {
        List<Pais> paises = new ArrayList<>(Arrays.asList(Pais.values()));
        Collections.shuffle(paises, random);

        panelBanderas.removeAll();
        for (int i = 0; i < BANDERAS_POR_PARTIDA; i++) {
            panelBanderas.add(crearTarjeta(i + 1, paises.get(i)));
        }
        respondidas = 0;
        marcador.reiniciar(BANDERAS_POR_PARTIDA);
        modeloHistorial.clear();
        panelBanderas.revalidate();
        panelBanderas.repaint();
    }

    /** Abre el diálogo, comprueba la respuesta y actualiza marcador y tarjeta. */
    private void preguntar(Pais correcto, BotonPersonalizado btn, JPanel tarjeta) {
        DialogoPregunta dialogo = new DialogoPregunta(this, correcto, generarOpciones(correcto));
        String respuesta = dialogo.mostrar();          // bloquea hasta cerrar (modal)

        if (respuesta == null && !dialogo.isTiempoAgotado()) {
            return;                                    // canceló: puede intentarlo luego
        }

        respondidas++;
        btn.setText(correcto.getNombre());
        btn.setEnabled(false);                         // cada bandera se responde una sola vez

        if (correcto.getNombre().equals(respuesta)) {
            int segundos = dialogo.getSegundosRestantes();
            boolean doble = marcador.getRacha() + 1 >= RACHA_PARA_DOBLE;
            int puntos = (PUNTOS_BASE + segundos * PUNTOS_POR_SEGUNDO) * (doble ? 2 : 1);
            marcador.registrarAcierto(puntos);
            modeloHistorial.add(0, new Respuesta(correcto, respuesta, true, puntos)); // la más reciente arriba

            btn.setColorFondo(VERDE);
            tarjeta.setBorder(bordeTarjeta(VERDE));

            String msg = "<html><b style='font-size:15px'>¡Has acertado!</b><br><br>"
                    + "Es la bandera de <b>" + correcto.getNombre() + "</b>.<br>"
                    + "+" + puntos + " puntos (" + PUNTOS_BASE + " + " + segundos * PUNTOS_POR_SEGUNDO
                    + " por rapidez)" + (doble ? "<br><b>¡Racha de " + marcador.getRacha() + "! Puntos x2</b>" : "")
                    + "</html>";
            JOptionPane.showMessageDialog(this, msg, "Resultado", JOptionPane.PLAIN_MESSAGE,
                    new IconoResultado(IconoResultado.Tipo.ACIERTO, 56));
        } else {
            marcador.registrarFallo();
            modeloHistorial.add(0, new Respuesta(correcto, respuesta, false, 0));
            btn.setColorFondo(ROJO);
            tarjeta.setBorder(bordeTarjeta(ROJO));

            String motivo = dialogo.isTiempoAgotado() ? "Se acabó el tiempo.<br>" : "";
            String msg = "<html><b style='font-size:15px'>Has fallado</b><br><br>" + motivo
                    + "Era la bandera de <b>" + correcto.getNombre() + "</b>.</html>";
            JOptionPane.showMessageDialog(this, msg, "Resultado", JOptionPane.PLAIN_MESSAGE,
                    new IconoResultado(IconoResultado.Tipo.FALLO, 56));   // aspa roja
        }

        if (respondidas == BANDERAS_POR_PARTIDA) {
            finPartida();
        }
    }

    /**
     * Opción correcta + incorrectas, barajadas.
     * Difícil: 3 incorrectas, primero del mismo grupo (banderas parecidas).
     * Fácil:   2 incorrectas al azar.
     */
    private List<String> generarOpciones(Pais correcto) {
        boolean dificil = comboDificultad.getSelectedIndex() == 1;

        List<Pais> candidatos = new ArrayList<>(Arrays.asList(Pais.values()));
        candidatos.remove(correcto);
        Collections.shuffle(candidatos, random);
        if (dificil) {
            // Ordenación estable: los del mismo grupo pasan delante manteniendo el azar
            candidatos.sort(Comparator.comparingInt(p -> p.getGrupo() == correcto.getGrupo() ? 0 : 1));
        }

        List<String> opciones = new ArrayList<>();
        opciones.add(correcto.getNombre());
        int incorrectas = dificil ? 3 : 2;
        for (int i = 0; i < incorrectas; i++) {
            opciones.add(candidatos.get(i).getNombre());
        }
        Collections.shuffle(opciones, random);
        return opciones;
    }

    private void finPartida() {
        int puntos = marcador.getPuntos();
        double nota = marcador.getAciertos() * 10.0 / BANDERAS_POR_PARTIDA;
        boolean nuevoRecord = puntos > record;
        if (nuevoRecord) {
            record = puntos;
            actualizarRecord();
        }

        String msg = String.format("<html><b style='font-size:15px'>Fin de la partida</b><br><br>"
                        + "Aciertos: %d de %d<br>Puntos: %d<br>Nota: <b>%.1f</b><br>%s<br>"
                        + "¿Quieres jugar otra partida?</html>",
                marcador.getAciertos(), BANDERAS_POR_PARTIDA, puntos, nota,
                nuevoRecord ? "<b>¡Nuevo récord!</b><br>" : "");

        int op = JOptionPane.showConfirmDialog(this, msg, "Resumen",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (op == JOptionPane.YES_OPTION) {
            nuevaPartida();
        }
    }

    private void actualizarRecord() {
        lblRecord.setText("Récord: " + record + " pts");
    }

    private void confirmarSalida() {
        int op = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres salir?", "Salir",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (op == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JuegoBanderas().setVisible(true));
    }
}
