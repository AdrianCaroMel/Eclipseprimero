package di;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;


public class IntentoExamen {

    // =========================================================
    // VARIABLE DEL LIENZO
    // =========================================================

    /*
     * Esta variable representa el área donde dibujamos.
     *
     * La declaramos fuera del main porque necesitamos utilizarla
     * desde muchos sitios:
     *
     * - botones
     * - menús
     * - eventos del ratón
     *
     * Al ser static podemos utilizarla desde main.
     */

    static Lienzo lienzo;


    public static void main(String[] args) {


        // =====================================================
        // 1. CREAR LA VENTANA
        // =====================================================

        JFrame ventana = new JFrame("Paint");

        ventana.setSize(800, 600);

        ventana.setLocation(700, 300);

        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*
         * BorderLayout nos permite colocar:
         *
         * NORTH  -> arriba
         * SOUTH  -> abajo
         * EAST   -> derecha
         * WEST   -> izquierda
         * CENTER -> centro
         */

        ventana.setLayout(new BorderLayout());


        // =====================================================
        // 2. CREAR EL LIENZO
        // =====================================================

        /*
         * Creamos nuestro JPanel personalizado.
         *
         * Dentro de él vamos a guardar y dibujar las formas.
         */

        lienzo = new Lienzo();

        /*
         * El lienzo ocupa el centro de la ventana.
         */

        ventana.add(lienzo, BorderLayout.CENTER);


        // =====================================================
        // 3. CREAR TOOLBAR
        // =====================================================

        /*
         * JToolBar es la barra de herramientas.
         *
         * Aquí ponemos las herramientas que vamos a utilizar
         * constantemente.
         */

        JToolBar herramientas = new JToolBar();


        // -----------------------------------------------------
        // BOTONES DE HERRAMIENTAS
        // -----------------------------------------------------

        JButton pincel = new JButton("Pincel");

        JButton linea = new JButton("Línea");

        JButton circulo = new JButton("Círculo");

        JButton cuadrado = new JButton("Cuadrado");

        JButton borrador = new JButton("Borrador");


        /*
         * Añadimos los botones a la toolbar.
         */

        herramientas.add(pincel);

        herramientas.add(linea);

        herramientas.add(circulo);

        herramientas.add(cuadrado);

        herramientas.add(borrador);


        /*
         * Separa visualmente los botones del resto.
         */

        herramientas.addSeparator();


        // =====================================================
        // 4. GROSOR
        // =====================================================

        JLabel textoGrosor = new JLabel("Grosor:");


        /*
         * JSlider nos permite seleccionar el ancho del pincel.
         *
         * mínimo = 1
         * máximo = 40
         * valor inicial = 2
         */

        JSlider grosor = new JSlider(1, 40, 2);

        grosor.setPreferredSize(new Dimension(120, 40));


        herramientas.add(textoGrosor);

        herramientas.add(grosor);


        herramientas.addSeparator();


        // =====================================================
        // 5. COLOR
        // =====================================================

        JButton color = new JButton("Color");

        herramientas.add(color);


        // =====================================================
        // 6. BOTÓN PINCEL
        // =====================================================

        /*
         * El pincel simplemente cambia la herramienta actual
         * a "libre".
         *
         * No dibuja nada en este momento.
         *
         * Solo estamos indicando qué herramienta utilizaremos
         * cuando posteriormente movamos el ratón.
         */

        pincel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                lienzo.herramienta = "libre";
            }
        });


        // =====================================================
        // 7. BOTÓN LÍNEA
        // =====================================================

        linea.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                lienzo.herramienta = "linea";
            }
        });


        // =====================================================
        // 8. BOTÓN CÍRCULO
        // =====================================================

        circulo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                lienzo.herramienta = "circulo";
            }
        });


        // =====================================================
        // 9. BOTÓN CUADRADO
        // =====================================================

        cuadrado.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                lienzo.herramienta = "cuadrado";
            }
        });


        // =====================================================
        // 10. BOTÓN BORRADOR
        // =====================================================

        /*
         * El borrador será simplemente otra herramienta.
         *
         * Cuando la seleccionamos:
         *
         * herramienta = "borrador"
         *
         * Después el código del ratón verá que esa es la
         * herramienta seleccionada y dibujará con el color
         * del fondo.
         */

        borrador.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                lienzo.herramienta = "borrador";
            }
        });


        // =====================================================
        // 11. SLIDER DEL GROSOR
        // =====================================================

        /*
         * JSlider utiliza ChangeListener.
         *
         * stateChanged() se ejecuta cada vez que cambia
         * el valor del slider.
         */

        grosor.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                /*
                 * getValue() devuelve el número seleccionado.
                 *
                 * Por ejemplo:
                 *
                 * slider = 10
                 *
                 * getValue() devuelve 10.
                 */

                lienzo.grosorActual = grosor.getValue();
            }
        });


        // =====================================================
        // 12. SELECTOR DE COLOR
        // =====================================================

        /*
         * Pulsando el botón se abre el selector avanzado
         * de colores de Java.
         */

        color.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Color nuevoColor = JColorChooser.showDialog(
                        ventana,
                        "Selecciona un color",
                        lienzo.colorActual
                );


                /*
                 * Si el usuario pulsa Cancelar,
                 * nuevoColor será null.
                 *
                 * Solo cambiamos el color si realmente
                 * ha elegido uno.
                 */

                if (nuevoColor != null) {

                    lienzo.colorActual = nuevoColor;
                }
            }
        });


        // =====================================================
        // 13. AÑADIR TOOLBAR
        // =====================================================

        /*
         * La toolbar va arriba.
         */

        ventana.add(herramientas, BorderLayout.NORTH);


        // =====================================================
        // 14. CREAR MENÚ
        // =====================================================

        JMenuBar barraMenu = new JMenuBar();

        JMenu archivo = new JMenu("Archivo");

        JMenu herramientasMenu = new JMenu("Herramientas");


        // =====================================================
        // 15. MENÚ ARCHIVO
        // =====================================================

        JMenuItem nuevoMenu = new JMenuItem("Nuevo");

        JMenuItem salirMenu = new JMenuItem("Salir");


        archivo.add(nuevoMenu);

        archivo.addSeparator();

        archivo.add(salirMenu);


        // =====================================================
        // 16. MENÚ HERRAMIENTAS
        // =====================================================

        JMenuItem pincelMenu = new JMenuItem("Pincel");

        JMenuItem pipetaMenu = new JMenuItem("Pipeta");

        JMenuItem cuboMenu = new JMenuItem("Cubo rellenador");

        JMenuItem borradorMenu = new JMenuItem("Borrador");


        herramientasMenu.add(pincelMenu);

        herramientasMenu.add(pipetaMenu);

        herramientasMenu.add(cuboMenu);

        herramientasMenu.add(borradorMenu);


        // =====================================================
        // 17. AÑADIR MENÚS
        // =====================================================

        barraMenu.add(archivo);

        barraMenu.add(herramientasMenu);


        /*
         * setJMenuBar conecta nuestra barra de menús con
         * la ventana.
         */

        ventana.setJMenuBar(barraMenu);


        // =====================================================
        // 18. GETJMenuBar
        // =====================================================

        /*
         * getJMenuBar() nos devuelve la barra de menús
         * que tiene actualmente la ventana.
         *
         * Aquí no necesitamos hacer nada con ella,
         * pero es importante conocer el método.
         */

        JMenuBar menuActual = ventana.getJMenuBar();


        // =====================================================
        // 19. NUEVO
        // =====================================================

        /*
         * Nuevo borra todas las formas.
         */

        nuevoMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                lienzo.formas.clear();

                lienzo.repaint();
            }
        });


        // =====================================================
        // 20. SALIR
        // =====================================================

        salirMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                /*
                 * Cierra el programa completamente.
                 */

                System.exit(0);
            }
        });


        // =====================================================
        // 21. PINCEL DESDE EL MENÚ
        // =====================================================

        pincelMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                lienzo.herramienta = "libre";
            }
        });


        // =====================================================
        // 22. PIPETA
        // =====================================================

        pipetaMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                lienzo.herramienta = "pipeta";
            }
        });


        // =====================================================
        // 23. CUBO
        // =====================================================

        cuboMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                lienzo.herramienta = "cubo";
            }
        });


        // =====================================================
        // 24. BORRADOR DESDE EL MENÚ
        // =====================================================

        borradorMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                lienzo.herramienta = "borrador";
            }
        });


        // =====================================================
        // 25. RATÓN
        // =====================================================

        /*
         * MouseAdapter nos permite controlar los eventos
         * del ratón.
         *
         * Aquí necesitamos:
         *
         * mousePressed()
         * mouseReleased()
         *
         * El primero guarda dónde empezamos.
         *
         * El segundo sabe dónde terminamos.
         */

        lienzo.addMouseListener(new MouseAdapter() {

            // -------------------------------------------------
            // MOUSE PRESSED
            // -------------------------------------------------

            @Override
            public void mousePressed(MouseEvent e) {

                /*
                 * Guardamos la posición inicial.
                 */

                lienzo.xInicio = e.getX();

                lienzo.yInicio = e.getY();


                /*
                 * Para el pincel y el borrador también
                 * necesitamos guardar el punto anterior.
                 */

                lienzo.xAnterior = e.getX();

                lienzo.yAnterior = e.getY();


                // =================================================
                // PIPETA
                // =================================================

                if (lienzo.herramienta.equals("pipeta")) {

                    /*
                     * Recorremos desde la última forma hasta
                     * la primera.
                     *
                     * Así comprobamos primero la figura que
                     * está más arriba.
                     */

                    for (int i = lienzo.formas.size() - 1;
                         i >= 0;
                         i--) {

                        Forma forma = lienzo.formas.get(i);


                        /*
                         * Comprobamos si el clic está dentro
                         * de esa forma.
                         */

                        if (lienzo.estaDentro(
                                forma,
                                e.getX(),
                                e.getY())) {


                            /*
                             * Cogemos su color.
                             */

                            lienzo.colorActual = forma.color;


                            /*
                             * Ya hemos encontrado una figura,
                             * así que no necesitamos seguir
                             * buscando.
                             */

                            break;
                        }
                    }
                }


                // =================================================
                // CUBO RELLENADOR
                // =================================================

                else if (lienzo.herramienta.equals("cubo")) {


                    /*
                     * El relleno que hacemos aquí es SIMPLE.
                     *
                     * No buscamos píxeles conectados.
                     *
                     * Simplemente buscamos si hemos pulsado
                     * dentro de un círculo o cuadrado.
                     */

                    for (int i = lienzo.formas.size() - 1;
                         i >= 0;
                         i--) {


                        Forma forma = lienzo.formas.get(i);


                        /*
                         * Solo permitimos rellenar:
                         *
                         * círculo
                         * cuadrado
                         */

                        if (forma.tipo.equals("circulo")
                                || forma.tipo.equals("cuadrado")) {


                            if (lienzo.estaDentro(
                                    forma,
                                    e.getX(),
                                    e.getY())) {


                                /*
                                 * Guardamos el color actual
                                 * como color de relleno.
                                 */

                                forma.relleno =
                                        lienzo.colorActual;


                                break;
                            }
                        }
                    }


                    /*
                     * Volvemos a pintar el lienzo.
                     */

                    lienzo.repaint();
                }
            }


            // -------------------------------------------------
            // MOUSE RELEASED
            // -------------------------------------------------

            @Override
            public void mouseReleased(MouseEvent e) {

                /*
                 * Guardamos la posición final.
                 */

                int xFin = e.getX();

                int yFin = e.getY();


                // =================================================
                // LÍNEA
                // =================================================

                if (lienzo.herramienta.equals("linea")) {

                    Forma forma = new Forma();

                    forma.tipo = "linea";

                    forma.xInicio = lienzo.xInicio;

                    forma.yInicio = lienzo.yInicio;

                    forma.xFin = xFin;

                    forma.yFin = yFin;


                    /*
                     * Guardamos el color y grosor que estaban
                     * seleccionados EN EL MOMENTO DE CREAR
                     * la figura.
                     */

                    forma.color = lienzo.colorActual;

                    forma.grosor = lienzo.grosorActual;


                    lienzo.formas.add(forma);
                }


                // =================================================
                // CÍRCULO
                // =================================================

                else if (lienzo.herramienta.equals("circulo")) {

                    Forma forma = new Forma();

                    forma.tipo = "circulo";

                    forma.xInicio = lienzo.xInicio;

                    forma.yInicio = lienzo.yInicio;

                    forma.xFin = xFin;

                    forma.yFin = yFin;

                    forma.color = lienzo.colorActual;

                    forma.grosor = lienzo.grosorActual;

                    lienzo.formas.add(forma);
                }


                // =================================================
                // CUADRADO
                // =================================================

                else if (lienzo.herramienta.equals("cuadrado")) {

                    Forma forma = new Forma();

                    forma.tipo = "cuadrado";

                    forma.xInicio = lienzo.xInicio;

                    forma.yInicio = lienzo.yInicio;

                    forma.xFin = xFin;

                    forma.yFin = yFin;

                    forma.color = lienzo.colorActual;

                    forma.grosor = lienzo.grosorActual;

                    lienzo.formas.add(forma);
                }


                /*
                 * Volvemos a pintar.
                 */

                lienzo.repaint();
            }
        });


        // =====================================================
        // 26. MOVIMIENTO DEL RATÓN
        // =====================================================

        /*
         * MouseMotionAdapter nos permite detectar el
         * movimiento mientras mantenemos pulsado el ratón.
         *
         * mouseDragged() es la parte fundamental del
         * dibujo libre y del borrador.
         */

        lienzo.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {


                // =================================================
                // PINCEL
                // =================================================

                if (lienzo.herramienta.equals("libre")) {

                    /*
                     * Cada movimiento crea una pequeña línea.
                     *
                     * Por ejemplo:
                     *
                     * punto 1 → punto 2
                     * punto 2 → punto 3
                     * punto 3 → punto 4
                     *
                     * Muchas líneas pequeñas juntas parecen
                     * un trazo continuo.
                     */

                    Forma forma = new Forma();

                    forma.tipo = "linea";

                    forma.xInicio = lienzo.xAnterior;

                    forma.yInicio = lienzo.yAnterior;

                    forma.xFin = e.getX();

                    forma.yFin = e.getY();

                    forma.color = lienzo.colorActual;

                    forma.grosor = lienzo.grosorActual;

                    lienzo.formas.add(forma);


                    /*
                     * El punto actual pasa a ser el punto anterior
                     * para el próximo movimiento.
                     */

                    lienzo.xAnterior = e.getX();

                    lienzo.yAnterior = e.getY();


                    lienzo.repaint();
                }


                // =================================================
                // BORRADOR
                // =================================================

                else if (lienzo.herramienta.equals("borrador")) {

                    /*
                     * El borrador funciona prácticamente igual
                     * que el pincel.
                     *
                     * La diferencia es el color.
                     *
                     * En lugar del color que hemos seleccionado,
                     * usamos el color del fondo.
                     *
                     * De esta forma parece que estamos borrando.
                     */

                    Forma forma = new Forma();

                    forma.tipo = "linea";

                    forma.xInicio = lienzo.xAnterior;

                    forma.yInicio = lienzo.yAnterior;

                    forma.xFin = e.getX();

                    forma.yFin = e.getY();


                    /*
                     * El color del borrador será el fondo
                     * del JPanel.
                     */

                    forma.color = lienzo.getBackground();

                    forma.grosor = lienzo.grosorActual;


                    lienzo.formas.add(forma);


                    /*
                     * Actualizamos el punto anterior.
                     */

                    lienzo.xAnterior = e.getX();

                    lienzo.yAnterior = e.getY();


                    lienzo.repaint();
                }
            }
        });


        // =====================================================
        // 27. MOSTRAR LA VENTANA
        // =====================================================

        ventana.setVisible(true);
    }


    // =========================================================
    // CLASE LIENZO
    // =========================================================

    /*
     * Lienzo hereda de JPanel.
     *
     * Por eso podemos utilizar:
     *
     * paintComponent()
     *
     * y convertirlo en nuestra zona de dibujo.
     */

    static class Lienzo extends JPanel {


        // =====================================================
        // LISTA DE FORMAS
        // =====================================================

        /*
         * Aquí guardamos TODAS las figuras dibujadas.
         *
         * Ejemplo:
         *
         * formas
         *   ├── círculo
         *   ├── línea
         *   ├── cuadrado
         *   ├── línea
         *   └── círculo
         */

        ArrayList<Forma> formas =
                new ArrayList<Forma>();


        // =====================================================
        // HERRAMIENTA
        // =====================================================

        /*
         * Guarda qué herramienta tenemos seleccionada.
         */

        String herramienta = "libre";


        // =====================================================
        // COLOR
        // =====================================================

        /*
         * Color que utilizaremos para NUEVAS figuras.
         */

        Color colorActual = Color.BLACK;


        // =====================================================
        // GROSOR
        // =====================================================

        /*
         * Grosor para NUEVAS figuras.
         */

        int grosorActual = 2;


        // =====================================================
        // COORDENADAS
        // =====================================================

        int xInicio;

        int yInicio;

        int xAnterior;

        int yAnterior;


        // =====================================================
        // PAINT COMPONENT
        // =====================================================

        /*
         * paintComponent() es donde realmente se dibuja.
         *
         * Cuando llamamos:
         *
         * repaint()
         *
         * Swing vuelve a ejecutar este método.
         */

        @Override
        public void paintComponent(Graphics g) {

            /*
             * Primero dejamos que JPanel limpie y prepare
             * correctamente el área.
             */

            super.paintComponent(g);


            /*
             * Convertimos Graphics en Graphics2D porque
             * Graphics2D nos permite utilizar:
             *
             * setStroke()
             *
             * para cambiar el grosor de las líneas.
             */

            Graphics2D g2 = (Graphics2D) g;


            // =================================================
            // DIBUJAR TODAS LAS FORMAS
            // =================================================

            /*
             * Recorremos todas las formas guardadas.
             */

            for (Forma forma : formas) {


                /*
                 * Cada figura tiene su propio color.
                 */

                g2.setColor(forma.color);


                /*
                 * Cada figura tiene su propio grosor.
                 */

                g2.setStroke(
                        new BasicStroke(forma.grosor)
                );


                // =============================================
                // LÍNEA
                // =============================================

                if (forma.tipo.equals("linea")) {

                    g2.drawLine(
                            forma.xInicio,
                            forma.yInicio,
                            forma.xFin,
                            forma.yFin
                    );
                }


                // =============================================
                // CÍRCULO
                // =============================================

                else if (forma.tipo.equals("circulo")) {


                    /*
                     * Calculamos la esquina superior izquierda.
                     */

                    int x = Math.min(
                            forma.xInicio,
                            forma.xFin
                    );

                    int y = Math.min(
                            forma.yInicio,
                            forma.yFin
                    );


                    /*
                     * Calculamos ancho y alto.
                     *
                     * Math.abs() hace que siempre sean positivos.
                     */

                    int ancho = Math.abs(
                            forma.xFin - forma.xInicio
                    );

                    int alto = Math.abs(
                            forma.yFin - forma.yInicio
                    );


                    // -----------------------------------------
                    // RELLENO
                    // -----------------------------------------

                    if (forma.relleno != null) {

                        /*
                         * Primero rellenamos.
                         */

                        g2.setColor(forma.relleno);

                        g2.fillOval(
                                x,
                                y,
                                ancho,
                                alto
                        );


                        /*
                         * Volvemos a poner el color original
                         * para dibujar el borde.
                         */

                        g2.setColor(forma.color);
                    }


                    // -----------------------------------------
                    // BORDE
                    // -----------------------------------------

                    g2.drawOval(
                            x,
                            y,
                            ancho,
                            alto
                    );
                }


                // =============================================
                // CUADRADO
                // =============================================

                else if (forma.tipo.equals("cuadrado")) {


                    int x = Math.min(
                            forma.xInicio,
                            forma.xFin
                    );

                    int y = Math.min(
                            forma.yInicio,
                            forma.yFin
                    );


                    int ancho = Math.abs(
                            forma.xFin - forma.xInicio
                    );

                    int alto = Math.abs(
                            forma.yFin - forma.yInicio
                    );


                    /*
                     * Queremos que sea un cuadrado, por lo que
                     * utilizamos el mayor valor para ambas
                     * dimensiones.
                     */

                    int tamaño = Math.max(
                            ancho,
                            alto
                    );


                    // -----------------------------------------
                    // RELLENO
                    // -----------------------------------------

                    if (forma.relleno != null) {

                        g2.setColor(forma.relleno);

                        g2.fillRect(
                                x,
                                y,
                                tamaño,
                                tamaño
                        );


                        g2.setColor(forma.color);
                    }


                    // -----------------------------------------
                    // BORDE
                    // -----------------------------------------

                    g2.drawRect(
                            x,
                            y,
                            tamaño,
                            tamaño
                    );
                }
            }
        }


        // =====================================================
        // COMPROBAR SI EL RATÓN ESTÁ DENTRO DE UNA FORMA
        // =====================================================

        /*
         * Este método lo utilizamos para:
         *
         * - pipeta
         * - cubo rellenador
         *
         * Devuelve:
         *
         * true  -> el punto está dentro
         * false -> el punto está fuera
         */

        public boolean estaDentro(
                Forma forma,
                int x,
                int y) {


            /*
             * Calculamos la esquina superior izquierda.
             */

            int izquierda = Math.min(
                    forma.xInicio,
                    forma.xFin
            );

            int arriba = Math.min(
                    forma.yInicio,
                    forma.yFin
            );


            /*
             * Calculamos dimensiones.
             */

            int ancho = Math.abs(
                    forma.xFin - forma.xInicio
            );

            int alto = Math.abs(
                    forma.yFin - forma.yInicio
            );


            // =================================================
            // COMPROBAR CUADRADO
            // =================================================

            if (forma.tipo.equals("cuadrado")) {

                int tamaño = Math.max(
                        ancho,
                        alto
                );


                /*
                 * Comprobamos si X e Y están dentro de los
                 * límites del cuadrado.
                 */

                return x >= izquierda
                        && x <= izquierda + tamaño
                        && y >= arriba
                        && y <= arriba + tamaño;
            }


            // =================================================
            // COMPROBAR CÍRCULO
            // =================================================

            if (forma.tipo.equals("circulo")) {


                /*
                 * Si no tiene tamaño, no podemos comprobarlo.
                 */

                if (ancho == 0 || alto == 0) {

                    return false;
                }


                /*
                 * Calculamos el centro del círculo.
                 */

                double centroX =
                        izquierda + ancho / 2.0;

                double centroY =
                        arriba + alto / 2.0;


                /*
                 * Calculamos los radios.
                 */

                double radioX =
                        ancho / 2.0;

                double radioY =
                        alto / 2.0;


                /*
                 * Comprobamos matemáticamente si el punto
                 * está dentro del círculo/óvalo.
                 */

                double resultado =
                        Math.pow(
                                (x - centroX) / radioX,
                                2
                        )
                        +
                        Math.pow(
                                (y - centroY) / radioY,
                                2
                        );


                return resultado <= 1;
            }


            /*
             * Si no es una figura que podamos comprobar,
             * devolvemos false.
             */

            return false;
        }
    }


    // =========================================================
    // CLASE FORMA
    // =========================================================

    /*
     * Forma representa una figura almacenada en el Paint.
     *
     * No tenemos constructor.
     *
     * Creamos:
     *
     * Forma forma = new Forma();
     *
     * y después rellenamos sus datos.
     */

    static class Forma {


        // =====================================================
        // TIPO
        // =====================================================

        /*
         * Puede ser:
         *
         * "linea"
         * "circulo"
         * "cuadrado"
         */

        String tipo;


        // =====================================================
        // COORDENADAS
        // =====================================================

        int xInicio;

        int yInicio;

        int xFin;

        int yFin;


        // =====================================================
        // COLOR DEL BORDE
        // =====================================================

        Color color;


        // =====================================================
        // COLOR DE RELLENO
        // =====================================================

        /*
         * Si es null:
         *
         * no hay relleno.
         *
         * Si contiene un Color:
         *
         * la figura tiene ese color de relleno.
         */

        Color relleno;


        // =====================================================
        // GROSOR
        // =====================================================

        int grosor;
    }
}