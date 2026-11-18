package panel;

public class Aeropuerto {

	static final int LECTURAS = 6;
	static final int MS_PAUSA = 150;
	static final int ACTUALIZACIONES = 4;

	public static void main(String[] args) {

		// 1. Los datos y el monitor, compartidos por todos
		Panel panel = new Panel();
		ControlPanel control = new ControlPanel();

		// 2. Las tres pantallas en arrays (las tareas hacen falta al final para sumar incoherencias)
		Pantalla[] pantallas = new Pantalla[3];
		Thread[] hilos = new Thread[3];
		for (int i = 0; i < pantallas.length; i++) {
			pantallas[i] = new Pantalla(panel, control, LECTURAS, MS_PAUSA);
			hilos[i] = new Thread(pantallas[i], "Pantalla-" + (i + 1));
		}

		// 3. El operador
		Thread operador = new Thread(new Operador(panel, control, ACTUALIZACIONES), "Operador");

		// 4. Se lanzan los cuatro
		long inicio = System.currentTimeMillis();
		for (int i = 0; i < hilos.length; i++) {
			hilos[i].start();
		}
		operador.start();

		// 5. Se espera a los cuatro, el main sin throws
		try {
			for (int i = 0; i < hilos.length; i++) {
				hilos[i].join();
			}
			operador.join();
		} catch (InterruptedException e) {
			System.out.println("Han interrumpido al main");
		}

		// 6. Resumen
		long tiempo = System.currentTimeMillis() - inicio;
		int incoherentes = 0;
		for (int i = 0; i < pantallas.length; i++) {
			incoherentes = incoherentes + pantallas[i].getIncoherentes();
		}
		System.out.println("=== Fin de la prueba del panel ===");
		System.out.println("Lecturas incoherentes: " + incoherentes);
		System.out.println("Máximo de pantallas leyendo a la vez: " + control.getMaxLectores());
		System.out.println("Panel final: retraso " + panel.getRetraso() + " min, salida " + Panel.hora(panel.getSalida()));
		System.out.println("Tiempo: " + tiempo + " ms");
	}
}