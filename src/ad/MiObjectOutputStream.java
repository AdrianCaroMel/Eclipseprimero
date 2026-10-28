package ad;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/*
 * Sirve para AÑADIR objetos a un fichero que ya tiene objetos.
 *
 * Problema: cada ObjectOutputStream nuevo escribe una CABECERA al principio.
 * Si abres el fichero en modo append con un ObjectOutputStream normal, queda
 * una segunda cabecera en mitad del fichero y al leer salta
 * StreamCorruptedException.
 *
 * Solución: esta clase no escribe la cabecera.
 */
public class MiObjectOutputStream extends ObjectOutputStream {

    public MiObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // No se escribe nada: así no se duplica la cabecera
    }
}
