package lineamontaje;

public class Taller {

	static final int BICICLETAS = 5;
	static final int FASES = 3;

	public static void main(String[] args) {

		// 1. La línea y los tres operarios, todos con la misma línea
		LineaMontaje linea = new LineaMontaje(FASES);
		Thread soldador = new Thread(new Operario(linea, 0, "suelda el cuadro de", BICICLETAS, 300), "Soldador");
		Thread montador = new Thread(new Operario(linea, 1, "monta las ruedas de", BICICLETAS, 200), "Montador");
		Thread pintor = new Thread(new Operario(linea, 2, "pinta", BICICLETAS, 400), "Pintor");

		// 2. Se lanzan en orden inverso para ver que el monitor impone el orden
		long inicio = System.currentTimeMillis();
		pintor.start();
		montador.start();
		soldador.start();

		// 3. Se espera a los tres, el main sin throws
		try {
			pintor.join();
			montador.join();
			soldador.join();
		} catch (InterruptedException e) {
			System.out.println("Han interrumpido al main");
		}

		// 4. Resumen de la jornada
		long tiempo = System.currentTimeMillis() - inicio;
		System.out.println("=== Fin de la jornada ===");
		System.out.println("Bicicletas terminadas: " + linea.getBicicletas());
		System.out.println("Tiempo: " + tiempo + " ms");
	}
}