package di;
import java.io.*;
import java.util.Scanner;
public class EjercicioSecuencialIA {
	private static final int MIN = 0;
	private static final int MAX = 100;

	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		Scanner teclado = new Scanner(System.in);
		boolean seguir = true; 

		while (seguir) {
			int secreto = random(); // numero que hay que adivinar
			int intentos = 0;
			boolean acertado = false; // controla si ya se ha adivinado el numero

			System.out.println("Adivina el numero secreto (0-100):");

			while (!acertado) {
				System.out.print("Introduce un numero: ");
				int numero = teclado.nextInt();
				intentos++; // contamos este intento
				if (numero == secreto) {
					acertado = true; // se ha adivinado, se sale del bucle
				} else if (numero < secreto) {
					System.out.println("Demasiado bajo.");
				} else {
					System.out.println("Demasiado alto.");
				}
			}
			System.out.println("Has acertado en " + intentos + " intentos.");
			comprobarRecord(intentos);

			// preguntamos si se quiere seguir jugando
			System.out.print("Quieres seguir jugando? (s/n): ");
			String respuesta = teclado.next();

			if (respuesta.equals("s")) {
				seguir = true;
			} else {
				seguir = false;
			}
		}
	}
	// Lee el record guardado en el fichero y lo actualiza si se ha mejorado
	private static void comprobarRecord(int intentos) throws IOException {
		File fichero = new File("src/record.txt");
		boolean hayRecordGuardado = fichero.exists() && fichero.length() > 0;
		int record = 0;

		if (hayRecordGuardado) {
			// el fichero ya tiene un record guardado, lo leemos
			FileReader fr = new FileReader(fichero);
			BufferedReader br = new BufferedReader(fr);
			String linea = br.readLine(); // leemos la unica linea del fichero
			br.close(); // cerramos el fichero, ya no lo necesitamos
			record = Integer.parseInt(linea); // convertimos el texto a numero
		}
		// si no habia record guardado, cualquier resultado es un record
		// si lo habia, comprobamos si el nuevo resultado es mejor (menor)
		if (!hayRecordGuardado || intentos < record) {
			System.out.println("Nuevo record!");
			FileWriter fw = new FileWriter(fichero); // abre el fichero para escribir (lo vacia si ya tenia algo)
			fw.write("" + intentos); // escribimos el numero como texto
			fw.close(); // cerramos y guardamos los cambios
		}
	}
	public static int random() {
		int numero;
		numero = (int) (Math.random()*(MAX-MIN)+1-MIN);
		return numero;
	}
}