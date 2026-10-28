package juegobanderas;

/**
 * Países disponibles en el juego.
 * El "grupo" agrupa banderas parecidas: en modo difícil las opciones
 * incorrectas se eligen preferentemente del mismo grupo.
 */
public enum Pais {

    // Franjas verticales
    FRANCIA("Francia", Grupo.FRANJAS_VERTICALES),
    ITALIA("Italia", Grupo.FRANJAS_VERTICALES),
    IRLANDA("Irlanda", Grupo.FRANJAS_VERTICALES),
    BELGICA("Bélgica", Grupo.FRANJAS_VERTICALES),

    // Franjas horizontales
    ESPANA("España", Grupo.FRANJAS_HORIZONTALES),
    ALEMANIA("Alemania", Grupo.FRANJAS_HORIZONTALES),
    PAISES_BAJOS("Países Bajos", Grupo.FRANJAS_HORIZONTALES),
    RUSIA("Rusia", Grupo.FRANJAS_HORIZONTALES),
    AUSTRIA("Austria", Grupo.FRANJAS_HORIZONTALES),
    COLOMBIA("Colombia", Grupo.FRANJAS_HORIZONTALES),

    // Bicolores horizontales
    POLONIA("Polonia", Grupo.BICOLOR),
    INDONESIA("Indonesia", Grupo.BICOLOR),
    UCRANIA("Ucrania", Grupo.BICOLOR),

    // Cruces
    SUECIA("Suecia", Grupo.CRUZ),
    FINLANDIA("Finlandia", Grupo.CRUZ),
    DINAMARCA("Dinamarca", Grupo.CRUZ),
    NORUEGA("Noruega", Grupo.CRUZ),
    SUIZA("Suiza", Grupo.CRUZ),

    // Círculo
    JAPON("Japón", Grupo.CIRCULO),
    BANGLADES("Bangladés", Grupo.CIRCULO);

    public enum Grupo { FRANJAS_VERTICALES, FRANJAS_HORIZONTALES, BICOLOR, CRUZ, CIRCULO }

    private final String nombre;
    private final Grupo grupo;

    Pais(String nombre, Grupo grupo) {
        this.nombre = nombre;
        this.grupo = grupo;
    }

    public String getNombre() { return nombre; }
    public Grupo getGrupo()   { return grupo; }

    @Override
    public String toString() { return nombre; }
}
