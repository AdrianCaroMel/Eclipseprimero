package ad;

import java.io.*;               // File, RandomAccessFile, todos los Stream, Reader, Writer y las excepciones de ficheros
import java.util.ArrayList;     // la lista de contactos en memoria
import java.util.Scanner;       // leer del teclado

public class MegaMain {

	// ---------- CONSTANTES DEL ACCESO ALEATORIO (productos.dat) ----------
	// todos los registros miden lo mismo para poder calcular donde empieza cada uno
	// int = 4 bytes, char = 2 bytes, double = 8 bytes (el tamaño NO depende del valor guardado)
	static final int LONG_NOMBRE = 25;                     // letras que tiene SIEMPRE el nombre
	static final int TAM = 4 + LONG_NOMBRE * 2 + 8;        // codigo + nombre + precio = 62 bytes
	static final int DESPL_PRECIO = 4 + LONG_NOMBRE * 2;   // bytes que hay antes del precio dentro del registro = 54

	// Scanner static para poder usarlo en el main y en leerInt/leerDouble
	static Scanner teclado = new Scanner(System.in);

	@SuppressWarnings("unchecked")   // quita el aviso amarillo del cast (ArrayList<Contacto>)
	public static void main(String[] args) {

		// ==================================================================
		// AL ARRANCAR: CARGAR LA LISTA DE CONTACTOS (DESERIALIZAR)
		// ==================================================================
		// la lista entera se guarda como UN solo objeto en contactos.dat
		ArrayList<Contacto> contactos = new ArrayList<Contacto>();
		File fContactos = new File("contactos.dat");   // File no lee ni escribe, solo representa la ruta

		// la primera vez el fichero no existe: solo leemos si existe, si no seguimos con la lista vacia
		if (fContactos.exists()) {
			// try-with-resources: lo que se abre en el parentesis se cierra solo
			// FileInputStream lee bytes del fichero; ObjectInputStream convierte esos bytes en objetos (Decorator)
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fContactos))) {
				// readObject devuelve Object, por eso el cast a la lista
				contactos = (ArrayList<Contacto>) in.readObject();
			} catch (IOException e) {
				System.out.println("Error al leer contactos: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				// la segunda excepcion obligatoria al leer objetos
				System.out.println("No se encuentra la clase Contacto");
			}
		}

		// ==================================================================
		// MENU: se repite hasta elegir 0
		// ==================================================================
		int opcion = -1;
		do {
			System.out.println("\n--- PRODUCTOS (acceso aleatorio) ---");
			System.out.println("1. Escribir producto   2. Leer producto   3. Cambiar precio   4. Listar productos");
			System.out.println("--- CONTACTOS (serializacion) ---");
			System.out.println("5. Alta   6. Listar   7. Sumar/restar saldo   8. Modificar telefono   9. Baja");
			System.out.println("--- TEXTO ---");
			System.out.println("10. Exportar informe.txt (suma y promedio)   11. Leer informe.txt   12. Añadir linea");
			System.out.println("--- OTROS ---");
			System.out.println("13. Copia de seguridad (bytes)   14. Serializar objeto a objeto   15. Info de un fichero");
			System.out.println("0. Salir");

			// leerInt lee con nextLine + parseInt: una letra NO cierra el programa (R6)
			opcion = leerInt("Opcion: ");

			// try DENTRO del bucle: si falla un fichero se muestra el error y el menu sigue (R6)
			try {
				switch (opcion) {

				// ##################################################################
				// PARTE 1: RandomAccessFile (productos.dat)
				// posicion de un registro = codigo * TAM     (codigo 3 -> byte 186)
				// cada read/write mueve el puntero hacia delante lo que ocupa el dato
				// ##################################################################

				case 1: {
					// ---------- ESCRIBIR UN PRODUCTO ----------
					int codigo = leerInt("Codigo: ");
					System.out.print("Nombre: ");
					String nombre = teclado.nextLine();
					double precio = leerDouble("Precio: ");

					// "rw" = leer y escribir, si no existe lo crea ("r" solo leer, si no existe -> FileNotFoundException)
					RandomAccessFile raf = new RandomAccessFile("productos.dat", "rw");

					// vamos al sitio de este producto; (long) porque seek pide long
					raf.seek((long) codigo * TAM);
					raf.writeInt(codigo);                         // 4 bytes

					// el nombre SIEMPRE con 25 caracteres: si es mas largo se corta, si es mas corto se rellena
					StringBuilder sb = new StringBuilder(nombre);
					while (sb.length() < LONG_NOMBRE) {
						sb.append(' ');                           // relleno con espacios hasta 25
					}
					sb.setLength(LONG_NOMBRE);                    // si era mas largo, lo recorta a 25
					raf.writeChars(sb.toString());                // 25 chars x 2 = 50 bytes (equivale al for con writeChar)

					raf.writeDouble(precio);                      // 8 bytes

					System.out.println("Producto guardado. Longitud del fichero: " + raf.length() + " bytes");
					raf.close();
					break;
				}

				case 2: {
					// ---------- LEER UN PRODUCTO ----------
					int codigo = leerInt("Codigo: ");
					RandomAccessFile raf = new RandomAccessFile("productos.dat", "rw");
					long pos = (long) codigo * TAM;

					// comprobacion 1: la posicion esta fuera del fichero -> no existe
					if (pos >= raf.length()) {
						System.out.println("Producto no encontrado");
					} else {
						raf.seek(pos);
						int codLeido = raf.readInt();

						// comprobacion 2: HUECO (sitio sin escribir, todo a ceros) -> codigo 0 -> no existe
						if (codLeido == 0) {
							System.out.println("Producto no encontrado");
						} else {
							// leemos los 25 chars en un array y lo pasamos a String; trim quita el relleno
							char[] letras = new char[LONG_NOMBRE];
							for (int i = 0; i < LONG_NOMBRE; i++) {
								letras[i] = raf.readChar();
							}
							String nombre = new String(letras).trim();
							double precio = raf.readDouble();
							System.out.println("Producto " + codLeido + ": " + nombre + " · " + precio + " €");
						}
					}
					raf.close();
					break;
				}

				case 3: {
					// ---------- CAMBIAR SOLO EL PRECIO ----------
					// no se reescribe todo: se salta directamente al precio (pos + 54) y se sobrescriben sus 8 bytes
					int codigo = leerInt("Codigo: ");
					double nuevo = leerDouble("Nuevo precio: ");
					RandomAccessFile raf = new RandomAccessFile("productos.dat", "rw");
					long pos = (long) codigo * TAM;

					if (pos >= raf.length()) {
						System.out.println("Producto no encontrado");
					} else {
						raf.seek(pos);
						if (raf.readInt() == 0) {
							System.out.println("Producto no encontrado");
						} else {
							// OJO: readInt ha movido el puntero 4 bytes -> hay que volver con seek antes de escribir
							raf.seek(pos + DESPL_PRECIO);
							raf.writeDouble(nuevo);
							System.out.println("Precio actualizado");
						}
					}
					raf.close();
					break;
				}

				case 4: {
					// ---------- LISTAR TODOS (saltando huecos) ----------
					File fProd = new File("productos.dat");
					if (!fProd.exists()) {
						System.out.println("No hay productos");
						break;
					}
					RandomAccessFile raf = new RandomAccessFile(fProd, "r");   // "r" porque solo leemos
					double total = 0;
					int cuantos = 0;

					// fin del fichero en RandomAccessFile: puntero < longitud
					while (raf.getFilePointer() < raf.length()) {
						// se lee el registro ENTERO siempre, aunque sea hueco, para no descolocar el puntero
						int codigo = raf.readInt();
						char[] letras = new char[LONG_NOMBRE];
						for (int i = 0; i < LONG_NOMBRE; i++) {
							letras[i] = raf.readChar();
						}
						double precio = raf.readDouble();

						if (codigo != 0) {
							System.out.println("Producto " + codigo + ": " + new String(letras).trim() + " · " + precio + " €");
							total = total + precio;   // SUMAR
							cuantos++;
						}
					}
					// PROMEDIO solo si hay alguno (dividir entre 0 no)
					if (cuantos > 0) {
						System.out.println("Total: " + total + " €   Promedio: " + (total / cuantos) + " €");
					}
					raf.close();
					break;
				}

				// ##################################################################
				// PARTE 2: SERIALIZACION (contactos.dat)
				// 1) se cambia la lista en memoria   2) se guarda la lista ENTERA con guardar()
				// ##################################################################

				case 5: {
					// ---------- ALTA (si no existe ya ese id activo) ----------
					int id = leerInt("ID: ");

					boolean existe = false;
					for (int i = 0; i < contactos.size(); i++) {
						if (contactos.get(i).getId() == id && !contactos.get(i).isEliminado()) {
							existe = true;
						}
					}

					if (existe) {
						System.out.println("El contacto con el ID " + id + " ya existe");
					} else {
						System.out.print("Nombre: ");
						String nombre = teclado.nextLine();
						System.out.print("Telefono: ");
						String telefono = teclado.nextLine();
						double saldo = leerDouble("Saldo: ");

						contactos.add(new Contacto(id, nombre, telefono, saldo));
						guardar(contactos);
						System.out.println("Contacto agregado");
					}
					break;
				}

				case 6: {
					// ---------- LISTAR ACTIVOS ----------
					boolean hay = false;
					for (int i = 0; i < contactos.size(); i++) {
						if (!contactos.get(i).isEliminado()) {
							System.out.println(contactos.get(i));   // usa el toString de Contacto
							hay = true;
						}
					}
					if (!hay) {
						System.out.println("No hay contactos");
					}
					break;
				}

				case 7: {
					// ---------- SUMAR / RESTAR (positivo suma, negativo resta) ----------
					int id = leerInt("ID: ");
					double cantidad = leerDouble("Cantidad (negativo para restar): ");

					boolean encontrado = false;
					for (int i = 0; i < contactos.size(); i++) {
						Contacto c = contactos.get(i);
						if (c.getId() == id && !c.isEliminado()) {
							encontrado = true;
							if (c.getSaldo() + cantidad < 0) {
								System.out.println("El saldo no puede quedar negativo");
							} else {
								c.setSaldo(c.getSaldo() + cantidad);   // cambiamos el objeto en memoria
								guardar(contactos);                    // y guardamos la lista
								System.out.println("Saldo actual: " + c.getSaldo() + " €");
							}
						}
					}
					if (!encontrado) {
						System.out.println("Contacto no encontrado");
					}
					break;
				}

				case 8: {
					// ---------- MODIFICAR UN STRING (telefono) ----------
					int id = leerInt("ID: ");
					boolean encontrado = false;
					for (int i = 0; i < contactos.size(); i++) {
						Contacto c = contactos.get(i);
						if (c.getId() == id && !c.isEliminado()) {
							System.out.print("Nuevo telefono: ");
							c.setTelefono(teclado.nextLine());
							guardar(contactos);
							System.out.println("Telefono actualizado");
							encontrado = true;
						}
					}
					if (!encontrado) {
						System.out.println("Contacto no encontrado");
					}
					break;
				}

				case 9: {
					// ---------- BAJA LOGICA (no se quita de la lista, se marca) ----------
					int id = leerInt("ID a eliminar: ");
					boolean hecho = false;
					for (int i = 0; i < contactos.size(); i++) {
						Contacto c = contactos.get(i);
						if (c.getId() == id && !c.isEliminado()) {
							c.setEliminado(true);
							guardar(contactos);
							hecho = true;
						}
					}
					if (hecho) {
						System.out.println("Contacto eliminado");
					} else {
						System.out.println("No se puede eliminar el contacto");
					}
					break;
				}

				// ##################################################################
				// PARTE 3: FICHEROS DE TEXTO (informe.txt)
				// Writer/Reader = texto; los Buffered usan un buffer -> menos accesos al disco
				// ##################################################################

				case 10: {
					// ---------- ESCRIBIR: una linea por contacto + suma + promedio ----------
					// sin true SOBRESCRIBE; con new FileWriter("informe.txt", true) AÑADE al final
					BufferedWriter bw = new BufferedWriter(new FileWriter("informe.txt"));
					double total = 0;
					int cuantos = 0;

					for (int i = 0; i < contactos.size(); i++) {
						Contacto c = contactos.get(i);
						if (!c.isEliminado()) {
							bw.write(c.getId() + ";" + c.getNombre() + ";" + c.getTelefono() + ";" + c.getSaldo());
							bw.newLine();                 // write NO pone salto de linea
							total = total + c.getSaldo();
							cuantos++;
						}
					}
					bw.write("TOTAL;" + total);
					bw.newLine();
					if (cuantos > 0) {
						bw.write("PROMEDIO;" + (total / cuantos));
						bw.newLine();
					}
					// close vuelca el buffer al disco; sin close el fichero puede quedar VACIO (flush vuelca sin cerrar)
					bw.close();
					System.out.println("Informe exportado (" + cuantos + " contactos)");
					break;
				}

				case 11: {
					// ---------- LEER LINEA A LINEA y trocear con split ----------
					File f = new File("informe.txt");
					if (!f.exists()) {
						System.out.println("No existe informe.txt");
						break;
					}
					BufferedReader br = new BufferedReader(new FileReader(f));
					String linea;
					// readLine devuelve null al llegar al final
					while ((linea = br.readLine()) != null) {
						String[] partes = linea.split(";");    // "1;Ana;600;20.5" -> [1] [Ana] [600] [20.5]
						if (partes[0].equals("TOTAL") || partes[0].equals("PROMEDIO")) {
							System.out.println(partes[0] + ": " + partes[1]);
						} else if (partes.length < 4) {
							// linea que no tiene el formato id;nombre;telefono;saldo (p.ej. añadida con la opcion 12)
							System.out.println(linea);
						} else {
							// texto -> numero con parseInt / parseDouble
							int id = Integer.parseInt(partes[0]);
							double saldo = Double.parseDouble(partes[3]);
							System.out.println(id + " - " + partes[1] + " tiene " + saldo + " €");
						}
					}
					br.close();
					break;
				}

				case 12: {
					// ---------- AÑADIR UNA LINEA AL FINAL (modo append) ----------
					System.out.print("Texto a añadir: ");
					String texto = teclado.nextLine();
					BufferedWriter bw = new BufferedWriter(new FileWriter("informe.txt", true));   // true = añadir
					bw.write(texto);
					bw.newLine();
					bw.close();
					System.out.println("Linea añadida");
					break;
				}

				// ##################################################################
				// OTROS: bytes, serializar uno a uno, File
				// ##################################################################

				case 13: {
					// ---------- COPIA DE SEGURIDAD BYTE A BYTE (vale para cualquier fichero) ----------
					File origen = new File("contactos.dat");
					if (!origen.exists()) {
						System.out.println("No hay nada que copiar");
						break;
					}
					FileInputStream entrada = new FileInputStream(origen);
					FileOutputStream salida = new FileOutputStream("contactos_backup.dat");   // si no existe lo crea
					int b;
					// read devuelve un byte (como int) o -1 al final
					while ((b = entrada.read()) != -1) {
						salida.write(b);
					}
					entrada.close();
					salida.close();
					System.out.println("Copia creada: contactos_backup.dat");
					break;
				}

				case 14: {
					// ---------- SERIALIZAR OBJETO A OBJETO (en vez de la lista entera) ----------
					// escribir: un writeObject por cada contacto
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("uno_a_uno.dat"));
					for (int i = 0; i < contactos.size(); i++) {
						out.writeObject(contactos.get(i));
					}
					out.close();

					// leer: readObject en bucle hasta que salta EOFException (asi se detecta el final)
					ObjectInputStream in = new ObjectInputStream(new FileInputStream("uno_a_uno.dat"));
					try {
						while (true) {
							Contacto c = (Contacto) in.readObject();
							// el atributo transient vuelve a null: no se guardo
							System.out.println(c + "   notaTemporal = " + c.getNotaTemporal());
						}
					} catch (EOFException e) {
						System.out.println("Fin del fichero");
					} catch (ClassNotFoundException e) {
						System.out.println("No se encuentra la clase Contacto");
					}
					in.close();
					break;
				}

				case 15: {
					// ---------- METODOS DE File ----------
					System.out.print("Nombre del fichero: ");
					File f = new File(teclado.nextLine());
					if (f.exists()) {
						System.out.println("Nombre: " + f.getName());
						System.out.println("Ruta: " + f.getAbsolutePath());
						System.out.println("Tamaño: " + f.length() + " bytes");
						System.out.println("Es fichero: " + f.isFile() + "   Es carpeta: " + f.isDirectory());
					} else {
						// createNewFile crea el fichero vacio
						f.createNewFile();
						System.out.println("No existia: creado vacio");
					}
					break;
				}

				case 0:
					System.out.println("Saliendo...");
					break;

				default:
					System.out.println("Opcion no valida");
				}

			} catch (IOException e) {
				// aqui llegan los errores de fichero de cualquier opcion; el menu sigue
				System.out.println("Error con el fichero: " + e.getMessage());
			}

		} while (opcion != 0);
	}

	// guarda la lista ENTERA como un unico objeto (sobrescribe contactos.dat)
	// throws IOException: el error se lanza al main, que lo captura en su catch
	static void guardar(ArrayList<Contacto> contactos) throws IOException {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("contactos.dat"))) {
			out.writeObject(contactos);
		}
	}

	// pide un entero; si se escribe una letra, parseInt lanza NumberFormatException y se vuelve a pedir
	static int leerInt(String mensaje) {
		while (true) {
			System.out.print(mensaje);
			try {
				return Integer.parseInt(teclado.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Debes introducir un numero");
			}
		}
	}

	// igual que leerInt pero para decimales; replace permite escribir 12,5 ademas de 12.5
	static double leerDouble(String mensaje) {
		while (true) {
			System.out.print(mensaje);
			try {
				return Double.parseDouble(teclado.nextLine().trim().replace(",", "."));
			} catch (NumberFormatException e) {
				System.out.println("Debes introducir un numero");
			}
		}
	}
}
