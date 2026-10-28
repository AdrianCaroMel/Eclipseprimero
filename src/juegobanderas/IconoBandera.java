package juegobanderas;

import java.awt.*;
import javax.swing.Icon;

/**
 * Icono que dibuja la bandera de un país con Graphics2D.
 * Se asigna a un JLabel con setIcon(...) o en su constructor.
 *
 * Ventaja: no hace falta cargar imágenes externas (el proyecto funciona
 * tal cual en Eclipse) y la bandera se puede dibujar a cualquier tamaño.
 */
public class IconoBandera implements Icon {

    private final Pais pais;
    private final int ancho;
    private final int alto;

    public IconoBandera(Pais pais, int ancho, int alto) {
        this.pais = pais;
        this.ancho = ancho;
        this.alto = alto;
    }

    @Override public int getIconWidth()  { return ancho; }
    @Override public int getIconHeight() { return alto; }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(x, y);           // a partir de aquí dibujamos en (0,0)
        int w = ancho, h = alto;

        switch (pais) {
            case FRANCIA:      verticales(g2, w, h, rgb(0, 85, 164), Color.WHITE, rgb(239, 65, 53)); break;
            case ITALIA:       verticales(g2, w, h, rgb(0, 146, 70), Color.WHITE, rgb(206, 43, 55)); break;
            case IRLANDA:      verticales(g2, w, h, rgb(22, 155, 98), Color.WHITE, rgb(255, 136, 62)); break;
            case BELGICA:      verticales(g2, w, h, Color.BLACK, rgb(253, 218, 36), rgb(239, 51, 64)); break;

            case ALEMANIA:     horizontales(g2, w, h, Color.BLACK, rgb(221, 0, 0), rgb(255, 206, 0)); break;
            case PAISES_BAJOS: horizontales(g2, w, h, rgb(174, 28, 40), Color.WHITE, rgb(33, 70, 139)); break;
            case RUSIA:        horizontales(g2, w, h, Color.WHITE, rgb(0, 57, 166), rgb(213, 43, 30)); break;
            case AUSTRIA:      horizontales(g2, w, h, rgb(200, 16, 46), Color.WHITE, rgb(200, 16, 46)); break;
            case POLONIA:      horizontales(g2, w, h, Color.WHITE, rgb(220, 20, 60)); break;
            case INDONESIA:    horizontales(g2, w, h, rgb(206, 17, 38), Color.WHITE); break;
            case UCRANIA:      horizontales(g2, w, h, rgb(0, 87, 183), rgb(255, 215, 0)); break;

            case ESPANA: // rojo 1/4, amarillo 1/2, rojo 1/4
                rect(g2, rgb(170, 21, 27), 0, 0, w, h);
                rect(g2, rgb(241, 191, 0), 0, h / 4, w, h / 2);
                break;
            case COLOMBIA: // amarillo 1/2, azul 1/4, rojo 1/4
                rect(g2, rgb(252, 209, 22), 0, 0, w, h / 2);
                rect(g2, rgb(0, 56, 147), 0, h / 2, w, h / 4);
                rect(g2, rgb(206, 17, 38), 0, h * 3 / 4, w, h - h * 3 / 4);
                break;

            // Cruces nórdicas: proporciones reales (ancho izq/cruz/der, alto sup/cruz/inf)
            case SUECIA:
                cruzNordica(g2, w, h, rgb(0, 106, 167), rgb(254, 204, 0), new int[]{5, 2, 9}, new int[]{4, 2, 4});
                break;
            case FINLANDIA:
                cruzNordica(g2, w, h, Color.WHITE, rgb(0, 47, 108), new int[]{5, 3, 10}, new int[]{4, 3, 4});
                break;
            case DINAMARCA:
                cruzNordica(g2, w, h, rgb(200, 16, 46), Color.WHITE, new int[]{12, 4, 21}, new int[]{12, 4, 12});
                break;
            case NORUEGA: // cruz blanca y, encima, cruz azul más estrecha
                cruzNordica(g2, w, h, rgb(186, 12, 47), Color.WHITE, new int[]{6, 4, 12}, new int[]{6, 4, 6});
                cruzSinFondo(g2, w, h, rgb(0, 32, 91), new int[]{7, 2, 13}, new int[]{7, 2, 7});
                break;
            case SUIZA: {
                rect(g2, rgb(218, 41, 28), 0, 0, w, h);
                int brazo = h * 3 / 5, grosor = h / 5;
                rect(g2, Color.WHITE, (w - grosor) / 2, (h - brazo) / 2, grosor, brazo);
                rect(g2, Color.WHITE, (w - brazo) / 2, (h - grosor) / 2, brazo, grosor);
                break;
            }

            case JAPON: {
                rect(g2, Color.WHITE, 0, 0, w, h);
                int d = h * 3 / 5;
                g2.setColor(rgb(188, 0, 45));
                g2.fillOval((w - d) / 2, (h - d) / 2, d, d);
                break;
            }
            case BANGLADES: {
                rect(g2, rgb(0, 106, 78), 0, 0, w, h);
                int d = w * 2 / 5;
                int cx = w * 9 / 20;           // el círculo está desplazado hacia el mástil
                g2.setColor(rgb(244, 42, 65));
                g2.fillOval(cx - d / 2, (h - d) / 2, d, d);
                break;
            }
        }

        // Marco fino alrededor de la bandera
        g2.setColor(new Color(0, 0, 0, 90));
        g2.drawRect(0, 0, w - 1, h - 1);
        g2.dispose();
    }

    // ------------------------- Métodos auxiliares -------------------------

    private static Color rgb(int r, int g, int b) {
        return new Color(r, g, b);
    }

    private static void rect(Graphics2D g2, Color c, int x, int y, int w, int h) {
        g2.setColor(c);
        g2.fillRect(x, y, w, h);
    }

    /** Franjas verticales del mismo ancho. */
    private static void verticales(Graphics2D g2, int w, int h, Color... colores) {
        for (int i = 0; i < colores.length; i++) {
            int x0 = w * i / colores.length;
            int x1 = w * (i + 1) / colores.length;
            rect(g2, colores[i], x0, 0, x1 - x0, h);
        }
    }

    /** Franjas horizontales del mismo alto. */
    private static void horizontales(Graphics2D g2, int w, int h, Color... colores) {
        for (int i = 0; i < colores.length; i++) {
            int y0 = h * i / colores.length;
            int y1 = h * (i + 1) / colores.length;
            rect(g2, colores[i], 0, y0, w, y1 - y0);
        }
    }

    private static void cruzNordica(Graphics2D g2, int w, int h, Color fondo, Color cruz,
                                    int[] propAncho, int[] propAlto) {
        rect(g2, fondo, 0, 0, w, h);
        cruzSinFondo(g2, w, h, cruz, propAncho, propAlto);
    }

    /** Dibuja solo la cruz, calculando su posición a partir de proporciones. */
    private static void cruzSinFondo(Graphics2D g2, int w, int h, Color cruz,
                                     int[] propAncho, int[] propAlto) {
        int totalW = propAncho[0] + propAncho[1] + propAncho[2];
        int totalH = propAlto[0] + propAlto[1] + propAlto[2];
        int x = w * propAncho[0] / totalW;
        int grosorV = w * propAncho[1] / totalW;
        int y = h * propAlto[0] / totalH;
        int grosorH = h * propAlto[1] / totalH;
        rect(g2, cruz, x, 0, grosorV, h);   // barra vertical
        rect(g2, cruz, 0, y, w, grosorH);   // barra horizontal
    }
}
