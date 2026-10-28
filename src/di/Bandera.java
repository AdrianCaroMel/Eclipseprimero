package di;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class Bandera extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel panelBanderas;
    private JLabel lblPuntos;
    private int puntos = 0;

    // =========================================================
    // CONSTRUCTOR: Se ejecuta nada más crear la ventana
    // =========================================================
    public Bandera() {
        super("Banderas Jogo");
        setSize(800, 500);
        setLocation(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        // -----------------------------------------------------
        // 1. MENÚ 
        // -----------------------------------------------------
        JMenuBar barraMenu = new JMenuBar();
        JMenu archivo = new JMenu("Archivo");
        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        archivo.add(salir);
        barraMenu.add(archivo);
        setJMenuBar(barraMenu);
        // -----------------------------------------------------
        // 2. PARTE SUPERIOR: Título a la izq. y Puntos a la der.
        // -----------------------------------------------------
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        JLabel titulo = new JLabel("Juego de las banderas");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelSuperior.add(titulo, BorderLayout.WEST);
        lblPuntos = new JLabel("Puntos: 0");
        panelSuperior.add(lblPuntos, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);
        // -----------------------------------------------------
        // 3. PANEL CENTRAL: Donde irán las banderas
        // -----------------------------------------------------
        panelBanderas = new JPanel();
        panelBanderas.setLayout(new BoxLayout(panelBanderas, BoxLayout.Y_AXIS));
        panelBanderas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(panelBanderas);
        add(scroll, BorderLayout.CENTER);
        // -----------------------------------------------------
        // 4. AÑADIR LAS BANDERAS COMPLETAS
        // -----------------------------------------------------
        agregarBandera("C:\\Users\\Tarde\\Desktop\\eclipse\\Programas\\src\\banderasJuego\\CoreaSur.png", 
                new String[]{"Corea del sur", "Maresama", "Sulfatima"}, "Corea del Sur");
        agregarBandera("C:\\Users\\Tarde\\Desktop\\eclipse\\Programas\\src\\banderasJuego\\ReinoUnido.png", 
                new String[]{"Reino Unido", "Gran Bretaña", "Britania"}, "Reino Unido");
        agregarBandera("C:\\Users\\Tarde\\Desktop\\eclipse\\Programas\\src\\banderasJuego\\Kosovo.png", 
                new String[]{"Islandia", "Kosovo", "Skypia"}, "Kosovo");
        agregarBandera("C:\\Users\\Tarde\\Desktop\\eclipse\\Programas\\src\\banderasJuego\\Kiribati.png", 
                new String[]{"Himnadic", "Jamacuco", "Kiribati"}, "Kiribati");
        agregarBandera("C:\\Users\\Tarde\\Desktop\\eclipse\\Programas\\src\\banderasJuego\\Madagascar.png", 
                new String[]{"Pakichu", "Sabanaland", "Madagascar"}, "Madagascar");
        agregarBandera("C:\\Users\\Tarde\\Desktop\\eclipse\\Programas\\src\\banderasJuego\\Seychelles.png", 
                new String[]{"Schrodinger", "Seychelles", "Rascaypica"}, "Seychelles");
        agregarBandera("C:\\Users\\Tarde\\Desktop\\eclipse\\Programas\\src\\banderasJuego\\BurkinaFaso.png", 
                new String[]{"BurkinaFaso", "Lolalolitaland", "Islas Marshall"}, "Burkina Faso");
    }
    // =========================================================
    // MÉTODO AGREGAR BANDERA: Crea 1 imagen y 1 botón
    // =========================================================
    private void agregarBandera(String rutaImagen, String[] opciones, String respuestaCorrecta) {
        File archivo = new File(rutaImagen);
        if (!archivo.isFile()) {
            System.out.println("Error: No se encuentra la imagen " + rutaImagen);
            return;
        }
        ImageIcon imagenOriginal = new ImageIcon(rutaImagen);
        Image imagenEscalada = imagenOriginal.getImage().getScaledInstance(150, 85, Image.SCALE_SMOOTH);
        JLabel etiquetaBandera = new JLabel(new ImageIcon(imagenEscalada));
        etiquetaBandera.setPreferredSize(new Dimension(160, 95));
        etiquetaBandera.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaBandera.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        BotonBandera boton = new BotonBandera("¿Qué bandera es?");
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogoPregunta dialogo = new DialogoPregunta(Bandera.this, opciones);
                dialogo.setVisible(true);
                String respuesta = dialogo.getRespuestaSeleccionada();
                if (respuesta == null) return;
                if (respuesta.equalsIgnoreCase(respuestaCorrecta)) {
                    puntos++;
                    lblPuntos.setText("Puntos: " + puntos);
                    JOptionPane.showMessageDialog(Bandera.this, "Has acertado", "Correcto", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(Bandera.this, "Has Fallado", "Incorrecto", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.add(etiquetaBandera);
        fila.add(boton);
        panelBanderas.add(fila);
        panelBanderas.add(new JSeparator());
    }

    // =========================================================
    // CLASE PERSONALIZADA: NUESTRO PROPIO BOTÓN
    // =========================================================
    static class BotonBandera extends JButton {
        private static final long serialVersionUID = 1L;

        public BotonBandera(String texto) {
            super(texto);
            setFont(new Font("Arial", Font.BOLD, 13));
            setBackground(new Color(45, 105, 170));
            setForeground(Color.WHITE);
            setOpaque(true);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(155, 38));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(30, 75, 125)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }
    }

    // =========================================================
    // CLASE INTERNA: EL MENÚ DE OPCIONES CON RADIOBUTTONS
    // =========================================================
    static class DialogoPregunta extends JDialog {
        private static final long serialVersionUID = 1L;
        private String respuestaSeleccionada = null;
        private ArrayList<JRadioButton> radios = new ArrayList<>();

        public DialogoPregunta(Frame propietario, String[] opciones) {
            super(propietario, "¿Qué bandera es?", true); // "true" lo hace modal
            setLayout(new BorderLayout(10, 10));
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            JLabel pregunta = new JLabel("¿Qué bandera es?", SwingConstants.CENTER);
            pregunta.setFont(new Font("Arial", Font.BOLD, 16));
            add(pregunta, BorderLayout.NORTH);

            JPanel panelOpciones = new JPanel();
            panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
            panelOpciones.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

            ButtonGroup grupo = new ButtonGroup();
            for (int i = 0; i < opciones.length; i++) {
                JRadioButton radio = new JRadioButton(opciones[i]);
                grupo.add(radio);
                radios.add(radio);
                panelOpciones.add(radio);
            }
            add(panelOpciones, BorderLayout.CENTER);

            JButton aceptar = new JButton("Aceptar");
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panelBotones.add(aceptar);
            add(panelBotones, BorderLayout.SOUTH);

            aceptar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < radios.size(); i++) {
                        if (radios.get(i).isSelected()) {
                            respuestaSeleccionada = radios.get(i).getText();
                            dispose();
                            return;
                        }
                    }
                    JOptionPane.showMessageDialog(DialogoPregunta.this, "Marca una opción.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });

            pack();
            setMinimumSize(new Dimension(300, 180));
            setLocationRelativeTo(propietario);
        }

        public String getRespuestaSeleccionada() {
            return respuestaSeleccionada;
        }
    }

    // ==========================================================================================
    // MAIN: El motor de arranque crear ventana que en el principio le damos propiedades
    // ==========================================================================================
    public static void main(String[] args) {
        Bandera ventana = new Bandera();
        ventana.setVisible(true);    
    }
}