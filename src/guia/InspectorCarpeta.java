package guia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class InspectorCarpeta {

    public static void main(String[] args) throws IOException, InterruptedException {

        // 1. Pedimos al usuario la carpeta y la extensión (sin punto)
        Scanner sc = new Scanner(System.in);
        System.out.print("Carpeta: ");
        String carpeta = sc.nextLine();
        System.out.print("Extensión (sin punto): ");
        String extension = sc.nextLine();

        // 2. Validaciones: si falla alguna, terminamos sin lanzar ningún proceso
        File dir = new File(carpeta);
        if (!dir.exists()) {
            System.out.println("La carpeta no existe");
            return;
        }
        if (extension.length() == 0) {
            System.out.println("La extensión es obligatoria");
            return;
        }

        // 3. Preparamos el proceso: cmd.exe /c dir *.<extensión>
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "dir", "*." + extension);

        // Lo ejecutamos dentro de la carpeta indicada
        pb.directory(dir);

        // 4. Variable de entorno DIRCMD=/b para que dir muestre solo los nombres
        pb.environment().put("DIRCMD", "/b");

        // 5. Los errores del proceso se guardan en errores_inspector.txt
        File errores = new File("errores_inspector.txt");
        pb.redirectError(errores);

        // 6. Lanzamos el proceso (aquí empieza a ejecutarse de verdad)
        Process proceso = pb.start();

        // Leemos la salida del proceso línea a línea y la numeramos
        BufferedReader lector = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
        String linea;
        int contador = 0;
        while ((linea = lector.readLine()) != null) {
            contador++;
            System.out.println(contador + ". " + linea);
        }
        lector.close();

        // 7. Esperamos a que termine y recogemos el código de salida
        int codigo = proceso.waitFor();

        if (codigo == 0) {
            // Todo bien: mostramos el total
            System.out.println("Total: " + contador + " archivos ." + extension);
        } else {
            // Hubo error: mostramos el contenido del archivo de errores
            BufferedReader lectorErrores = new BufferedReader(new FileReader(errores));
            String lineaError;
            while ((lineaError = lectorErrores.readLine()) != null) {
                System.out.println(lineaError);
            }
            lectorErrores.close();
            System.out.println("No hay archivos ." + extension + " en " + carpeta + " (código " + codigo + ")");
        }
    }
}