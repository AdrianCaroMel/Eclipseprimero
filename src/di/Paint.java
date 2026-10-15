package di;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Paint {

	// color y grosor actuales de la brocha
	static Color colorActual = Color.BLACK;
	static int grosorActual = 3;

	// forma seleccionada en el combo
	static String formaActual = "Libre";

	// lista con todas las formas que se han dibujado
	static ArrayList<Forma> formas = new ArrayList<Forma>();

	// punto donde empezo la forma actual
	static int xInicio;
	static int yInicio;

	// forma que se esta dibujando ahora mismo (mientras se arrastra el raton)
	static Forma formaEnCurso = null;

	static Lienzo lienzo;

	public static void main(String[] args) {

		JFrame ventana = new JFrame("Mini Paint");
		ventana.setSize(700, 500);
		ventana.setLocation(100, 100);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setLayout(new BorderLayout());

		// barra de herramientas con los controles
		JToolBar barraHerramientas = new JToolBar();

		JButton botonBorrar = new JButton("Borrar");
		JButton botonColor = new JButton("Color");
		JComboBox<String> comboFormas = new JComboBox<String>(new String[] { "Libre", "Linea", "Circulo", "Cuadrado" });
		JSlider sliderGrosor = new JSlider(1, 20, grosorActual);

		barraHerramientas.add(botonBorrar);
		barraHerramientas.add(botonColor);
		barraHerramientas.add(comboFormas);
		barraHerramientas.add(sliderGrosor);

		ventana.add(barraHerramientas, BorderLayout.NORTH);

		// lienzo donde se dibuja
		lienzo = new Lienzo();
		ventana.add(lienzo, BorderLayout.CENTER);

		// borrar vacia la lista de formas y repinta el lienzo en blanco
		botonBorrar.addActionListener(e -> {
			formas.clear();
			lienzo.repaint();
		});

		// abre el selector de color y lo guarda como color actual
		botonColor.addActionListener(e -> {
			Color elegido = JColorChooser.showDialog(ventana, "Elige un color", colorActual);
			if (elegido != null) {
				colorActual = elegido;
			}
		});

		// cambia la forma que se va a dibujar
		comboFormas.addActionListener(e -> {
			formaActual = (String) comboFormas.getSelectedItem();
		});

		// cambia el grosor de la brocha
		sliderGrosor.addChangeListener(e -> {
			grosorActual = sliderGrosor.getValue();
		});

		// al pulsar el raton, guardamos el punto donde empieza la forma
		lienzo.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				xInicio = e.getX();
				yInicio = e.getY();
				formaEnCurso = null;
			}
		});

		// mientras se arrastra el raton, vamos dibujando
		lienzo.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {

				if (formaActual.equals("Libre")) {
					// en libre, cada tramo pequeño es una linea nueva
					Forma trazo = new Forma(formaActual, xInicio, yInicio, e.getX(), e.getY(), colorActual, grosorActual);
					formas.add(trazo);
					xInicio = e.getX();
					yInicio = e.getY();

				} else if (formaEnCurso == null) {
					// primera vez que se arrastra: creamos la forma y la añadimos a la lista
					formaEnCurso = new Forma(formaActual, xInicio, yInicio, e.getX(), e.getY(), colorActual, grosorActual);
					formas.add(formaEnCurso);

				} else {
					// seguimos arrastrando: actualizamos el punto final de la misma forma
					formaEnCurso.xFin = e.getX();
					formaEnCurso.yFin = e.getY();
				}

				lienzo.repaint();
			}
		});

		ventana.setVisible(true);
	}

	// panel donde se dibujan todas las formas guardadas
	static class Lienzo extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			for (int i = 0; i < formas.size(); i++) {
				Forma forma = formas.get(i);

				g2.setColor(forma.color);
				g2.setStroke(new BasicStroke(forma.grosor));

				if (forma.tipo.equals("Circulo")) {
					int diametro = Math.abs(forma.xFin - forma.xInicio);
					g2.drawOval(forma.xInicio, forma.yInicio, diametro, diametro);

				} else if (forma.tipo.equals("Cuadrado")) {
					int lado = Math.abs(forma.xFin - forma.xInicio);
					g2.drawRect(forma.xInicio, forma.yInicio, lado, lado);

				} else {
					// libre y linea se dibujan igual, con una linea recta
					g2.drawLine(forma.xInicio, forma.yInicio, forma.xFin, forma.yFin);
				}
			}
		}
	}

	// guarda los datos de una forma dibujada
	static class Forma {
		String tipo;
		int xInicio, yInicio, xFin, yFin;
		Color color;
		int grosor;

		Forma(String tipo, int xInicio, int yInicio, int xFin, int yFin, Color color, int grosor) {
			this.tipo = tipo;
			this.xInicio = xInicio;
			this.yInicio = yInicio;
			this.xFin = xFin;
			this.yFin = yFin;
			this.color = color;
			this.grosor = grosor;
		}
	}
}