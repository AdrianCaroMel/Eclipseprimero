package b1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LanzadorHerramientas {

	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("Lanzador Herramientas");
		String opcion;

		// el menú se repite hasta que se elige salir
		do {
			System.out.println("1. Bloc de notas");
			System.out.println("2. Calculadora");
			System.out.println("3. Listar C:\\Windows");
			System.out.println("4. Programa inexistente");
			System.out.println("5. Salir");
			System.out.print("Opcion: ");
			// se lee como texto, así si se escribe una letra no peta como pasaría con nextInt()
			opcion = sc.nextLine().trim();

			// el ProcessBuilder se crea vacío y el comando se le da con command() según la opción, aún no se lanza nada
			ProcessBuilder pb = new ProcessBuilder();
			boolean lanzar = true;

			switch (opcion) {
			case "1":
				pb.command("notepad.exe");
				break;
			case "2":
				pb.command("calc.exe");
				break;
			case "3":
				// cada argumento va en un String distinto, dir es un comando interno de cmd y por eso hace falta cmd.exe /c
				pb.command("cmd.exe", "/c", "dir");
				// directory() hace que el proceso se ejecute dentro de esa carpeta
				pb.directory(new File("C:\\Windows"));
				break;
			case "4":
				// este programa no existe, es para provocar el error a propósito
				pb.command("programaquenoexiste.exe");
				break;
			case "5":
				System.out.println("\nSaliendo\n");
				lanzar = false;
				break;
			default:
				System.out.println("\nOpcion invalida, vuelve a intentarlo\n");
				lanzar = false;
				break;
			}

			// el lanzamiento va una sola vez aquí abajo en vez de repetir el try/catch en cada case
			if (lanzar) {

				// inheritIO() hace que la salida del proceso salga en la consola de Eclipse
				pb.inheritIO();

				try {
					// el proceso empieza a existir con start(), antes solo estaba preparado
					Process p = pb.start();
					System.out.println("Lanzado: " + pb.command());

					// waitFor() para esperar a que termine y que la salida no se mezcle con el menú
					p.waitFor();

				} catch (IOException e) {
					// salta si no se puede lanzar, por ejemplo con el programa que no existe, así el programa no se rompe
					System.out.println("No se pudo lanzar " + pb.command() + ": " + e.getMessage());
				} catch (InterruptedException e) {
					// waitFor() lanza esta excepción aparte, por eso va en su propio catch
					System.out.println("Espera interrumpida: " + e.getMessage());
				}
				System.out.println();
			}

		} while (!opcion.equals("5"));
	}
}