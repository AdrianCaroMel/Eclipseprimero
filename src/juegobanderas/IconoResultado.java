package juegobanderas;

import java.awt.*;
import javax.swing.Icon;

/**
 * Icono para los mensajes de resultado, dibujado con Graphics2D:
 * - ACIERTO: check verde.
 * - FALLO:   aspa roja.
 */
public class IconoResultado implements Icon {

    public enum Tipo { ACIERTO, FALLO }

    private final Tipo tipo;
    private final int tam;

    public IconoResultado(Tipo tipo, int tam) {
        this.tipo = tipo;
        this.tam = tam;
    }

    @Override public int getIconWidth()  { return tam; }
    @Override public int getIconHeight() { return tam; }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(x, y);

        Color color = (tipo == Tipo.FALLO) ? new Color(220, 30, 40) : new Color(40, 167, 69);

        // Círculo claro de fondo
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 35));
        g2.fillOval(0, 0, tam, tam);

        // Trazo grueso con extremos redondeados
        g2.setColor(color);
        g2.setStroke(new BasicStroke(tam / 7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int m = tam / 4;   // margen
        if (tipo == Tipo.FALLO) {
            // Aspa roja: dos diagonales
            g2.drawLine(m, m, tam - m, tam - m);
            g2.drawLine(tam - m, m, m, tam - m);
        } else {
            // Check verde
            g2.drawPolyline(new int[]{m, tam * 5 / 12, tam - m},
                            new int[]{tam / 2, tam - m - tam / 12, m + tam / 12}, 3);
        }
        g2.dispose();
    }
}
