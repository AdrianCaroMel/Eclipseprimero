package di;

import java.awt.BorderLayout;

import javax.swing.*;

public class Ejercicio5 {

	public static void main(String[] args) {
		JFrame ventana = new JFrame("Editor de Texto");
		ventana.setSize(400, 400);
		ventana.setLocation(100, 200);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setLayout(new BorderLayout());

		JMenuBar barraMenu = new JMenuBar();
		JMenu menuArchivo = new JMenu("Archivo");
		JMenuItem submenuNuevo = new JMenuItem("Nuevo");

		menuArchivo.add(submenuNuevo);
		barraMenu.add(menuArchivo);
		ventana.setJMenuBar(barraMenu);

		JTextArea areaTexto = new JTextArea();
		JScrollPane scroll = new JScrollPane(areaTexto);
	
		submenuNuevo.addActionListener(e -> areaTexto.setText(""));//accion de borrado o reset de el textfield

		ventana.add(scroll, BorderLayout.CENTER);

		ventana.setVisible(true);
	}

}
