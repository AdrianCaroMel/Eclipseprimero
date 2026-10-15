package di;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class Bloque1 {
	public static void main (String[]args) {
		JFrame ventana = new JFrame("Bloque1");
		ventana.setSize(800,400);
		ventana.setLocation(400,200);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setLayout(new BorderLayout());
		JMenuBar barraMenu = new JMenuBar();
		JMenu archivo = new JMenu("Archivo");
		JMenu herramientasMenu = new JMenu("Herramientas");
		JMenuItem salir = new JMenuItem("Salir");		
		JMenuItem nuevo = new JMenuItem("Nuevo");		
		JMenuItem pincelMenu = new JMenuItem("Pincel");		
		JMenuItem pipetaMenu = new JMenuItem("Pipeta");		
		JMenuItem cuboMenu = new JMenuItem("Cubo Rellenar");		
		JMenuItem borradorMenu = new JMenuItem("Borrador");
		barraMenu.add(archivo);
		barraMenu.add(herramientasMenu);
		archivo.add(salir);
		archivo.add(nuevo);
		herramientasMenu.add(pincelMenu);
		herramientasMenu.add(pipetaMenu);
		herramientasMenu.add(cuboMenu);
		herramientasMenu.add(borradorMenu);
		ventana.setJMenuBar(barraMenu);
		JToolBar herramientas = new JToolBar();
		ventana.add(herramientas ,BorderLayout.NORTH);
		JButton pincel = new JButton("Pincel");
		JButton linea = new JButton("Linea");
		JButton circulo = new JButton("Circulo");
		JButton cuadrado = new JButton("Cuadrado"); 
		JButton borrador =new JButton("Borrador");
		herramientas.add(pincel);
		herramientas.add(linea);
		herramientas.add(circulo);
		herramientas.add(cuadrado);
		herramientas.add(borrador);
		herramientas.addSeparator();
		JLabel txtgrosor = new JLabel("Grosor:");
		JSlider grosor = new JSlider(1,40,2);
		grosor.setPreferredSize(new Dimension(120,40));
		herramientas.add(txtgrosor);
		herramientas.add(grosor);
		herramientas.addSeparator();
		JButton color = new JButton("Color");
		herramientas.add(color);
		Lienzo lienzo = new Lienzo();
		ventana.add(lienzo, BorderLayout.CENTER);
		color.addActionListener(new ActionListener(){

		    @Override
		    public void actionPerformed(ActionEvent e) {

		        Color nuevoColor = JColorChooser.showDialog(
		                ventana,
		                "Selecciona un color",
		                lienzo.colorActual
		        );

		        if (nuevoColor != null) {
		            lienzo.colorActual = nuevoColor;
		        }
		    }
		});
		
		lienzo.addMouseListener(new MouseAdapter() {

		    @Override
		    public void mousePressed(MouseEvent e) {
		    lienzo.xInicio= e.getX();
		    lienzo.yInicio =e.getY();
		    lienzo.xAnterior = e.getX();
		    lienzo.yAnterior =e.getY();
		    }
		    @Override
		    public void mouseReleased(MouseEvent e) {
			    lienzo.xFin= e.getX();
			    lienzo.yFin =e.getY();
			     
			    if (lienzo.herramienta.equals("linea") || lienzo.herramienta.equals("circulo") || lienzo.herramienta.equals("cuadrado")) {
			    	
			        Forma forma = new Forma();

			        forma.xInicio = lienzo.xInicio;
			        forma.yInicio = lienzo.yInicio;
			        forma.xFin = lienzo.xFin;
			        forma.yFin = lienzo.yFin;

			        forma.tipo = lienzo.herramienta;
			        forma.color =lienzo.colorActual;
			        forma.grosor = lienzo.grosorActual;
			        
			        lienzo.formas.add(forma);

			        lienzo.repaint();
			    }
			    
		    }
		});	
		lienzo.addMouseMotionListener(new MouseMotionAdapter() {

		    @Override
		    public void mouseDragged(MouseEvent e) {

		        if (lienzo.herramienta.equals("libre") || lienzo.herramienta.equals("borrador")) {

		            Forma forma = new Forma();
		            forma.grosor = lienzo.grosorActual;
		            forma.xInicio = lienzo.xAnterior;
		            forma.yInicio = lienzo.yAnterior;
		            if (lienzo.herramienta.equals("libre")) {
		                forma.color = lienzo.colorActual;
		            }
		            if (lienzo.herramienta.equals("borrador")) {
		                forma.color = lienzo.getBackground();
		            }
		            forma.xFin = e.getX();
		            forma.yFin = e.getY();

		            forma.tipo = lienzo.herramienta;

		            lienzo.formas.add(forma);
		            lienzo.xAnterior = e.getX();
		            lienzo.yAnterior = e.getY();

		            lienzo.repaint();
		        }
		    }
		});
		pincel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				lienzo.herramienta = "libre";
			}
			
		});
		linea.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				lienzo.herramienta = "linea";
			}
			
		});
		nuevo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				lienzo.formas.clear();
				lienzo.repaint();
			}
			
		});
		salir.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
		circulo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				lienzo.herramienta = "circulo";
			}
			
		});
		cuadrado.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				lienzo.herramienta = "cuadrado";
			}
			
		});
		borrador.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				lienzo.herramienta = "borrador";
			}
			
		});
		pincelMenu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				lienzo.herramienta = "libre";
			}
			
		});
		pipetaMenu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				lienzo.herramienta = "pipeta";
			}
			
		});
		cuboMenu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				lienzo.herramienta = "cubo";
			}
			
		});
		borradorMenu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				lienzo.herramienta = "borrador";
			}
			
		});
		grosor.addChangeListener(new ChangeListener() {

		    @Override
		    public void stateChanged(ChangeEvent e) {
		        lienzo.grosorActual = grosor.getValue();
		    }
		});
		ventana.setVisible(true);
	}
    static class Lienzo extends JPanel {

        int xInicio;
        int yInicio;
        int xFin;
        int yFin;
        int xAnterior;
        int yAnterior;

        Color colorActual = Color.BLACK;
        String herramienta = "libre";
        int grosorActual = 2;

        ArrayList<Forma> formas = new ArrayList<>();

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            
            for (Forma forma : formas) {
            	g2.setColor(forma.color);
            	g2.setStroke(new BasicStroke(forma.grosor));
            	if (forma.tipo.equals("linea")) {
                g2.drawLine(
                    forma.xInicio,
                    forma.yInicio,
                    forma.xFin,
                    forma.yFin
                );}
            	else if (forma.tipo.equals("circulo")) {
            		int x = Math.min(forma.xInicio, forma.xFin);
            		int y = Math.min(forma.yInicio, forma.yFin);
            		
                    int ancho = Math.abs(forma.xFin - forma.xInicio);
                    int alto = Math.abs(forma.yFin - forma.yInicio);
                    g2.drawOval(x, y, ancho, alto);
            	}
            	else if (forma.tipo.equals("cuadrado")) {
            		int x = Math.min(forma.xInicio, forma.xFin);
            		int y = Math.min(forma.yInicio, forma.yFin);
            		
                    int ancho = Math.abs(forma.xFin - forma.xInicio);
                    int alto = Math.abs(forma.yFin - forma.yInicio);
                    g2.drawRect(x, y, ancho, alto);
            	}
            	else if (forma.tipo.equals("libre")) {

            	    g2.drawLine(
            	        forma.xInicio,
            	        forma.yInicio,
            	        forma.xFin,
            	        forma.yFin
            	    );
            	}
            	else if (forma.tipo.equals("borrador")) {

            	    g2.drawLine(
            	        forma.xInicio,
            	        forma.yInicio,
            	        forma.xFin,
            	        forma.yFin
            	    );
            	}
            }
        }
    }
    static class Forma {

        int xInicio;
        int yInicio;
        int xFin;
        int yFin;
        
        String tipo;
        Color color;
        int grosor;
    }
}