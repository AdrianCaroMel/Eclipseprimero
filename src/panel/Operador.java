package panel;

// tarea del operador: cada aviso suma 15 min al retraso y luego cambia la salida
public class Operador implements Runnable {

	private Panel panel;
	private ControlPanel control;
	private int actualizaciones;

	public Operador(Panel panel, ControlPanel control, int actualizaciones) {
		this.panel = panel;
		this.control = control;
		this.actualizaciones = actualizaciones;
	}

	@Override
	public void run() {
		for (int i = 0; i < actualizaciones; i++) {
			// espera a que llegue el aviso
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				System.out.println("Operador: interrumpido");
			}

			// la escritura va entre empezarEscritura() y terminarEscritura(), fuera del monitor
			control.empezarEscritura();
			panel.setRetraso(panel.getRetraso() + 15);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.out.println("Operador: interrumpido");
			}
			panel.setSalida(Panel.PREVISTA + panel.getRetraso());
			control.terminarEscritura();

			System.out.println("Operador actualiza: retraso " + panel.getRetraso() + " min, salida " + Panel.hora(panel.getSalida()));
		}
	}
}