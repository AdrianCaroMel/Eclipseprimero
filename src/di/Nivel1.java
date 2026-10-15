package di;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Nivel1 {

    public static void main(String[] args) {

        JFrame ventana = new JFrame("Nivel1");
        ventana.setSize(400, 400);
        ventana.setLocation(700, 300);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(new BorderLayout());

        JTextField nombre = new JTextField(15);
        JLabel resultado = new JLabel("Resultado");
        JButton confirmar = new JButton("Confirmar");

        JPanel primero = new JPanel();
        primero.setLayout(new BorderLayout());

        confirmar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                String respuesta = nombre.getText();
                resultado.setText("Hola " + respuesta);

            }
        });

        ventana.add(nombre, BorderLayout.NORTH);

        primero.add(resultado, BorderLayout.CENTER);
        primero.add(confirmar, BorderLayout.NORTH);

        ventana.add(primero, BorderLayout.CENTER);

        ventana.setVisible(true);
    }
}