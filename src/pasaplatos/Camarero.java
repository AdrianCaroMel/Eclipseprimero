package pasaplatos;

// tarea del camarero: recoge platos del pasaplatos y los sirve
public class Camarero implements Runnable {

	private Pasaplatos pase;
	private int cantidad;

	public Camarero(Pasaplatos pase, int cantidad) {
		this.pase = pase;
		this.cantidad = cantidad;
	}

	@Override
	public void run() {
		String nombre = Thread.currentThread().getName();

		// recoge tantos platos como le hayan dicho
		for (int i = 0; i < cantidad; i++) {
			// si el pasaplatos está vacío se queda esperando dentro de recoger()
			String plato = pase.recoger();

			// tarda un poco en llevarlo a la mesa, run() no puede llevar throws
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				System.out.println(nombre + ": interrumpido");
			}
			System.out.println(nombre + ": sirve " + plato + " (" + (i + 1) + "/" + cantidad + ")");
		}
		System.out.println(nombre + ": todos los platos servidos");
	}
}