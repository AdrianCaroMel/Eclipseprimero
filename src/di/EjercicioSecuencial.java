package di;
import java.io.*;
import java.util.Scanner;
public class EjercicioSecuencial {
	private static final int MIN = 0;
	private static final int MAX = 100;
	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		Scanner teclado = new Scanner(System.in);
		boolean seguir = true;
		// Bucle principal: cada vuelta es una partida completa
		while (seguir) {
			int secreto = random();
			int intentos = 0;
			boolean acertado = false;
			System.out.println("Adivina el numero secreto (0-100):");
			// Bucle de intentos: se repite hasta acertar el numero
			while (!acertado) {
				System.out.print("Introduce un numero: ");
				int numero = teclado.nextInt();
				intentos++;

				if (numero == secreto) {
					acertado = true;
				} else if (numero < secreto) {
					System.out.println("Demasiado bajo.");
				} else {
					System.out.println("Demasiado alto.");
				}
			}
			System.out.println("Has acertado en " + intentos + " intentos.");
			comprobarRecord(intentos);
			// Se pregunta si se quiere jugar otra partida
			System.out.print("Quieres seguir jugando? (s/n): ");
			String respuesta = teclado.next();

			if (respuesta.equals("s")) {
				seguir = true;
			} else {
				seguir = false;
			}
		}
	}
	/**
	 * Comprueba si el numero de intentos de la partida actual
	 * mejora el record guardado en record.txt, y si es asi,
	 * actualiza el fichero con el nuevo record.
	 */
	private static void comprobarRecord(int intentos) throws IOException {
		File fichero = new File("src/record.txt");
		boolean hayRecordGuardado = fichero.exists() && fichero.length() > 0;
		int record = 0;
		// Lectura del record actual (si existe)
		if (hayRecordGuardado) {
			FileReader fr = new FileReader(fichero);
			BufferedReader br = new BufferedReader(fr);
			String linea = br.readLine();
			br.close();
			record = Integer.parseInt(linea);
		}
		// Si no habia record previo, o el actual es mejor (menos intentos),
		// se escribe el nuevo record en el fichero
		if (!hayRecordGuardado || intentos < record) {
			System.out.println("Nuevo record!");
			FileWriter fw = new FileWriter(fichero);
			fw.write("" + intentos);
			fw.close();
		}
	}
	/**
	 * Genera un numero aleatorio entre MIN y MAX.
	 */
	public static int random() {
		int numero;
		numero = (int) (Math.random()*(MAX-MIN)+1-MIN);
		return numero;
	}
}