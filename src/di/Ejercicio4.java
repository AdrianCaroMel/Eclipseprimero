package di;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Ejercicio4 {
	public static void main(String[] args) {
		JFrame ventana = new JFrame("Login");
		ventana.setSize(400, 250);
		ventana.setLocation(100, 200);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setLayout(new BorderLayout());

		String usuarioValido = "Tarde";
		String passwordValida = "CsggFp2627";

		JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));
		panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));

		JLabel lblUsuario = new JLabel("Usuario:");
		JTextField txtUsuario = new JTextField();

		JLabel lblPassword = new JLabel("Contraseña:");
		JPasswordField txtPassword = new JPasswordField();

		panelFormulario.add(lblUsuario);
		panelFormulario.add(txtUsuario);
		panelFormulario.add(lblPassword);
		panelFormulario.add(txtPassword);

		JPanel panelInferior = new JPanel(new FlowLayout());
		JButton verificar = new JButton("Comprobar");
		JLabel resultado = new JLabel(" ");

		panelInferior.add(verificar);
		panelInferior.add(resultado);

		verificar.addActionListener(e -> {
			String usuario = txtUsuario.getText();
			String password = new String(txtPassword.getPassword());

			boolean usuarioOk = usuario.equals(usuarioValido);
			boolean passwordOk = password.equals(passwordValida);

			if (!usuarioOk && !passwordOk) {
				resultado.setText("El usuario y la password son incorrectos.");
			} else if (!usuarioOk) {
				resultado.setText("El usuario introducido no es correcto");
			} else if (!passwordOk) {
				resultado.setText("La password introducida no es correcta.");
			} else {
				resultado.setText("Acceso correcto");
			}
		});

		ventana.add(panelFormulario, BorderLayout.CENTER);
		ventana.add(panelInferior, BorderLayout.SOUTH);

		ventana.setVisible(true);
	}
}