package lineamontaje;

// tarea de cada operario: espera su turno, hace su trabajo y pasa el turno
public class Operario implements Runnable {

	private LineaMontaje linea;
	private int miFase;
	private String trabajo;
	private int cantidad;
	private int ms;

	public Operario(LineaMontaje linea, int miFase, String trabajo, int cantidad, int ms) {
		this.linea = linea;
		this.miFase = miFase;
		this.trabajo = trabajo;
		this.cantidad = cantidad;
		this.ms = ms;
	}

	@Override
	public void run() {
		String nombre = Thread.currentThread().getName();

		// una vuelta por bicicleta
		for (int i = 1; i <= cantidad; i++) {
			linea.esperarTurno(miFase);

			System.out.println(nombre + " " + trabajo + " la bicicleta " + i);

			// el trabajo va aquí, fuera del monitor, para no bloquear a los demás mientras duerme
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				System.out.println(nombre + ": interrumpido");
			}

			linea.pasarTurno();
		}
		System.out.println(nombre + " termina su jornada");
	}
}