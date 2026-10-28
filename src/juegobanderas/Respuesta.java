package juegobanderas;

/**
 * Modelo de una respuesta del jugador. Es el tipo de dato que guarda la
 * JList del historial (JList<Respuesta>) y que recibe el ListCellRenderer.
 */
public class Respuesta {

    private final Pais pais;              // bandera que se preguntaba
    private final String respuestaDada;   // lo que eligió el jugador (null = se acabó el tiempo)
    private final boolean acierto;
    private final int puntos;

    public Respuesta(Pais pais, String respuestaDada, boolean acierto, int puntos) {
        this.pais = pais;
        this.respuestaDada = respuestaDada;
        this.acierto = acierto;
        this.puntos = puntos;
    }

    public Pais getPais()            { return pais; }
    public String getRespuestaDada() { return respuestaDada; }
    public boolean isAcierto()       { return acierto; }
    public int getPuntos()           { return puntos; }

    /** Si no hubiera renderer, la JList mostraría este texto. */
    @Override
    public String toString() {
        return pais.getNombre() + (acierto ? " (acierto)" : " (fallo)");
    }
}
