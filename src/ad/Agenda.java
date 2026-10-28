package ad;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * AGENDA CON OBJETOS SERIALIZADOS (ObjectOutputStream / ObjectInputStream)
 *
 * Necesita Contacto.java y MiObjectOutputStream.java en la misma carpeta.
 *
 * - Guardar: writeObject(objeto)
 * - Leer:    (Contacto) readObject()  -> hay que hacer CAST
 * - El fichero no dice cuántos objetos tiene: se lee hasta que salta EOFException.
 * - Modificar o borrar: no se puede tocar un objeto suelto dentro del fichero.
 *   Se leen todos a un ArrayList, se cambia la lista y se reescribe el fichero entero.
 */
public class Agenda {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        File fichero = new File("agenda.dat");
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n--- AGENDA ---");
            System.out.println("1. Añadir contacto");
            System.out.println("2. Listar contactos");
            System.out.println("3. Buscar por nombre");
            System.out.println("4. Modificar teléfono");
            System.out.println("5. Borrar contacto");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {

                case 1: { // ===== AÑADIR AL FINAL =====
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Teléfono: ");
                    String telefono = sc.nextLine();
                    System.out.print("Edad: ");
                    int edad = Integer.parseInt(sc.nextLine());
                    Contacto1 c = new Contacto1(nombre, telefono, edad);

                    try {
                        ObjectOutputStream oos;
                        // Si el fichero ya tiene datos: append SIN cabecera
                        if (fichero.exists() && fichero.length() > 0) {
                            oos = new MiObjectOutputStream(new FileOutputStream(fichero, true));
                        } else {
                            // Si es nuevo: ObjectOutputStream normal (con cabecera)
                            oos = new ObjectOutputStream(new FileOutputStream(fichero));
                        }
                        oos.writeObject(c);
                        oos.close();
                        System.out.println("Contacto guardado");
                    } catch (IOException e) {
                        System.out.println("Error al guardar: " + e.getMessage());
                    }
                    break;
                }

                case 2: { // ===== LISTAR: leer hasta EOFException =====
                    if (!fichero.exists()) {
                        System.out.println("La agenda está vacía");
                        break;
                    }
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
                        while (true) {
                            Contacto1 c = (Contacto1) ois.readObject();
                            System.out.println(c);
                        }
                    } catch (EOFException e) {
                        // Normal: significa que ya no quedan objetos
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error al leer: " + e.getMessage());
                    }
                    break;
                }

                case 3: { // ===== BUSCAR por nombre =====
                    System.out.print("Nombre a buscar: ");
                    String buscado = sc.nextLine();
                    boolean encontrado = false;
                    if (fichero.exists()) {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
                            while (true) {
                                Contacto1 c = (Contacto1) ois.readObject();
                                if (c.getNombre().equalsIgnoreCase(buscado)) {
                                    System.out.println("Encontrado: " + c);
                                    encontrado = true;
                                }
                            }
                        } catch (EOFException e) {
                            // fin del fichero
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("Error al leer: " + e.getMessage());
                        }
                    }
                    if (!encontrado) {
                        System.out.println("No hay nadie con ese nombre");
                    }
                    break;
                }

                case 4:   // ===== MODIFICAR =====
                case 5: { // ===== BORRAR =====
                    System.out.print("Nombre del contacto: ");
                    String buscado = sc.nextLine();

                    // 1. Leer todos los objetos a una lista
                    ArrayList<Contacto1> lista = new ArrayList<Contacto1>();
                    if (fichero.exists()) {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
                            while (true) {
                                lista.add((Contacto1) ois.readObject());
                            }
                        } catch (EOFException e) {
                            // fin del fichero
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("Error al leer: " + e.getMessage());
                        }
                    }

                    // 2. Cambiar la lista
                    boolean cambiado = false;
                    for (int i = 0; i < lista.size(); i++) {
                        if (lista.get(i).getNombre().equalsIgnoreCase(buscado)) {
                            if (opcion == 4) {
                                System.out.print("Nuevo teléfono: ");
                                lista.get(i).setTelefono(sc.nextLine());
                            } else {
                                lista.remove(i);
                            }
                            cambiado = true;
                            break;
                        }
                    }
                    if (!cambiado) {
                        System.out.println("No hay nadie con ese nombre");
                        break;
                    }

                    // 3. Reescribir el fichero entero (sin append: se machaca)
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero))) {
                        for (Contacto1 c : lista) {
                            oos.writeObject(c);
                        }
                        System.out.println(opcion == 4 ? "Teléfono cambiado" : "Contacto borrado");
                    } catch (IOException e) {
                        System.out.println("Error al guardar: " + e.getMessage());
                    }
                    break;
                }

                case 0:
                    System.out.println("Adiós");
                    break;

                default:
                    System.out.println("Opción no válida");
            }
        }
    }
}
