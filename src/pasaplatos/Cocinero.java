package pasaplatos;

// tarea del cocinero: prepara los platos uno a uno y los deja en el pasaplatos
public class Cocinero implements Runnable {

	private String[] platos;
	private Pasaplatos pase;

	public Cocinero(String[] platos, Pasaplatos pase) {
		this.platos = platos;
		this.pase = pase;
	}

	@Override
	public void run() {
		String nombre = Thread.currentThread().getName();

		// prepara los platos en orden
		for (int i = 0; i < platos.length; i++) {
			System.out.println(nombre + ": preparando " + platos[i]);

			// tarda en cocinarlo, run() no puede llevar throws
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println(nombre + ": interrumpido");
			}

			// lo deja en el pasaplatos, si está ocupado espera dentro de dejar()
			pase.dejar(platos[i]);
		}
		System.out.println(nombre + ": todos los platos preparados");
	}
}