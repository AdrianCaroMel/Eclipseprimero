package juegobanderas;

import java.awt.*;
import javax.swing.*;

/**
 * ListCellRenderer personalizado para la JList del historial.
 *
 * Cómo funciona un renderer:
 *  - La JList NO crea un componente por fila. Usa UN solo componente
 *    (este JLabel) como "sello": para cada fila llama a
 *    getListCellRendererComponent(...), lo configuramos con los datos de
 *    ese elemento y la JList lo "estampa" en su posición.
 *  - Por eso hay que configurar TODAS las propiedades en cada llamada
 *    (si no, se arrastran valores de la fila anterior).
 *  - El componente devuelto solo se pinta: no recibe clics. Por eso no se
 *    pueden poner botones "reales" dentro de una celda.
 *
 * Cada fila muestra: miniatura de la bandera, nombre del país, detalle
 * (puntos o lo que eligió el jugador) y una franja verde/roja a la izquierda.
 */
public class RespuestaCellRenderer extends JLabel implements ListCellRenderer<Respuesta> {

    private static final long serialVersionUID = 1L;

    private static final Color VERDE = new Color(40, 167, 69);
    private static final Color ROJO = new Color(220, 53, 69);
    private static final Color FONDO_VERDE = new Color(232, 246, 236);
    private static final Color FONDO_ROJO = new Color(252, 234, 236);
    private static final Color FONDO_SELECCION = new Color(205, 220, 245);

    public RespuestaCellRenderer() {
        setOpaque(true);                 // imprescindible para que se vea el fondo
        setIconTextGap(12);
        setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Respuesta> lista, Respuesta r,
                                                  int indice, boolean seleccionado, boolean tieneFoco) {
        Color colorEstado = r.isAcierto() ? VERDE : ROJO;

        // 1. Icono: la bandera en miniatura (reutilizamos IconoBandera)
        setIcon(new IconoBandera(r.getPais(), 48, 32));

        // 2. Texto en dos líneas con HTML
        String detalle;
        if (r.isAcierto()) {
            detalle = "+" + r.getPuntos() + " puntos";
        } else if (r.getRespuestaDada() == null) {
            detalle = "Tiempo agotado";
        } else {
            detalle = "Elegiste: " + r.getRespuestaDada();
        }
        setText("<html><b>" + r.getPais().getNombre() + "</b><br>"
                + "<span style='color:" + (r.isAcierto() ? "#1e7e34" : "#b02a37") + "'>"
                + detalle + "</span></html>");

        // 3. Fondo según estado y selección
        if (seleccionado) {
            setBackground(FONDO_SELECCION);
        } else {
            setBackground(r.isAcierto() ? FONDO_VERDE : FONDO_ROJO);
        }
        setForeground(Color.DARK_GRAY);

        // 4. Borde: franja de color a la izquierda + separación entre filas
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 6, 2, 0, colorEstado),
                BorderFactory.createEmptyBorder(6, 10, 6, 8)));

        // 5. Tooltip por fila (la JList lo pide al renderer)
        setToolTipText(r.isAcierto() ? "Acertaste " + r.getPais().getNombre()
                                     : "Era " + r.getPais().getNombre());

        // Respeta el estado habilitado de la lista
        setEnabled(lista.isEnabled());
        return this;
    }
}
