package juegobanderas;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * Botón personalizado: hereda de JButton y redefine cómo se pinta.
 *
 * - Esquinas redondeadas con degradado y sombra.
 * - Tres estados de color: normal, ratón encima (hover) y pulsado.
 * - Color de fondo configurable; los colores de hover y pulsado se calculan solos.
 * - Pinta el texto él mismo para que también se lea bien cuando está deshabilitado.
 *
 * NOTA: si en clase usáis otra versión de BotonPersonalizado, basta con
 * sustituir este archivo; el resto del juego solo usa el constructor
 * (String) / (String, Color) y setColorFondo(Color).
 */
public class BotonPersonalizado extends JButton {

    private static final long serialVersionUID = 1L;

    // Propiedades con valores por defecto
    private Color colorFondo = new Color(52, 101, 164);
    private Color colorHover;
    private Color colorPulsado;
    private Color colorTexto = Color.WHITE;
    private int radio = 18;

    private boolean ratonEncima = false;

    public BotonPersonalizado(String texto) {
        super(texto);
        setColorFondo(colorFondo);                 // calcula hover y pulsado

        // Desactivamos el pintado estándar de Swing: lo hacemos nosotros
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        setForeground(colorTexto);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setBorder(BorderFactory.createEmptyBorder(9, 20, 11, 22)); // margen interior
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Efecto hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ratonEncima = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ratonEncima = false;
                repaint();
            }
        });
    }

    public BotonPersonalizado(String texto, Color fondo) {
        this(texto);
        setColorFondo(fondo);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth() - 3;   // dejamos 3 px para la sombra
        int h = getHeight() - 3;

        // 1. Elegir color según el estado
        Color base;
        if (!isEnabled()) {
            base = colorFondo;                       // mantiene su color (verde/rojo al responder)
        } else if (getModel().isPressed()) {
            base = colorPulsado;
        } else if (ratonEncima) {
            base = colorHover;
        } else {
            base = colorFondo;
        }

        // 2. Sombra
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillRoundRect(2, 3, w, h, radio, radio);

        // 3. Fondo con degradado vertical (al pulsar se "hunde" 1 px)
        int desp = getModel().isPressed() ? 1 : 0;
        g2.setPaint(new GradientPaint(0, 0, mezclar(base, Color.WHITE, 0.25f), 0, h, base));
        g2.fillRoundRect(desp, desp, w, h, radio, radio);

        // 4. Texto centrado (lo pintamos nosotros)
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int xTexto = desp + (w - fm.stringWidth(getText())) / 2;
        int yTexto = desp + (h - fm.getHeight()) / 2 + fm.getAscent();
        g2.setColor(isEnabled() ? colorTexto : new Color(255, 255, 255, 220));
        g2.drawString(getText(), xTexto, yTexto);

        g2.dispose();
    }

    /** Mezcla dos colores: proporcion = 0 devuelve a, 1 devuelve b. */
    private static Color mezclar(Color a, Color b, float proporcion) {
        float p = Math.max(0, Math.min(1, proporcion));
        return new Color(
                Math.round(a.getRed()   * (1 - p) + b.getRed()   * p),
                Math.round(a.getGreen() * (1 - p) + b.getGreen() * p),
                Math.round(a.getBlue()  * (1 - p) + b.getBlue()  * p));
    }

    // ---------- Getters / setters (propiedades del componente) ----------

    public Color getColorFondo() {
        return colorFondo;
    }

    /** Cambia el color base y recalcula automáticamente hover y pulsado. */
    public void setColorFondo(Color colorFondo) {
        this.colorFondo = colorFondo;
        this.colorHover = mezclar(colorFondo, Color.WHITE, 0.20f);
        this.colorPulsado = mezclar(colorFondo, Color.BLACK, 0.25f);
        repaint();
    }

    public Color getColorTexto() {
        return colorTexto;
    }

    public void setColorTexto(Color colorTexto) {
        this.colorTexto = colorTexto;
        repaint();
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
        repaint();
    }
}
