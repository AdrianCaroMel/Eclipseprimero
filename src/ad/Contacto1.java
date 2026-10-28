package ad;

import java.io.Serializable;

/*
 * Clase que se guarda en el fichero.
 * OBLIGATORIO: implements Serializable (si no, NotSerializableException).
 * serialVersionUID: número de versión de la clase; si cambia, no se pueden leer
 * los objetos guardados antes (InvalidClassException).
 */
public class Contacto1 implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private String telefono;
    private int edad;
    // transient = este atributo NO se guarda (al leerlo vale null / 0)
    private transient String notaTemporal;

    public Contacto1(String nombre, String telefono, int edad) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.edad = edad;
    }

    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public int getEdad() { return edad; }

    @Override
    public String toString() {
        return nombre + " | " + telefono + " | " + edad + " años";
    }
}
