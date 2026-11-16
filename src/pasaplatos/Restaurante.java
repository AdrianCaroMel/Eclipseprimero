package pasaplatos;

public class Restaurante {
	public static void main(String[] args) {

		// 1. El pasaplatos compartido y los platos de hoy
		Pasaplatos pasaplatos = new Pasaplatos();
		String[] platos = {"Paella", "Gazpacho", "Tortilla","Croquetas", "Tarta"};

		// 2. Los dos hilos reciben el mismo pasaplatos; el camarero recoge tantos platos como hay
		Thread cocinero = new Thread(new Cocinero(platos, pasaplatos), "Cocinero");
		Thread camarero = new Thread(new Camarero(pasaplatos, platos.length), "Camarero");

		// 3. Se lanzan los dos
		cocinero.start();
		camarero.start();

		// 4. Se espera a que terminen los dos, el main sin throws
		try {
			cocinero.join();
			camarero.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Cierra el restaurante");
	}
}