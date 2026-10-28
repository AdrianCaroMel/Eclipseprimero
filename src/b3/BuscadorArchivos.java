package b3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class BuscadorArchivos {
	public static void main (String[]args) {

		// pedimos carpeta y extensión, la extensión en minúsculas para comparar luego
		Scanner sc = new Scanner(System.in);
		System.out.print("Carpeta: ");
		String carpeta = sc.nextLine().trim();
		System.out.print("Extension (Sin el punto): ");
		String extension = sc.nextLine().trim().toLowerCase();
		sc.close();

		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "dir", "/b", carpeta);

		int total = 0;
		int codigo = -1;

		try {
			Process p = pb.start();

			try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))){
				String linea;
				while ((linea = br.readLine()) != null) {
					if (linea.toLowerCase().endsWith("."+extension)) {
						total++;
						System.out.println(total + "." + linea);
					}
				}
			}

			try (BufferedReader brErr = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
				String linea;
				while ((linea = brErr.readLine()) != null) {
					if (!linea.trim().isEmpty()) {
						System.out.println("ERROR: " + linea);
					}
				}
			}

			codigo = p.waitFor();

		} catch (IOException e) {
			System.out.println("ERROR: no se pudo lanzar " + e.getMessage());
		} catch (InterruptedException e) {
			System.out.println("ERROR: espera interrumpida " + e.getMessage());
		}

		if (total == 0 && codigo == 0) {
			System.out.println("No hay archivos ." + extension);
		}
		System.out.println("Total: " + total + " | Código de salida: " + codigo);
	}
}