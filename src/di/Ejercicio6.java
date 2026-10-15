package di;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.*;

public class Ejercicio6 {
	public static void main (String[]args){
		JFrame ventana = new JFrame("Modificador de texto");
		ventana.setSize(400, 400);
		ventana.setLocation(100, 200);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setLayout(new BorderLayout());
		JTextArea areaTexto = new JTextArea("");
		JScrollPane scroll = new JScrollPane(areaTexto);
		ventana.add(scroll, BorderLayout.CENTER);
		
		JSlider slider = new JSlider(8, 72, 12);
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(12, 8, 72, 1));
		
		slider.addChangeListener(e -> {
			int tamano = slider.getValue();
			spinner.setValue(tamano);
			areaTexto.setFont(new Font("Arial", Font.PLAIN, tamano));
		});
		
		spinner.addChangeListener(e -> {
			int tamano = (int) spinner.getValue();
			slider.setValue(tamano);
			areaTexto.setFont(new Font("Arial", Font.PLAIN, tamano));
		});
		
		JPanel panelControles = new JPanel(new FlowLayout());
		panelControles.add(slider);
		panelControles.add(spinner);
		ventana.add(panelControles, BorderLayout.SOUTH);
		
		ventana.setVisible(true);
	}
}