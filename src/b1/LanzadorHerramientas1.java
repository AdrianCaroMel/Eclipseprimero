package b1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LanzadorHerramientas1 {

    public static void main(String[] args) {

        // menú y lectura de la opción, se lee como texto para poder comparar con equals
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Bloc de notas");
        System.out.println("2. Calculadora");
        System.out.println("3. Listar C:\\Windows");
        System.out.println("4. Programa inexistente");
        System.out.print("Opción: ");
        String opcion = sc.nextLine();

        // el ProcessBuilder se crea vacío y el comando se le da con command() según la opción, aún no se lanza nada
        ProcessBuilder pb = new ProcessBuilder();
        boolean valida = true;

        if (opcion.equals("1")) {
            pb.command("notepad.exe");
        } else if (opcion.equals("2")) {
            pb.command("calc.exe");
        } else if (opcion.equals("3")) {
            // cada argumento va en un String distinto, dir es un comando interno de cmd y por eso hace falta cmd.exe /c
            pb.command("cmd.exe", "/c", "dir");
            // directory() hace que el proceso se ejecute dentro de esa carpeta
            pb.directory(new File("C:\\Windows"));
        } else if (opcion.equals("4")) {
            // este programa no existe, es para provocar el error a propósito
            pb.command("programaquenoexiste.exe");
        } else {
            // si la opción no es ninguna de las anteriores no se lanza nada
            valida = false;
            System.out.println("Opción no válida");
        }

        if (valida) {

            // inheritIO() hace que la salida del proceso salga en la consola de Eclipse
            pb.inheritIO();

            try {
                // el proceso empieza a existir con start(), antes solo estaba preparado
                Process p = pb.start();
                System.out.println("Lanzado: " + pb.command());

                // waitFor() para esperar a que termine y que la salida no se mezcle
                p.waitFor();

            } catch (IOException e) {
                // salta si no se puede lanzar, por ejemplo con el programa que no existe, así el programa no se rompe
                System.out.println("No se pudo lanzar " + pb.command() + ": " + e.getMessage());
            } catch (InterruptedException e) {
                // waitFor() lanza esta excepción aparte, por eso va en su propio catch
                System.out.println("Espera interrumpida: " + e.getMessage());
            }
        }
    }
}