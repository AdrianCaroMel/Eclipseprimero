package di;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;

public class Paint1 {

	// color con el que se va a dibujar en este momento; empieza en negro
	static Color colorActual = Color.BLACK;

	// grosor de la linea con la que se dibuja; empieza en 3
	static int grosorActual = 3;

	// que tipo de forma esta seleccionada en el combo ("Libre", "Linea", "Circulo" o "Cuadrado")
	static String formaActual = "Libre";

	// lista donde se van guardando TODAS las formas que se han dibujado hasta ahora
	// cada vez que hay que repintar la pantalla, se recorre esta lista entera y se vuelve a dibujar todo
	static ArrayList<Forma> formas = new ArrayList<Forma>();

	// coordenadas donde empieza la forma que se esta dibujando ahora mismo
	// se actualizan cada vez que se pulsa el raton (mousePressed)
	static int xInicio;
	static int yInicio;

	// referencia a la forma que se esta dibujando MIENTRAS se arrastra el raton
	// se pone a null cuando se suelta el raton o se vuelve a pulsar, para indicar que no hay ninguna forma "en construccion"
	static Forma formaEnCurso = null;

	// referencia al lienzo (el panel donde se dibuja), para poder llamar a repaint() desde los listeners
	static Lienzo lienzo;

	public static void main(String[] args) {

		// creamos la ventana principal
		JFrame ventana = new JFrame("Mini Paint");
		ventana.setSize(700, 500);
		ventana.setLocation(100, 100);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setLayout(new BorderLayout());

		// ---------- BARRA DE HERRAMIENTAS ----------
		// un JToolBar es como un panel especial pensado para meter botones y controles en fila
		JToolBar barraHerramientas = new JToolBar();

		// creamos los componentes de la barra de herramientas
		JButton botonBorrar = new JButton("Borrar");
		JButton botonColor = new JButton("Color");
		JComboBox<String> comboFormas = new JComboBox<String>(new String[] { "Libre", "Linea", "Circulo", "Cuadrado" });
		JSlider sliderGrosor = new JSlider(1, 20, grosorActual);

		// los añadimos a la barra de herramientas, en el orden en que queremos que se vean
		barraHerramientas.add(botonBorrar);
		barraHerramientas.add(botonColor);
		barraHerramientas.add(comboFormas);
		barraHerramientas.add(sliderGrosor);

		// metemos la barra de herramientas arriba de la ventana
		ventana.add(barraHerramientas, BorderLayout.NORTH);

		// ---------- LIENZO ----------
		// creamos el panel donde se va a dibujar y lo ponemos en el centro, para que ocupe todo el espacio que sobra
		lienzo = new Lienzo();
		ventana.add(lienzo, BorderLayout.CENTER);

		// ---------- LISTENER DEL BOTON BORRAR ----------
		// clear() vacia la lista de formas por completo
		// repaint() le dice a Swing "vuelve a pintar este panel ahora mismo"
		// como la lista esta vacia, el bucle de paintComponent no dibuja nada y el lienzo queda en blanco
		botonBorrar.addActionListener(e -> {
			formas.clear();
			lienzo.repaint();
		});

		// ---------- LISTENER DEL BOTON COLOR ----------
		// JColorChooser.showDialog abre una ventana aparte con la rueda de colores
		// si el usuario pulsa Cancelar, devuelve null, por eso comprobamos antes de usarlo
		botonColor.addActionListener(e -> {
			Color elegido = JColorChooser.showDialog(null, "Elige un color", colorActual);
			if (elegido != null) {
				colorActual = elegido;
			}
		});

		// ---------- LISTENER DEL COMBO DE FORMAS ----------
		// cada vez que se elige una opcion distinta del desplegable, guardamos cual es
		// getSelectedItem() devuelve un Object, por eso hay que castearlo a String
		comboFormas.addActionListener(e -> {
			formaActual = (String) comboFormas.getSelectedItem();
		});

		// ---------- LISTENER DEL SLIDER DE GROSOR ----------
		// un slider usa ChangeListener, no ActionListener, porque el valor puede cambiar muchas veces seguidas
		// mientras se arrastra, no solo al final
		sliderGrosor.addChangeListener(e -> {
			grosorActual = sliderGrosor.getValue();
		});

		// ---------- LISTENER DEL RATON: AL PULSAR ----------
		// MouseAdapter es una clase que ya implementa todos los metodos de MouseListener vacios
		// asi solo sobreescribimos el que nos interesa (mousePressed), sin tener que implementar los demas
		lienzo.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// guardamos el punto exacto donde se ha pulsado, sera el punto de inicio de la forma nueva
				xInicio = e.getX();
				yInicio = e.getY();

				// al empezar un trazo nuevo, nos aseguramos de que no quede "colgada" una forma anterior
				formaEnCurso = null;
			}
		});

		// ---------- LISTENER DEL RATON: AL ARRASTRAR ----------
		// MouseMotionAdapter funciona igual que MouseAdapter pero para los eventos de movimiento
		// mouseDragged se dispara muchisimas veces por segundo mientras se mueve el raton con el boton pulsado
		lienzo.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {

				if (formaActual.equals("Libre")) {
					// MODO LIBRE: en vez de una forma grande, vamos creando muchas lineas muy cortas seguidas
					// cada tramo va del ultimo punto registrado hasta la posicion actual del raton
					Forma trazo = new Forma(formaActual, xInicio, yInicio, e.getX(), e.getY(), colorActual, grosorActual);
					formas.add(trazo);

					// actualizamos el punto de inicio para que el PROXIMO tramo empiece donde ha terminado este
					xInicio = e.getX();
					yInicio = e.getY();

				} else if (formaEnCurso == null) {
					// MODO LINEA/CIRCULO/CUADRADO, primer movimiento del arrastre:
					// creamos la forma por primera vez, con el punto final igual al punto donde esta el raton ahora
					// y la guardamos tanto en formaEnCurso (para poder seguir modificandola) como en la lista formas
					formaEnCurso = new Forma(formaActual, xInicio, yInicio, e.getX(), e.getY(), colorActual, grosorActual);
					formas.add(formaEnCurso);

				} else {
					// MODO LINEA/CIRCULO/CUADRADO, siguientes movimientos del arrastre:
					// la forma ya existe y ya esta en la lista, asi que solo actualizamos su punto final
					// (el objeto Forma es el mismo que esta guardado en la lista, por eso al modificarlo aqui
					// se modifica tambien el que se va a dibujar)
					formaEnCurso.xFin = e.getX();
					formaEnCurso.yFin = e.getY();
				}

				// repaint() hace que paintComponent se vuelva a ejecutar y se vea el cambio en pantalla
				lienzo.repaint();
			}
		});

		// mostramos la ventana solo al final, cuando ya esta todo creado y añadido
		ventana.setVisible(true);
	}

	// Lienzo es un JPanel al que le hemos sobreescrito paintComponent
	// paintComponent es el metodo que Swing llama automaticamente cada vez que hay que dibujar el panel
	// (al arrancar, al redimensionar la ventana, o cuando nosotros llamamos a repaint())
	@SuppressWarnings("serial")
	static class Lienzo extends JPanel {
		public void paintComponent(Graphics g) {

			// siempre hay que llamar primero al metodo original, que limpia el panel antes de dibujar encima
			super.paintComponent(g);

			// Graphics2D es una version mas completa de Graphics, que permite cosas como setStroke (grosor de linea)
			Graphics2D g2 = (Graphics2D) g;

			// recorremos TODAS las formas guardadas y las volvemos a dibujar una a una, en orden
			for (int i = 0; i < formas.size(); i++) {
				Forma forma = formas.get(i);

				// antes de dibujar cada forma, configuramos su color y su grosor
				g2.setColor(forma.color);
				g2.setStroke(new BasicStroke(forma.grosor));

				if (forma.tipo.equals("Circulo")) {
					// el "diametro" lo calculamos como la distancia horizontal entre el punto de inicio y el final
					// Math.abs() evita que de negativo si arrastras el raton hacia la izquierda
					int diametro = Math.abs(forma.xFin - forma.xInicio);

					// drawOval dibuja un ovalo dentro de un rectangulo imaginario:
					// los dos primeros parametros son la esquina superior izquierda, los dos ultimos el ancho y alto
					// al usar el mismo valor para ancho y alto, el ovalo sale un circulo perfecto
					g2.drawOval(forma.xInicio, forma.yInicio, diametro, diametro);

				} else if (forma.tipo.equals("Cuadrado")) {
					// mismo razonamiento que el circulo, pero con drawRect
					int lado = Math.abs(forma.xFin - forma.xInicio);
					g2.drawRect(forma.xInicio, forma.yInicio, lado, lado);

				} else {
					// este "else" cubre tanto "Libre" como "Linea", porque las dos se dibujan igual:
					// una simple linea recta desde el punto de inicio hasta el punto final
					g2.drawLine(forma.xInicio, forma.yInicio, forma.xFin, forma.yFin);
				}
			}
		}
	}

	// clase que representa UNA forma dibujada, con todos los datos necesarios para poder volver a pintarla
	// cada vez que se hace un trazo (o se arrastra para una linea/circulo/cuadrado), se crea un objeto Forma nuevo
	static class Forma {
		String tipo;              // "Libre", "Linea", "Circulo" o "Cuadrado"
		int xInicio, yInicio;     // punto donde empieza la forma
		int xFin, yFin;           // punto donde termina la forma (se actualiza mientras se arrastra el raton)
		Color color;               // color con el que se dibujo
		int grosor;                 // grosor de la linea con el que se dibujo

		// constructor: recibe todos los datos de golpe y los guarda en los atributos
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