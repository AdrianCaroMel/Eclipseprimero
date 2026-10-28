package di;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;

// Ventana principal de la aplicación
public class MainApp extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainApp() {

		// Configuración básica de la ventana
		super("Gestión de notas interactivas");
		setSize(750, 500);
		setLocation(200, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(10, 10));

		// ---- 1. LISTA DE NOTAS: aquí se guardan y se ven las notas ----
		// El modelo guarda los datos y el JList los muestra
		DefaultListModel<String> modeloNotas = new DefaultListModel<String>();
		JList<String> listaNotas = new JList<String>(modeloNotas);
		listaNotas.setToolTipText("Lista de notas guardadas. Selecciona una para eliminarla");

		// El JScrollPane añade la barra de desplazamiento
		JScrollPane scrollLista = new JScrollPane(listaNotas);
		scrollLista.setBorder(BorderFactory.createTitledBorder("Mis notas"));

		// ---- 2. CAMPO DE TEXTO: aquí se escribe la nota nueva ----
		JTextArea areaTexto = new JTextArea();
		areaTexto.setLineWrap(true); // salta de línea al llegar al borde
		areaTexto.setWrapStyleWord(true); // sin cortar las palabras
		areaTexto.setToolTipText("Escribe aquí el texto de la nota (Ctrl+Z deshacer, Ctrl+Y rehacer)");

		JScrollPane scrollTexto = new JScrollPane(areaTexto);
		scrollTexto.setBorder(BorderFactory.createTitledBorder("Nueva nota"));

		// Panel del centro: lista a la izquierda y texto a la derecha
		JPanel panelCentro = new JPanel(new GridLayout(1, 2, 10, 10));
		panelCentro.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		panelCentro.add(scrollLista);
		panelCentro.add(scrollTexto);
		add(panelCentro, BorderLayout.CENTER);

		// ---- 3. DESHACER / REHACER: historial de cambios del JTextArea ----
		// Paso 1: el UndoManager es quien guarda el historial
		UndoManager undoManager = new UndoManager();

		// Paso 2: cada vez que cambia el texto, se apunta el cambio en el historial
		areaTexto.getDocument().addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent e) {
				undoManager.addEdit(e.getEdit());
			}
		});

		// Paso 3: las acciones que deshacen y rehacen (canUndo/canRedo evitan errores)
		AbstractAction accionDeshacer = new AbstractAction("Deshacer") {
			public void actionPerformed(ActionEvent e) {
				if (undoManager.canUndo()) {
					undoManager.undo();
				}
			}
		};
		AbstractAction accionRehacer = new AbstractAction("Rehacer") {
			public void actionPerformed(ActionEvent e) {
				if (undoManager.canRedo()) {
					undoManager.redo();
				}
			}
		};

		// Paso 4: se asocian las teclas Ctrl+Z y Ctrl+Y a esas acciones en el JTextArea
		areaTexto.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "deshacer");
		areaTexto.getActionMap().put("deshacer", accionDeshacer);
		areaTexto.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "rehacer");
		areaTexto.getActionMap().put("rehacer", accionRehacer);

		// ---- 4. ACCIONES AÑADIR / ELIMINAR: las usan a la vez el botón y el menú ----
		// Añadir: pasa el texto del JTextArea a la lista y limpia el campo
		AbstractAction accionAnadir = new AbstractAction("Añadir nota") {
			public void actionPerformed(ActionEvent e) {
				String texto = areaTexto.getText();

				if (texto.length() > 0) {
					modeloNotas.addElement(texto);
					areaTexto.setText("");
				}
			}
		};

		// Eliminar: borra la nota seleccionada (-1 significa que no hay ninguna)
		AbstractAction accionEliminar = new AbstractAction("Eliminar nota") {
			public void actionPerformed(ActionEvent e) {
				int indice = listaNotas.getSelectedIndex();

				if (indice != -1) {
					modeloNotas.remove(indice);
				}
			}
		};

		// ---- 5. BOTONES: parte de abajo, con tecla Alt y tooltip ----
		JButton btnAnadir = new JButton(accionAnadir);
		btnAnadir.setMnemonic(KeyEvent.VK_N); // Alt + N
		btnAnadir.setToolTipText("Añade el texto escrito a la lista de notas (Alt+N)");

		JButton btnEliminar = new JButton(accionEliminar);
		btnEliminar.setMnemonic(KeyEvent.VK_L); // Alt + L
		btnEliminar.setToolTipText("Elimina la nota seleccionada de la lista (Alt+L)");

		JPanel panelBotones = new JPanel(new FlowLayout());
		panelBotones.add(btnAnadir);
		panelBotones.add(btnEliminar);
		add(panelBotones, BorderLayout.SOUTH);

		// ---- 6. SELECTOR DE TEMA: parte de arriba, cambia el Look & Feel ----
		JComboBox<String> comboTema = new JComboBox<String>(new String[] { "Nimbus", "Metal", "Windows", "Motif" });
		comboTema.setToolTipText("Cambia el aspecto (tema) de toda la aplicación");

		// Cuando el usuario elige un tema, se aplica su Look & Feel
		comboTema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tema = (String) comboTema.getSelectedItem();

				try {
					if (tema.equals("Nimbus")) {
						UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					} else if (tema.equals("Metal")) {
						UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
					} else if (tema.equals("Windows")) {
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					} else {
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
					}
					// Sin esta línea el tema nuevo no se ve en la ventana ya abierta
					SwingUtilities.updateComponentTreeUI(MainApp.this);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		JPanel panelTema = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelTema.add(new JLabel("Tema:"));
		panelTema.add(comboTema);
		add(panelTema, BorderLayout.NORTH);

		// ---- 7. MENÚS: Alt + letra abre el menú, Ctrl + letra ejecuta la opción ----
		JMenuBar barraMenu = new JMenuBar();

		// Menú Archivo (Alt + A)
		JMenu menuArchivo = new JMenu("Archivo");
		menuArchivo.setMnemonic(KeyEvent.VK_A);

		JMenuItem itemAnadir = new JMenuItem(accionAnadir);
		itemAnadir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)); // Ctrl + N
		itemAnadir.setToolTipText("Añade la nota escrita a la lista (Ctrl+N)");

		JMenuItem itemEliminar = new JMenuItem(accionEliminar);
		itemEliminar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK)); // Ctrl + E
		itemEliminar.setToolTipText("Elimina la nota seleccionada (Ctrl+E)");

		menuArchivo.add(itemAnadir);
		menuArchivo.add(itemEliminar);

		// Menú Editar (Alt + E)
		JMenu menuEditar = new JMenu("Editar");
		menuEditar.setMnemonic(KeyEvent.VK_E);

		JMenuItem itemDeshacer = new JMenuItem(accionDeshacer);
		itemDeshacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK)); // Ctrl + Z
		itemDeshacer.setToolTipText("Deshace el último cambio del texto (Ctrl+Z)");

		JMenuItem itemRehacer = new JMenuItem(accionRehacer);
		itemRehacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK)); // Ctrl + Y
		itemRehacer.setToolTipText("Rehace el cambio deshecho (Ctrl+Y)");

		menuEditar.add(itemDeshacer);
		menuEditar.add(itemRehacer);

		// Se meten los dos menús en la barra y la barra en la ventana
		barraMenu.add(menuArchivo);
		barraMenu.add(menuEditar);
		setJMenuBar(barraMenu);
	}
}