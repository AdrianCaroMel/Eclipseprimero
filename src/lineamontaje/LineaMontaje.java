package lineamontaje;

public class LineaMontaje {
	private int turno = 0;
	private int fases;
	private int bicicletas = 0;
	public LineaMontaje (int fases) {
		this.fases = fases;
	}
	
	public synchronized void esperarTurno(int miFase) {
		String nombre = Thread.currentThread().getName();
		while (turno != miFase) {
			System.out.println(nombre + " espera: es el turno de la fase " + turno);
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(nombre + ": interrumpido esperando turno");
			}
		}
	}
	public synchronized void pasarTurno() {
		turno = (turno+1)%fases;

		if (turno == 0) {
			bicicletas++;
			System.out.println("Bicicleta " + bicicletas + " terminada");
		}

		// notifyAll y no notify: hay varios operarios esperando y notify despertaría solo a uno al azar;
		notifyAll();
	}
	public synchronized int getBicicletas() {
		return bicicletas;
	}
}
