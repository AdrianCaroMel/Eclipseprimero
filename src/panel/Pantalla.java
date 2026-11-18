package panel;

// tarea de cada pantalla: lee el panel varias veces y comprueba que los datos cuadran
public class Pantalla implements Runnable {

	private Panel panel;
	private ControlPanel control;
	private int lecturas;
	private int msPausa;
	private int incoherentes = 0;

	public Pantalla(Panel panel, ControlPanel control, int lecturas, int msPausa) {
		this.panel = panel;
		this.control = control;
		this.lecturas = lecturas;
		this.msPausa = msPausa;
	}

	@Override
	public void run() {
		String nombre = Thread.currentThread().getName();

		for (int i = 0; i < lecturas; i++) {
			// la lectura va entre empezarLectura() y terminarLectura(), fuera del monitor
			control.empezarLectura();
			int retraso = panel.getRetraso();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println(nombre + ": interrumpido");
			}
			int salida = panel.getSalida();
			control.terminarLectura();

			// comprueba si lo leído es coherente
			if (salida != Panel.PREVISTA + retraso) {
				incoherentes++;
				System.out.println(nombre + " ¡INCOHERENTE!: retraso " + retraso + " min pero salida " + Panel.hora(salida));
			} else {
				System.out.println(nombre + " muestra: retraso " + retraso + " min, salida " + Panel.hora(salida));
			}

			// pausa entre lecturas
			try {
				Thread.sleep(msPausa);
			} catch (InterruptedException e) {
				System.out.println(nombre + ": interrumpido");
			}
		}
	}

	public int getIncoherentes() {
		return incoherentes;
	}
}