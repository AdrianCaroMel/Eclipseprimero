package di;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class Ejercicio3 {
	public static void main(String[] args) {
		JFrame basica = new JFrame("Basica");
		basica.setSize(400, 400);
		basica.setLocation(100, 200);
		basica.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		basica.setLayout(new FlowLayout());

		JTextField txtToyota = new JTextField("Toyota", 10);
		JTextField txtHonda = new JTextField("Honda", 10);
		JTextField txtTesla = new JTextField("Tesla", 10);
		txtToyota.setEditable(false);
		txtHonda.setEditable(false);
		txtTesla.setEditable(false);

		JCheckBox chkToyota = new JCheckBox();
		JCheckBox chkHonda = new JCheckBox();
		JCheckBox chkTesla = new JCheckBox();

		JPanel panelMarcas = new JPanel(new GridLayout(3, 2, 10, 10));
		panelMarcas.add(txtToyota);
		panelMarcas.add(chkToyota);
		panelMarcas.add(txtHonda);
		panelMarcas.add(chkHonda);
		panelMarcas.add(txtTesla);
		panelMarcas.add(chkTesla);

		JButton comprobar = new JButton("Comprobar Seleccion");
		JLabel resultado = new JLabel("");

		comprobar.addActionListener(e -> {
			boolean toyota = chkToyota.isSelected();
			boolean honda = chkHonda.isSelected();
			boolean tesla = chkTesla.isSelected();

			if (toyota && honda && tesla) {
				resultado.setText("Has seleccionado Toyota, Honda y Tesla");
			} else if (toyota && honda) {
				resultado.setText("Has seleccionado Toyota y Honda");
			} else if (honda && tesla) {
				resultado.setText("Has seleccionado Honda y Tesla");
			} else if (toyota && tesla) {
				resultado.setText("Has seleccionado Toyota y Tesla");
			} else if (toyota) {
				resultado.setText("Has seleccionado Toyota");
			} else if (honda) {
				resultado.setText("Has seleccionado Honda");
			} else if (tesla) {
				resultado.setText("Has seleccionado Tesla");
			} else {
				resultado.setText("No has seleccionado ninguna marca");
			}
		});

		basica.add(panelMarcas);
		basica.add(comprobar);
		basica.add(resultado);

		basica.setVisible(true);
	}
}