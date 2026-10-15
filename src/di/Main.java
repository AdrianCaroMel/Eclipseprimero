package di;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        
        // Generamos la lista de bibliotecas que pide el enunciado
        ArrayList<Biblioteca> listaBibliotecas = new ArrayList<Biblioteca>();
        Biblioteca bibliotecaActual = null;

        try {
            // Inicializamos un par de bibliotecas de prueba
            listaBibliotecas.add(new Biblioteca("Ciencias"));
            listaBibliotecas.add(new Biblioteca("Letras"));
            
            // Empezamos por defecto con la primera
            bibliotecaActual = listaBibliotecas.get(0); 
            
            int opcion;
            do {
                System.out.println("--- GESTION DE BIBLIOTECAS ---");
                System.out.println("Biblioteca seleccionada: " + bibliotecaActual.getNombreBiblioteca());
                System.out.println("1. Cambiar de biblioteca");
                System.out.println("2. Agregar libro");
                System.out.println("3. Consultar libro");
                System.out.println("4. Modificar libro");
                System.out.println("5. Eliminar libro");
                System.out.println("6. Listar libros");
                System.out.println("7. Salir");
                System.out.print("Elige una opcion: ");
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.println("Bibliotecas disponibles:");
                        for (int i = 0; i < listaBibliotecas.size(); i++) {
                            System.out.println((i + 1) + ". " + listaBibliotecas.get(i).getNombreBiblioteca());
                        }
                        System.out.print("Elige una biblioteca (numero): ");
                        int numBiblio = teclado.nextInt();
                        teclado.nextLine();
                        if (numBiblio > 0 && numBiblio <= listaBibliotecas.size()) {
                            bibliotecaActual = listaBibliotecas.get(numBiblio - 1);
                            System.out.println("Has cambiado a la biblioteca: " + bibliotecaActual.getNombreBiblioteca());
                        } else {
                            System.out.println("Opcion incorrecta.");
                        }
                        break;
                    case 2:
                        System.out.print("ID del libro: ");
                        int id = teclado.nextInt();
                        teclado.nextLine();
                        System.out.print("Titulo del libro: ");
                        String titulo = teclado.nextLine();
                        System.out.print("Autor del libro: ");
                        String autor = teclado.nextLine();
                        System.out.print("Precio del libro: ");
                        float precio = teclado.nextFloat();
                        teclado.nextLine();

                        Libro nuevoLibro = new Libro(id, titulo, autor, precio, false);
                        bibliotecaActual.agregarLibro(nuevoLibro);
                        break;
                    case 3:
                        System.out.print("ID del libro que quieres consultar: ");
                        int idBuscar = teclado.nextInt();
                        Libro encontrado = bibliotecaActual.consultarLibro(idBuscar);
                        if (encontrado != null) {
                            System.out.println(encontrado);
                        } else {
                            System.out.println("Libro no encontrado o eliminado.");
                        }
                        break;
                    case 4:
                        System.out.print("ID del libro que quieres modificar: ");
                        int idModificar = teclado.nextInt();
                        System.out.print("Nuevo precio: ");
                        float nuevoPrecio = teclado.nextFloat();
                        teclado.nextLine();
                        bibliotecaActual.modificarLibro(idModificar, nuevoPrecio);
                        break;
                    case 5:
                        System.out.print("ID del libro que quieres eliminar: ");
                        int idEliminar = teclado.nextInt();
                        teclado.nextLine();
                        bibliotecaActual.eliminarLibro(idEliminar);
                        break;
                    case 6:
                        bibliotecaActual.listarLibros();
                        break;
                    case 7:
                        System.out.println("Saliendo del programa.");
                        break;
                    default:
                        System.out.println("Opcion incorrecta.");
                        break;
                }
            } while (opcion != 7);
            
        } catch (IOException e) {
            System.out.println("Error al trabajar con los ficheros.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al leer los objetos de los ficheros.");
        }
        teclado.close();
    }
}