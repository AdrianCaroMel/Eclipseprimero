package di;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Ventana de login: JDialog modal y sin barra de título
public class LoginDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public LoginDialog() {

		// true = modal: bloquea el programa hasta que se cierra con dispose()
		super((JFrame) null, "Inicio de sesión", true);

		setUndecorated(true);
		setLayout(new BorderLayout(10, 10));
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

		// ---- Título ----
		JLabel titulo = new JLabel("Iniciar sesión", SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 18));
		titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 0, 10));
		add(titulo, BorderLayout.NORTH);

		// ---- Campos: usuario (JTextField) y contraseña (JPasswordField) ----
		JTextField txtUsuario = new JTextField(12);
		JPasswordField txtClave = new JPasswordField(12);
		txtUsuario.setToolTipText("Escribe tu nombre de usuario");
		txtClave.setToolTipText("Escribe tu contraseña");

		JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 10));
		panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		panelCampos.add(new JLabel("Usuario:"));
		panelCampos.add(txtUsuario);
		panelCampos.add(new JLabel("Contraseña:"));
		panelCampos.add(txtClave);
		add(panelCampos, BorderLayout.CENTER);

		// ---- Botones con tecla mnemónica (Alt + letra) ----
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.setMnemonic(KeyEvent.VK_A);
		btnAceptar.setToolTipText("Entrar en la aplicación (Enter)");

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setMnemonic(KeyEvent.VK_C);
		btnCancelar.setToolTipText("Cerrar la aplicación");

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		panelBotones.add(btnAceptar);
		panelBotones.add(btnCancelar);
		add(panelBotones, BorderLayout.SOUTH);

		// Botón por defecto: se pulsa con Enter
		getRootPane().setDefaultButton(btnAceptar);

		// ---- Aceptar: correcto = se cierra; incorrecto = error y se mantiene ----
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String usuario = txtUsuario.getText();
				String clave = new String(txtClave.getPassword());

				if (usuario.equals("admin") && clave.equals("1234")) {
					dispose();
				} else {
					JOptionPane.showMessageDialog(LoginDialog.this, "Usuario o contraseña incorrectos",
							"Error de acceso", JOptionPane.ERROR_MESSAGE);
					txtClave.setText("");
				}
			}
		});

		// ---- Cancelar: termina la aplicación ----
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		pack();
		setLocationRelativeTo(null);
	}
}