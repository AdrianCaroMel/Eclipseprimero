package di;

import javax.swing.*;

// Clase con el main: lanza login, pantalla de carga y ventana principal
public class Practica5 {

	public static void main(String[] args) {

		// 1. Look & Feel inicial (antes de crear ninguna ventana)
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 2. Login: al ser modal, el programa espera aquí hasta que sea correcto
		LoginDialog login = new LoginDialog();
		login.setVisible(true);

		// 3. Pantalla de carga: la barra sube de 0 a 100
		SplashScreen splash = new SplashScreen();
		splash.setVisible(true);

		for (int i = 0; i <= 100; i++) {
			splash.incrementar(i);
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		splash.dispose();

		// 4. Ventana principal
		MainApp ventana = new MainApp();
		ventana.setVisible(true);
	}
}