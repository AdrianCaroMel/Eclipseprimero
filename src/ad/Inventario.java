package ad;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

/*
 * INVENTARIO CON RandomAccessFile (acceso aleatorio)
 *
 * Cada producto es un REGISTRO de TAMAÑO FIJO, así se puede saltar
 * directamente a cualquiera con seek(posicion).
 *
 *   id       int            4 bytes
 *   nombre   20 caracteres  40 bytes (cada char ocupa 2 bytes con writeChars)
 *   cantidad int            4 bytes
 *   precio   double         8 bytes
 *   ------------------------------
 *   TOTAL                  56 bytes por registro
 *
 * El id coincide con la posición: el id 1 está en el byte 0, el id 2 en el 56...
 *   posicion = (id - 1) * TAM
 * Borrado lógico: se pone el id a 0 y el registro se salta al listar.
 */
public class Inventario {

    static final int TAM_NOMBRE = 20;                   // caracteres del nombre
    static final int TAM = 4 + TAM_NOMBRE * 2 + 4 + 8;  // 56 bytes

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        File fichero = new File("inventario.dat");
        int opcion = -1;

        // try-with-resources: el fichero se cierra solo al salir
        // "rw" = lectura y escritura (si no existe lo crea)
        try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {

            while (opcion != 0) {
                System.out.println("\n--- INVENTARIO ---");
                System.out.println("1. Alta de producto");
                System.out.println("2. Listar todos");
                System.out.println("3. Buscar por id");
                System.out.println("4. Modificar cantidad");
                System.out.println("5. Borrar (lógico)");
                System.out.println("0. Salir");
                System.out.print("Opción: ");
                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {

                    case 1: // ===== ALTA: se escribe siempre al FINAL =====
                        // El id nuevo sale del número de registros que ya hay
                        int id = (int) (raf.length() / TAM) + 1;
                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine();
                        System.out.print("Cantidad: ");
                        int cantidad = Integer.parseInt(sc.nextLine());
                        System.out.print("Precio: ");
                        double precio = Double.parseDouble(sc.nextLine());

                        raf.seek(raf.length());          // ir al final
                        raf.writeInt(id);
                        // Nombre a tamaño fijo: se corta o se rellena hasta 20
                        StringBuffer sb = new StringBuffer(nombre);
                        sb.setLength(TAM_NOMBRE);
                        raf.writeChars(sb.toString());
                        raf.writeInt(cantidad);
                        raf.writeDouble(precio);
                        System.out.println("Guardado con id " + id);
                        break;

                    case 2: // ===== LISTAR: recorrer desde el principio =====
                        raf.seek(0);
                        // Mientras el puntero no llegue al final del fichero
                        while (raf.getFilePointer() < raf.length()) {
                            int idLeido = raf.readInt();
                            // Leer el nombre carácter a carácter
                            char[] letras = new char[TAM_NOMBRE];
                            for (int i = 0; i < TAM_NOMBRE; i++) {
                                letras[i] = raf.readChar();
                            }
                            String nombreLeido = new String(letras).trim();
                            int cantidadLeida = raf.readInt();
                            double precioLeido = raf.readDouble();

                            if (idLeido != 0) {          // 0 = borrado, no se muestra
                                System.out.println(idLeido + " | " + nombreLeido + " | "
                                        + cantidadLeida + " uds | " + precioLeido + " €");
                            }
                        }
                        break;

                    case 3: // ===== BUSCAR: saltar directo con seek =====
                        System.out.print("Id a buscar: ");
                        int idBuscar = Integer.parseInt(sc.nextLine());
                        long pos = (long) (idBuscar - 1) * TAM;

                        // Comprobar que la posición está dentro del fichero
                        if (idBuscar < 1 || pos >= raf.length()) {
                            System.out.println("No existe ese id");
                            break;
                        }
                        raf.seek(pos);
                        int idEncontrado = raf.readInt();
                        if (idEncontrado == 0) {
                            System.out.println("Ese producto está borrado");
                            break;
                        }
                        char[] letrasB = new char[TAM_NOMBRE];
                        for (int i = 0; i < TAM_NOMBRE; i++) {
                            letrasB[i] = raf.readChar();
                        }
                        System.out.println("Nombre: " + new String(letrasB).trim());
                        System.out.println("Cantidad: " + raf.readInt());
                        System.out.println("Precio: " + raf.readDouble() + " €");
                        break;

                    case 4: // ===== MODIFICAR un solo campo =====
                        System.out.print("Id a modificar: ");
                        int idMod = Integer.parseInt(sc.nextLine());
                        long posMod = (long) (idMod - 1) * TAM;
                        if (idMod < 1 || posMod >= raf.length()) {
                            System.out.println("No existe ese id");
                            break;
                        }
                        System.out.print("Nueva cantidad: ");
                        int nuevaCantidad = Integer.parseInt(sc.nextLine());
                        // Saltar el id (4) y el nombre (40) para caer justo en la cantidad
                        raf.seek(posMod + 4 + TAM_NOMBRE * 2);
                        raf.writeInt(nuevaCantidad);
                        System.out.println("Cantidad actualizada");
                        break;

                    case 5: // ===== BORRADO LÓGICO: id a 0 =====
                        System.out.print("Id a borrar: ");
                        int idBorrar = Integer.parseInt(sc.nextLine());
                        long posBorrar = (long) (idBorrar - 1) * TAM;
                        if (idBorrar < 1 || posBorrar >= raf.length()) {
                            System.out.println("No existe ese id");
                            break;
                        }
                        raf.seek(posBorrar);
                        raf.writeInt(0);                 // se marca como borrado
                        System.out.println("Producto borrado");
                        break;

                    case 0:
                        System.out.println("Adiós");
                        break;

                    default:
                        System.out.println("Opción no válida");
                }
            }

        } catch (IOException e) {
            System.out.println("Error con el fichero: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Tenías que escribir un número");
        }
    }
}
