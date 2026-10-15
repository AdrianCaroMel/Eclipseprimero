package di;
import java.awt.*;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;
public class Ejercicio7 {

	public static void main(String[] args) {
		JFrame ventana = new JFrame("Selector de fuentes");
		ventana.setSize(400, 400);
		ventana.setLocation(100, 200);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setLayout(new BorderLayout());
		JTextArea areaTexto = new JTextArea("Cagadon Mesopotamico");
		GraphicsEnvironment entorno = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] nombresFuentes = entorno.getAvailableFontFamilyNames();
		JComboBox<String> cajaFuentes = new JComboBox<>(nombresFuentes);
		
		cajaFuentes.addActionListener(e -> {
			String fuenteElegida = (String) cajaFuentes.getSelectedItem();//esta es la unica accion importante y interesante el resto simplemente es reciclado de años pasados bueno junto a el tener que cargar interfaces graficas y la familia de fuentes
			areaTexto.setFont(new Font(fuenteElegida, Font.PLAIN, 14));
		});
		
		ventana.add(cajaFuentes, BorderLayout.NORTH);
		ventana.add(areaTexto, BorderLayout.CENTER);
		ventana.setVisible(true);
	}

}
