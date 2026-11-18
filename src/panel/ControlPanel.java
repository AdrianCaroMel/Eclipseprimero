package panel;

public class ControlPanel {

	private int lectores = 0;
	private boolean escribiendo = false;
	private int maxLectores = 0;

	public synchronized void empezarLectura() {
		String nombre = Thread.currentThread().getName();
		while (escribiendo) {
			System.out.println(nombre + " espera: el operador está escribiendo");
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(nombre + ": interrumpido");
			}
		}
		lectores++;
		if (lectores > maxLectores) {
			maxLectores = lectores;
		}
	}

	// una pantalla deja de leer: si era la última, avisa
	public synchronized void terminarLectura() {
		lectores--;
		if (lectores == 0) {
			// basta notify: si hay pantallas leyendo nadie está escribiendo, así que ninguna pantalla puede estar
			// esperando; el único que puede esperar aquí es el operador, y solo hay uno
			notify();
		}
	}


	public synchronized void empezarEscritura() {
		while (escribiendo || lectores > 0) {
			System.out.println("Operador espera: hay " + lectores + " pantallas leyendo");
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Operador: interrumpido");
			}
		}
		escribiendo = true;
	}

	// el operador termina de escribir y avisa
	public synchronized void terminarEscritura() {
		escribiendo = false;
		// notifyAll y no notify: pueden estar esperando las tres pantallas y todas pueden leer a la vez;
		// con notify solo despertaría una y las demás tendrían que esperar a que esa termine
		notifyAll();
	}

	public synchronized int getMaxLectores() {
		return maxLectores;
	}
}