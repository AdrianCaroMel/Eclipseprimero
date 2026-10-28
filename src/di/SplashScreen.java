package di;

import java.awt.*;
import javax.swing.*;

// Pantalla de carga: JDialog con título, texto de estado y barra de progreso
public class SplashScreen extends JDialog {

	private static final long serialVersionUID = 1L;

	private JProgressBar barra;

	public SplashScreen() {

		setUndecorated(true);
		setSize(400, 200);
		setLayout(new BorderLayout(10, 10));
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

		// Label 1: título de la aplicación
		JLabel titulo = new JLabel("Gestión de notas interactivas", SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 20));
		add(titulo, BorderLayout.CENTER);

		// Label 2: lo que está haciendo + barra de progreso
		JLabel lblEstado = new JLabel("Cargando recursos...", SwingConstants.CENTER);
		barra = new JProgressBar(0, 100);
		barra.setStringPainted(true);

		JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
		panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
		panelInferior.add(lblEstado, BorderLayout.NORTH);
		panelInferior.add(barra, BorderLayout.SOUTH);
		add(panelInferior, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
	}

	// Método que incrementa el valor de la barra de progreso
	public void incrementar(int valor) {
		barra.setValue(valor);
	}
}