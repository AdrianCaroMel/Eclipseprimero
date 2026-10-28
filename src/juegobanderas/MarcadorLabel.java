package juegobanderas;

import java.awt.*;
import javax.swing.*;

/**
 * Marcador personalizado: hereda de JLabel.
 *
 * - Fondo oscuro redondeado con borde dorado.
 * - Destello verde al acertar y rojo al fallar.
 * - Los puntos "suben" poco a poco (animación con javax.swing.Timer).
 * - Guarda el estado de la partida: puntos, aciertos, fallos y racha.
 */
public class MarcadorLabel extends JLabel {

    private static final long serialVersionUID = 1L;

    private static final Color FONDO = new Color(33, 37, 41);
    private static final Color BORDE = new Color(255, 193, 7);
    private static final Color VERDE = new Color(40, 167, 69);
    private static final Color ROJO = new Color(220, 53, 69);

    private int puntos;
    private int puntosMostrados;   // valor que se ve mientras dura la animación
    private int aciertos;
    private int fallos;
    private int racha;
    private int total;

    private Color colorDestello;   // null = sin destello
    private final Timer timerDestello;
    private final Timer timerContador;

    public MarcadorLabel() {
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 17));
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

        // Quita el destello pasados 600 ms
        timerDestello = new Timer(600, e -> {
            colorDestello = null;
            repaint();
        });
        timerDestello.setRepeats(false);

        // Animación del contador de puntos
        timerContador = new Timer(15, e -> {
            if (puntosMostrados < puntos) {
                puntosMostrados += Math.max(1, (puntos - puntosMostrados) / 8);
            } else {
                puntosMostrados = puntos;
                ((Timer) e.getSource()).stop();
            }
            actualizarTexto();
        });

        actualizarTexto();
    }

    /** Pone el marcador a cero para una partida de 'total' banderas. */
    public void reiniciar(int total) {
        this.total = total;
        puntos = puntosMostrados = aciertos = fallos = racha = 0;
        actualizarTexto();
    }

    /** Suma un acierto con los puntos indicados y aumenta la racha. */
    public void registrarAcierto(int puntosGanados) {
        aciertos++;
        racha++;
        puntos += puntosGanados;
        destello(VERDE);
        timerContador.restart();
    }

    /** Suma un fallo y corta la racha. */
    public void registrarFallo() {
        fallos++;
        racha = 0;
        destello(ROJO);
        actualizarTexto();
    }

    private void destello(Color color) {
        colorDestello = color;
        repaint();
        timerDestello.restart();
    }

    private void actualizarTexto() {
        setText(String.format("PUNTOS: %d     Aciertos: %d/%d     Fallos: %d     Racha: %d",
                puntosMostrados, aciertos, total, fallos, racha));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(colorDestello != null ? colorDestello : FONDO);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);

        g2.setStroke(new BasicStroke(2.5f));
        g2.setColor(BORDE);
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 24, 24);
        g2.dispose();

        super.paintComponent(g);   // JLabel pinta el texto encima
    }

    // ---------- Getters ----------
    public int getPuntos()   { return puntos; }
    public int getAciertos() { return aciertos; }
    public int getFallos()   { return fallos; }
    public int getRacha()    { return racha; }
    public int getTotal()    { return total; }
}
