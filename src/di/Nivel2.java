package di;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Nivel2 {
	public static void main (String[]args) {
	JFrame ventana = new JFrame("Nivel2");
	ventana.setSize(400,400);
	ventana.setLocation(700,300);
	ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	ventana.setLayout(new FlowLayout());
	
	JLabel txtPerro = new JLabel("Perro", 10);
	JLabel txtGato = new JLabel("Gato", 10);
	JLabel txtConejo= new JLabel("Conejo", 10);
	JCheckBox chkPerro = new JCheckBox();
	JCheckBox chkGato = new JCheckBox();
	JCheckBox chkConejo = new JCheckBox();
	}
}
