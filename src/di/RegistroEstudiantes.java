package di;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class RegistroEstudiantes {

	// tamaño fijo reservado para el nombre dentro del registro
	static final int LONGITUD_NOMBRE = 20;

	// tamaño total de un registro: 4 bytes del id + 20 caracteres del nombre (2 bytes cada uno) + 4 bytes de la nota
	static final int TAMANO_REGISTRO = 4 + (LONGITUD_NOMBRE * 2) + 4;

	public static void main(String[] args) {

		Scanner teclado = new Scanner(System.in);
		int opcion;

		do {
			// menu principal por consola
			System.out.println("1. Escribir estudiante");
			System.out.println("2. Leer estudiante");
			System.out.println("3. Salir");
			System.out.print("Elige una opcion: ");
			opcion = teclado.nextInt();

			switch (opcion) {

				case 1:
					// pedimos los datos del estudiante al usuario
					System.out.print("Id del estudiante: ");
					int id = teclado.nextInt();
					teclado.nextLine();

					System.out.print("Nombre del estudiante: ");
					String nombre = teclado.nextLine();

					System.out.print("Nota del estudiante: ");
					float nota = teclado.nextFloat();

					// abrimos el fichero en modo lectura/escritura y llamamos al metodo que escribe el registro
					try {
						RandomAccessFile fichero = new RandomAccessFile("estudiantes.dat", "rw");
						escribir(fichero, id, nombre, nota);
						fichero.close();
					} catch (IOException e) {
						System.out.println("Error al escribir en el fichero.");
					}
					break;

				case 2:
					System.out.print("Id del estudiante a buscar: ");
					int idBuscado = teclado.nextInt();

					// abrimos el fichero y llamamos al metodo que busca y muestra el registro
					try {
						RandomAccessFile fichero = new RandomAccessFile("estudiantes.dat", "rw");
						leer(fichero, idBuscado);
						fichero.close();
					} catch (IOException e) {
						System.out.println("Error al leer el fichero.");
					}
					break;

				case 3:
					System.out.println("Saliendo...");
					break;

				default:
					System.out.println("Opcion no valida.");
			}

		} while (opcion != 3);

		teclado.close();
	}

	static void escribir(RandomAccessFile fichero, int id, String nombre, float nota) throws IOException {

		// nos posicionamos al inicio del registro que corresponde a ese id
		// multiplicamos el id por el tamaño de un registro para calcular el byte exacto donde empieza
		fichero.seek((long) id * TAMANO_REGISTRO);

		// escribimos el id, que siempre ocupa 4 bytes
		fichero.writeInt(id);

		// metemos el nombre en un StringBuilder para poder ajustarlo a 20 caracteres
		StringBuilder nombreFijo = new StringBuilder(nombre);

		// si el nombre se pasa de 20 caracteres, lo recortamos
		if (nombreFijo.length() > LONGITUD_NOMBRE) {
			nombreFijo.setLength(LONGITUD_NOMBRE);
		} else {
			// si el nombre es mas corto de 20 caracteres, rellenamos con espacios hasta completarlo
			while (nombreFijo.length() < LONGITUD_NOMBRE) {
				nombreFijo.append(" ");
			}
		}

		// escribimos el nombre caracter a caracter, para que siempre ocupe exactamente 20 posiciones
		for (int i = 0; i < LONGITUD_NOMBRE; i++) {
			fichero.writeChar(nombreFijo.charAt(i));
		}

		// por ultimo escribimos la nota, que ocupa 4 bytes
		fichero.writeFloat(nota);
	}

	static void leer(RandomAccessFile fichero, int id) throws IOException {

		// calculamos la posicion donde deberia estar el registro de ese id
		long posicion = (long) id * TAMANO_REGISTRO;

		// si esa posicion esta fuera del tamaño del fichero, es que ese id no se ha escrito nunca
		if (posicion >= fichero.length()) {
			System.out.println("Registro no encontrado.");
			return;
		}

		// nos posicionamos en el registro encontrado
		fichero.seek(posicion);

		// leemos el id, que ocupa 4 bytes
		int idLeido = fichero.readInt();

		// leemos los 20 caracteres del nombre uno a uno y los guardamos en un array de char
		char[] letras = new char[LONGITUD_NOMBRE];
		for (int i = 0; i < LONGITUD_NOMBRE; i++) {
			letras[i] = fichero.readChar();
		}

		// convertimos el array de char en String y quitamos los espacios en blanco que se añadieron al escribir
		String nombre = new String(letras).trim();

		// leemos la nota, que ocupa 4 bytes
		float nota = fichero.readFloat();

		// mostramos la informacion del estudiante por pantalla
		System.out.println("El estudiante con ID " + idLeido + ", de nombre " + nombre + " tiene como calificacion " + nota);
	}
}