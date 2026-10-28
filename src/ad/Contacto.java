package ad;

import java.io.Serializable;

// clase que representa UN contacto; se guarda en contactos.dat con serializacion
// implements Serializable = permiso para convertir el objeto en bytes (si falta -> NotSerializableException al guardar)
public class Contacto implements Serializable {

	// numero de version de la clase; si al leer no coincide -> InvalidClassException
	private static final long serialVersionUID = 1L;

	// un atributo de cada tipo: int, String, double, boolean
	private int id;
	private String nombre;
	private String telefono;
	private double saldo;
	private boolean eliminado;   // borrado logico: no se quita de la lista, se marca a true

	// transient = este atributo NO se guarda en el fichero; al leer vuelve con su valor por defecto (null, 0, false)
	private transient String notaTemporal;

	// constructor con todos los datos; eliminado no se pide porque un contacto nuevo siempre empieza activo
	public Contacto(int id, String nombre, String telefono, double saldo) {
		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.saldo = saldo;
		this.eliminado = false;
		this.notaTemporal = "solo en memoria";
	}

	// getters (leer desde fuera) y setters (cambiar desde fuera); los boolean usan isX en vez de getX
	// Eclipse: Alt+Shift+S -> Generate Getters and Setters
	public int getId() { return id; }
	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	public String getTelefono() { return telefono; }
	public void setTelefono(String telefono) { this.telefono = telefono; }
	public double getSaldo() { return saldo; }
	public void setSaldo(double saldo) { this.saldo = saldo; }
	public boolean isEliminado() { return eliminado; }
	public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }
	public String getNotaTemporal() { return notaTemporal; }

	// lo que sale al hacer System.out.println(contacto); se pone con el formato EXACTO del enunciado
	@Override
	public String toString() {
		return "Contacto " + id + ": " + nombre + " · " + telefono + " · " + saldo + " €";
	}
}
