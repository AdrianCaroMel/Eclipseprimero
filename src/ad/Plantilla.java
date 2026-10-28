package ad;

import java.io.*;               // ficheros: File, RandomAccessFile, Object streams, IOException...
import java.util.ArrayList;     // lista de objetos en memoria
import java.util.Scanner;       // teclado

/*
 * PLANTILLA EXAMEN: RandomAccessFile (productos.dat) + objetos serializados (contactos.dat) + menu
 * Usa la clase Contacto (Serializable) que ya tienes.
 * PASOS EN EL EXAMEN: 1) calcular TAM  2) copiar mensajes EXACTOS del enunciado  3) adaptar nombres
 */
public class Plantilla {

	// === TAMAÑOS (int 4, char 2, double 8) ===
	static final int LONG_NOMBRE = 25;                    // letras del nombre (lo dice el enunciado)
	static final int TAM = 4 + LONG_NOMBRE * 2 + 8;       // bytes de UN registro = 62
	static final int DESPL_PRECIO = 4 + LONG_NOMBRE * 2;  // donde empieza el precio DENTRO del registro = 54

	static Scanner teclado = new Scanner(System.in);

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		// === 1. CARGAR OBJETOS AL ARRANCAR (aunque el fichero no exista) ===
		ArrayList<Contacto> lista = new ArrayList<Contacto>();     // si no hay fichero, se queda vacia
		File fObj = new File("contactos.dat");
		if (fObj.exists()) {                                       // solo leer si existe
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fObj))) {
				lista = (ArrayList<Contacto>) in.readObject();     // lee la lista ENTERA; cast porque devuelve Object
			} catch (IOException e) {
				System.out.println("Error al leer el fichero");
			} catch (ClassNotFoundException e) {                   // obligatoria al leer objetos
				System.out.println("Clase no encontrada");
			}
		}

		// === 2. MENU: se repite hasta 0 ===
		int opcion = -1;
		do {
			System.out.println("\n1. Escribir producto  2. Leer producto  3. Modificar precio");
			System.out.println("4. Alta contacto  5. Listar contactos  6. Baja contacto  0. Salir");
			System.out.print("Opcion: ");

			// leer opcion sin que una letra cierre el programa
			try {
				opcion = Integer.parseInt(teclado.nextLine());     // texto -> numero
			} catch (NumberFormatException e) {
				System.out.println("Debes introducir un numero");
				continue;                                          // vuelve a mostrar el menu
			}

			// try DENTRO del bucle: un error de fichero no cierra el programa
			try {
				switch (opcion) {

				// ---------- RANDOM ACCESS: ESCRIBIR ----------
				case 1: {
					System.out.print("Codigo: ");
					int codigo = Integer.parseInt(teclado.nextLine());
					System.out.print("Nombre: ");
					String nombre = teclado.nextLine();
					System.out.print("Precio: ");
					double precio = Double.parseDouble(teclado.nextLine());

					RandomAccessFile raf = new RandomAccessFile("productos.dat", "rw");  // rw: crea si no existe
					raf.seek((long) codigo * TAM);           // ir al registro: codigo x tamaño

					raf.writeInt(codigo);                    // 4 bytes

					StringBuilder sb = new StringBuilder(nombre);
					sb.setLength(LONG_NOMBRE);               // recorta o rellena hasta 25 (rellena con '\0')
					raf.writeChars(sb.toString().replace('\0', ' '));   // 25 chars = 50 bytes; '\0' -> espacio

					raf.writeDouble(precio);                 // 8 bytes

					System.out.println("Producto guardado. Longitud: " + raf.length() + " bytes");
					raf.close();
					break;
				}

				// ---------- RANDOM ACCESS: LEER ----------
				case 2: {
					System.out.print("Codigo: ");
					int codigo = Integer.parseInt(teclado.nextLine());
					RandomAccessFile raf = new RandomAccessFile("productos.dat", "rw");
					long pos = (long) codigo * TAM;

					if (pos >= raf.length()) {                       // fuera del fichero
						System.out.println("Producto no encontrado");
					} else {
						raf.seek(pos);
						int cod = raf.readInt();
						if (cod == 0) {                              // hueco (todo ceros)
							System.out.println("Producto no encontrado");
						} else {
							char[] letras = new char[LONG_NOMBRE];   // leer los 25 chars
							for (int i = 0; i < LONG_NOMBRE; i++) {
								letras[i] = raf.readChar();
							}
							String nombre = new String(letras).trim();   // trim quita el relleno
							double precio = raf.readDouble();
							System.out.println(cod + " - " + nombre + " - " + precio);
						}
					}
					raf.close();
					break;
				}

				// ---------- RANDOM ACCESS: MODIFICAR UN CAMPO ----------
				case 3: {
					System.out.print("Codigo: ");
					int codigo = Integer.parseInt(teclado.nextLine());
					System.out.print("Nuevo precio: ");
					double nuevo = Double.parseDouble(teclado.nextLine());
					RandomAccessFile raf = new RandomAccessFile("productos.dat", "rw");
					long pos = (long) codigo * TAM;

					if (pos >= raf.length()) {
						System.out.println("Producto no encontrado");
					} else {
						raf.seek(pos);
						if (raf.readInt() == 0) {                    // readInt MUEVE el puntero 4 bytes
							System.out.println("Producto no encontrado");
						} else {
							raf.seek(pos + DESPL_PRECIO);            // volver al sitio exacto del precio
							raf.writeDouble(nuevo);
							System.out.println("Precio actualizado");
						}
					}
					raf.close();
					break;
				}

				// ---------- OBJETOS: ALTA ----------
				case 4: {
					System.out.print("ID: ");
					int id = Integer.parseInt(teclado.nextLine());

					boolean existe = false;                          // ¿ya hay uno activo con ese id?
					for (int i = 0; i < lista.size(); i++) {
						if (lista.get(i).getId() == id && !lista.get(i).isEliminado()) {
							existe = true;
						}
					}
					if (existe) {
						System.out.println("Ya existe");
					} else {
						System.out.print("Nombre: ");
						String nombre = teclado.nextLine();
						System.out.print("Telefono: ");
						String tel = teclado.nextLine();
						System.out.print("Saldo: ");
						double saldo = Double.parseDouble(teclado.nextLine());

						lista.add(new Contacto(id, nombre, tel, saldo));   // 1) cambiar la lista
						guardar(lista);                                    // 2) guardarla entera
						System.out.println("Contacto agregado");
					}
					break;
				}

				// ---------- OBJETOS: LISTAR ACTIVOS ----------
				case 5: {
					boolean hay = false;
					for (int i = 0; i < lista.size(); i++) {
						if (!lista.get(i).isEliminado()) {
							System.out.println(lista.get(i));        // usa toString de Contacto
							hay = true;
						}
					}
					if (!hay) {
						System.out.println("No hay contactos");
					}
					break;
				}

				// ---------- OBJETOS: BAJA LOGICA ----------
				case 6: {
					System.out.print("ID: ");
					int id = Integer.parseInt(teclado.nextLine());
					boolean hecho = false;
					for (int i = 0; i < lista.size(); i++) {
						if (lista.get(i).getId() == id && !lista.get(i).isEliminado()) {
							lista.get(i).setEliminado(true);         // no se borra, se marca
							guardar(lista);
							hecho = true;
						}
					}
					System.out.println(hecho ? "Contacto eliminado" : "No encontrado");
					break;
				}

				case 0:
					System.out.println("Saliendo...");
					break;

				default:
					System.out.println("Opcion no valida");
				}

			} catch (IOException e) {                                // errores de fichero
				System.out.println("Error de fichero: " + e.getMessage());
			} catch (NumberFormatException e) {                      // letras al pedir codigo/precio/id
				System.out.println("Debes introducir un numero");
			}

		} while (opcion != 0);
	}

	// guarda la lista ENTERA como un solo objeto (sobrescribe el fichero)
	static void guardar(ArrayList<Contacto> lista) throws IOException {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("contactos.dat"))) {
			out.writeObject(lista);
		}
	}
}
