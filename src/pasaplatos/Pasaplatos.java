package pasaplatos;

// el monitor: el hueco del pasaplatos donde cabe un solo plato
public class Pasaplatos {

	private String plato;
	private boolean lleno = false;

	// lo llama el cocinero: si el hueco está ocupado espera a que el camarero lo vacíe
	public synchronized void dejar(String plato) {
		String nombre = Thread.currentThread().getName();
		// while y no if: al despertar vuelve a comprobar que el hueco sigue libre
		while (lleno) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(nombre + ": interrumpido esperando hueco");
			}
		}
		this.plato = plato;
		lleno = true;
		System.out.println(nombre + " deja " + plato + " en el pasaplatos");
		// notifyAll y no notify: con notify podría despertar a otro cocinero en vez de al camarero
		notifyAll();
	}

	// lo llama el camarero: si no hay plato espera a que un cocinero deje uno
	public synchronized String recoger() {
		String nombre = Thread.currentThread().getName();
		while (!lleno) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(nombre + ": interrumpido esperando plato");
			}
		}
		lleno = false;
		System.out.println(nombre + " recoge " + plato + " del pasaplatos");
		// avisa a los cocineros de que el hueco ya está libre
		notifyAll();
		return plato;
	}
}