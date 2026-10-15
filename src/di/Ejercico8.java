package di;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

public class Ejercico8 {

	static JTextArea areaTexto;
	static JComboBox<String> cajaFuentes;
	static JSpinner spinnerTamano;
	static JSlider sliderTamano;
	static JCheckBox chkNegrita;
	static JCheckBox chkCursiva;
	static JCheckBoxMenuItem menuNegrita;
	static JCheckBoxMenuItem menuCursiva;

	public static void main(String[] args) {

		JFrame ventana = new JFrame("Editor de Texto");
		ventana.setSize(600, 500);
		ventana.setLocation(100, 100);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setLayout(new BorderLayout());

		// menu de archivo
		JMenuBar barraMenu = new JMenuBar();
		JMenu menuArchivo = new JMenu("Archivo");
		JMenuItem itemNuevo = new JMenuItem("Nuevo");
		JMenuItem itemSalir = new JMenuItem("Salir");
		menuArchivo.add(itemNuevo);
		menuArchivo.add(itemSalir);

		// menu de estilo
		JMenu menuEstilo = new JMenu("Estilo");
		menuNegrita = new JCheckBoxMenuItem("Negrita");
		menuCursiva = new JCheckBoxMenuItem("Cursiva");
		menuEstilo.add(menuNegrita);
		menuEstilo.add(menuCursiva);

		barraMenu.add(menuArchivo);
		barraMenu.add(menuEstilo);
		ventana.setJMenuBar(barraMenu);

		// area de texto con scroll
		areaTexto = new JTextArea();
		areaTexto.setFont(new Font("Arial", Font.PLAIN, 14));
		JScrollPane scroll = new JScrollPane(areaTexto);

		// panel de arriba con los controles
		JPanel panelSuperior = new JPanel(new FlowLayout());

		GraphicsEnvironment entorno = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] nombresFuentes = entorno.getAvailableFontFamilyNames();
		cajaFuentes = new JComboBox<String>(nombresFuentes);
		cajaFuentes.setSelectedItem("Arial");

		spinnerTamano = new JSpinner(new SpinnerNumberModel(14, 8, 72, 1));
		sliderTamano = new JSlider(8, 72, 14);

		chkNegrita = new JCheckBox("Negrita");
		chkCursiva = new JCheckBox("Cursiva");

		panelSuperior.add(cajaFuentes);
		panelSuperior.add(spinnerTamano);
		panelSuperior.add(sliderTamano);
		panelSuperior.add(chkNegrita);
		panelSuperior.add(chkCursiva);

		ventana.add(panelSuperior, BorderLayout.NORTH);
		ventana.add(scroll, BorderLayout.CENTER);

		// nuevo borra el area de texto
		itemNuevo.addActionListener(e -> {
			areaTexto.setText("");
		});

		// salir cierra el programa
		itemSalir.addActionListener(e -> {
			System.exit(0);
		});

		// cambiar la fuente cuando se elige otra en el combo
		cajaFuentes.addActionListener(e -> {
			String fuente = (String) cajaFuentes.getSelectedItem();
			int tamano = (int) spinnerTamano.getValue();
			int estilo = Font.PLAIN;
			if (chkNegrita.isSelected() == true && chkCursiva.isSelected() == true) {
				estilo = Font.BOLD + Font.ITALIC;
			} else if (chkNegrita.isSelected() == true) {
				estilo = Font.BOLD;
			} else if (chkCursiva.isSelected() == true) {
				estilo = Font.ITALIC;
			}
			areaTexto.setFont(new Font(fuente, estilo, tamano));
		});

		// spinner cambia el tamano de letra
		spinnerTamano.addChangeListener(e -> {
			int tamano = (int) spinnerTamano.getValue();
			sliderTamano.setValue(tamano);
			Font actual = areaTexto.getFont();
			areaTexto.setFont(new Font(actual.getFamily(), actual.getStyle(), tamano));
		});

		// slider cambia el tamano de letra tambien
		sliderTamano.addChangeListener(e -> {
			int tamano = sliderTamano.getValue();
			spinnerTamano.setValue(tamano);
			Font actual = areaTexto.getFont();
			areaTexto.setFont(new Font(actual.getFamily(), actual.getStyle(), tamano));
		});

		// checkbox negrita
		chkNegrita.addActionListener(e -> {
			menuNegrita.setSelected(chkNegrita.isSelected());
			int estilo = Font.PLAIN;
			if (chkNegrita.isSelected() == true && chkCursiva.isSelected() == true) {
				estilo = Font.BOLD + Font.ITALIC;
			} else if (chkNegrita.isSelected() == true) {
				estilo = Font.BOLD;
			} else if (chkCursiva.isSelected() == true) {
				estilo = Font.ITALIC;
			}
			Font actual = areaTexto.getFont();
			areaTexto.setFont(new Font(actual.getFamily(), estilo, actual.getSize()));
		});

		// checkbox cursiva
		chkCursiva.addActionListener(e -> {
			menuCursiva.setSelected(chkCursiva.isSelected());
			int estilo = Font.PLAIN;
			if (chkNegrita.isSelected() == true && chkCursiva.isSelected() == true) {
				estilo = Font.BOLD + Font.ITALIC;
			} else if (chkNegrita.isSelected() == true) {
				estilo = Font.BOLD;
			} else if (chkCursiva.isSelected() == true) {
				estilo = Font.ITALIC;
			}
			Font actual = areaTexto.getFont();
			areaTexto.setFont(new Font(actual.getFamily(), estilo, actual.getSize()));
		});

		// menu negrita hace lo mismo que el checkbox
		menuNegrita.addActionListener(e -> {
			chkNegrita.setSelected(menuNegrita.isSelected());
			int estilo = Font.PLAIN;
			if (chkNegrita.isSelected() == true && chkCursiva.isSelected() == true) {
				estilo = Font.BOLD + Font.ITALIC;
			} else if (chkNegrita.isSelected() == true) {
				estilo = Font.BOLD;
			} else if (chkCursiva.isSelected() == true) {
				estilo = Font.ITALIC;
			}
			Font actual = areaTexto.getFont();
			areaTexto.setFont(new Font(actual.getFamily(), estilo, actual.getSize()));
		});

		// menu cursiva hace lo mismo que el checkbox
		menuCursiva.addActionListener(e -> {
			chkCursiva.setSelected(menuCursiva.isSelected());
			int estilo = Font.PLAIN;
			if (chkNegrita.isSelected() == true && chkCursiva.isSelected() == true) {
				estilo = Font.BOLD + Font.ITALIC;
			} else if (chkNegrita.isSelected() == true) {
				estilo = Font.BOLD;
			} else if (chkCursiva.isSelected() == true) {
				estilo = Font.ITALIC;
			}
			Font actual = areaTexto.getFont();
			areaTexto.setFont(new Font(actual.getFamily(), estilo, actual.getSize()));
		});

		ventana.setVisible(true);
	}
}