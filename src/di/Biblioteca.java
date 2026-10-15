package di;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Biblioteca {
    private ArrayList<Libro> libros;
    private final String nombreFichero;
    private final String nombreBiblioteca;

    // Modificamos el constructor para que reciba el nombre de la biblioteca
    public Biblioteca(String nombre) throws IOException, ClassNotFoundException {
        this.nombreBiblioteca = nombre;
        this.nombreFichero = nombre + ".dat"; // Cada biblioteca tendrá su propio archivo
        libros = new ArrayList<Libro>();
        cargarLibros();
    }

    public String getNombreBiblioteca() {
        return nombreBiblioteca;
    }

    public void cargarLibros() throws IOException, ClassNotFoundException {
        libros.clear();
        File fichero = new File(nombreFichero);
        if (fichero.exists() && fichero.length() > 0) {
            ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(fichero));
            try {
                while (true) {
                    Libro libro = (Libro) entrada.readObject();
                    libros.add(libro);
                }
            } catch (java.io.EOFException e) {
                // Fin del fichero
            }
            entrada.close();
        }
    }

    public void guardarLibros() throws IOException {
        ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(nombreFichero));
        for (int i = 0; i < libros.size(); i++) {
            salida.writeObject(libros.get(i));
        }
        salida.close();
    }

    public void agregarLibro(Libro nuevoLibro) throws IOException, ClassNotFoundException {
        cargarLibros();
        for (int i = 0; i < libros.size(); i++) {
            if (libros.get(i).getId() == nuevoLibro.getId()) {
                System.out.println("El libro con el ID " + nuevoLibro.getId() + " ya existe y no se puede agregar.");
                return;
            }
        }
        libros.add(nuevoLibro);
        guardarLibros();
        System.out.println("Libro agregado correctamente.");
    }

    public Libro consultarLibro(int id) throws IOException, ClassNotFoundException {
        cargarLibros();
        for (int i = 0; i < libros.size(); i++) {
            // Consultamos solo si el ID coincide y además no está eliminado
            if (libros.get(i).getId() == id && !libros.get(i).isEliminado()) {
                return libros.get(i);
            }
        }
        return null;
    }

    // --- NUEVOS MÉTODOS DE LA FASE 2 ---

    public void modificarLibro(int id, float nuevoPrecio) throws IOException, ClassNotFoundException {
        cargarLibros();
        for (int i = 0; i < libros.size(); i++) {
            if (libros.get(i).getId() == id && !libros.get(i).isEliminado()) {
                libros.get(i).setPrecio(nuevoPrecio); // Actualizamos el precio
                guardarLibros(); // Guardamos toda la lista en el fichero
                System.out.println("Precio modificado con éxito.");
                return;
            }
        }
        System.out.println("El libro no existe o ya ha sido eliminado.");
    }

    public void eliminarLibro(int id) throws IOException, ClassNotFoundException {
        cargarLibros();
        for (int i = 0; i < libros.size(); i++) {
            if (libros.get(i).getId() == id && !libros.get(i).isEliminado()) {
                libros.get(i).setEliminado(true); // Borrado lógico
                guardarLibros(); // Guardamos los cambios
                System.out.println("Libro eliminado con éxito.");
                return;
            }
        }
        // Mensaje exacto que pide el enunciado
        System.out.println("no se puede eliminar el libro");
    }

    public void listarLibros() throws IOException, ClassNotFoundException {
        cargarLibros();
        boolean hayLibros = false;
        System.out.println("\n--- LISTA DE LIBROS ACTIVOS ---");
        for (int i = 0; i < libros.size(); i++) {
            if (!libros.get(i).isEliminado()) { // Solo mostramos los que no están eliminados
                System.out.println(libros.get(i).toString());
                hayLibros = true;
            }
        }
        if (!hayLibros) {
            System.out.println("No hay libros activos en esta biblioteca.");
        }
    }
}