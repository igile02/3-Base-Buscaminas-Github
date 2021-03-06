package buscaminas;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Ventana principal del Buscaminas, en esta clase nos encargamos de todo lo
 * relaccionado con la parte visual de nuestro juego(mostrar mensajes,
 * actualizar puntuacion, refrescar pantalla, etc), creamos todos sus
 * componentes, los inicializamos {@link #inicializar()}
 * {@code ventana.setVisible(true);
 * 														 		    inicializarComponentes();
 *																    inicializarListeners();}
 * y les damos formato, esta clase se encarga también de dar las acciones
 * respectivas a cada uno de los botones mediante los listeners.
 * 
 * @author Iván Gil Esteban
 * @see ControlJuego
 * @version 1.0
 * @since 1.0
 */
public class VentanaPrincipal {

	// La ventana principal, en este caso, guarda todos los componentes:
	private JFrame ventana;
	private JPanel panelEmpezar;
	private JPanel panelPuntuacion;
	private JPanel panelJuego;
	private JCronometro cronometro;

	// Todos los botones se meten en un panel independiente.
	// Hacemos esto para que podamos cambiar después los componentes por otros
	private JPanel[][] panelesJuego;
	private JButton[][] botonesJuego;

	// Correspondencia de colores para las minas:
	private Color correspondenciaColores[] = { Color.BLACK, Color.CYAN, Color.GREEN, Color.ORANGE, Color.RED, Color.RED,
			Color.RED, Color.RED, Color.RED, Color.RED };

	private JButton botonEmpezar;
	private JTextField pantallaPuntuacion;

	// LA VENTANA GUARDA UN CONTROL DE JUEGO:
	private ControlJuego juego;

	// Constructor, marca el tamaño y el cierre del frame
	public VentanaPrincipal() {
		ventana = new JFrame();
		ventana.setBounds(100, 100, 700, 500);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		juego = new ControlJuego();
	}

	// Inicializa todos los componentes del frame
	public void inicializarComponentes() {

		// Definimos el layout:
		ventana.setLayout(new GridBagLayout());

		// Inicializamos componentes
		cronometro = new JCronometro();
		panelEmpezar = new JPanel();
		panelEmpezar.setLayout(new GridLayout(1, 1));
		panelPuntuacion = new JPanel();
		panelPuntuacion.setLayout(new GridLayout(1, 1));
		panelJuego = new JPanel();
		panelJuego.setLayout(new GridLayout(10, 10));

		botonEmpezar = new JButton("Go!");
		pantallaPuntuacion = new JTextField("0");
		pantallaPuntuacion.setEditable(false);
		pantallaPuntuacion.setHorizontalAlignment(SwingConstants.CENTER);

		// Bordes y colores:
		cronometro.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		panelEmpezar.setBorder(BorderFactory.createTitledBorder("Empezar"));
		panelPuntuacion.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		panelJuego.setBorder(BorderFactory.createTitledBorder("Juego"));

		// Colocamos los componentes:
		// Cronometro
		GridBagConstraints settings = new GridBagConstraints();
		settings.gridx = 0;
		settings.gridy = 0;
		settings.weightx = 1;
		settings.weighty = 1;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(cronometro, settings);
		// PanelEmpezar
		settings = new GridBagConstraints();
		settings.gridx = 1;
		settings.gridy = 0;
		settings.weightx = 1;
		settings.weighty = 1;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelEmpezar, settings);
		// PanelPuntuación
		settings = new GridBagConstraints();
		settings.gridx = 2;
		settings.gridy = 0;
		settings.weightx = 1;
		settings.weighty = 1;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelPuntuacion, settings);
		// PanelJuego
		settings = new GridBagConstraints();
		settings.gridx = 0;
		settings.gridy = 1;
		settings.weightx = 1;
		settings.weighty = 10;
		settings.gridwidth = 3;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelJuego, settings);

		// Paneles
		panelesJuego = new JPanel[10][10];
		for (int i = 0; i < panelesJuego.length; i++) {
			for (int j = 0; j < panelesJuego[i].length; j++) {
				panelesJuego[i][j] = new JPanel();
				panelesJuego[i][j].setLayout(new GridLayout(1, 1));
				panelJuego.add(panelesJuego[i][j]);
			}
		}

		// Botones
		botonesJuego = new JButton[10][10];
		for (int i = 0; i < botonesJuego.length; i++) {
			for (int j = 0; j < botonesJuego[i].length; j++) {
				botonesJuego[i][j] = new JButton("-");
				panelesJuego[i][j].add(botonesJuego[i][j]);
			}
		}

		// BotónEmpezar:
		panelEmpezar.add(botonEmpezar);
		panelPuntuacion.add(pantallaPuntuacion);

	}

	/**
	 * Método que inicializa todos los lísteners que necesita inicialmente el
	 * programa
	 */
	public void inicializarListeners() {

		botonEmpezar.addActionListener((e) -> {
			reiniciarPartida();
			cronometro.resetear();
			cronometro.comenzar();
			for (int i = 0; i < botonesJuego.length; i++) {
				for (int j = 0; j < botonesJuego.length; j++) {
					botonesJuego[i][j].addActionListener(new ActionBoton(this, i, j));
				}
			}
		});

	}

	/**
	 * Pinta en la pantalla el número de minas que hay alrededor de la celda Saca el
	 * botón que haya en la celda determinada y añade un JLabel centrado y no
	 * editable con el número de minas alrededor. Se pinta el color del texto según
	 * la siguiente correspondecia (consultar la variable correspondeciaColor): - 0
	 * : negro - 1 : cyan - 2 : verde - 3 : naranja - 4 ó más : rojo
	 * 
	 * @param i: posición vertical de la celda.
	 * @param j: posición horizontal de la celda.
	 */
	public void mostrarNumMinasAlrededor(int i, int j) {
		int num = juego.getMinasAlrededor(i, j);
		JLabel label = new JLabel(Integer.toString(num));
		panelesJuego[i][j].removeAll();
		panelesJuego[i][j].add(label);
		label.setForeground(correspondenciaColores[num]);
		label.setHorizontalAlignment(JLabel.CENTER);
		refrescarPantalla();
	}

	/**
	 * Muestra una ventana que indica el fin del juego
	 * 
	 * @param porExplosion : Un booleano que indica si es final del juego porque ha
	 *                     explotado una mina (true) o bien porque hemos desactivado
	 *                     todas (false)
	 * @param i:           Posicion vertical del boton que es una mina para colocar
	 *                     el icono de mina.
	 * @param j:           Posicion del boton que es una mina para colocar el icono
	 *                     de mina.
	 * @post : Todos los botones se desactivan excepto el de volver a iniciar el
	 *       juego.
	 */
	public void mostrarFinJuego(boolean porExplosion, int i, int j) {
		if (juego.esFinJuego()) {
			cronometro.parar();
			desactivarBotones();

			ImageIcon icono = new ImageIcon(getClass().getResource("imagenes/carita.png"));
			botonEmpezar.setIcon(icono);

			mensajeFin("Enhorabuena ha ganado \n ¿Desea jugar otra partida?", "Juego Ganado");
		}

		if (!porExplosion) {
			cronometro.parar();
			desactivarBotones();

			ImageIcon icono = new ImageIcon(getClass().getResource("imagenes/mina.png"));
			botonesJuego[i][j].setText("");
			botonesJuego[i][j].setIcon(icono);

			mensajeFin("Ha explotado una mina \n ¿Desea jugar otra partida?", "Juego Perdido");
		}
	}

	/**
	 * Método que actualiza la puntuación de nuestro panel de puntuación
	 */
	public void actualizarPuntuacion() {
		pantallaPuntuacion.setText(String.valueOf(juego.getPuntuacion()));
	}

	/**
	 * Método para refrescar la pantalla
	 */
	public void refrescarPantalla() {
		ventana.revalidate();
		ventana.repaint();
	}

	/**
	 * Método que devuelve el control del juego de una ventana
	 * 
	 * @return un ControlJuego con el control del juego de la ventana
	 */
	public ControlJuego getJuego() {
		return juego;
	}

	/**
	 * Método para reiniciar todo el tablero, tanto visualmente como
	 * internamente(posición de las minas y numero de minas adjuntas)
	 */
	public void reiniciarPartida() {
		juego.inicializarPartida();
		actualizarPuntuacion();
		ventana.getContentPane().removeAll();
		inicializar();
		refrescarPantalla();
	}

	/**
	 * Método para desactivar los botones del tablero de juego.
	 */
	public void desactivarBotones() {
		for (int i = 0; i < botonesJuego.length; i++) {
			for (int j = 0; j < botonesJuego.length; j++) {
				botonesJuego[i][j].setEnabled(false);
			}
		}
	}

	/**
	 * Método que muestra el mensaje correspondiente para cada final y reinicia la
	 * partida si la opcion selecionada es "YES"
	 * 
	 * @param mensaje: El mensaje que muestra el JOptionPane, que sera uno en caso
	 *                 de ganar u otro en caso de perder.
	 * @param titulo:  Titulo de la ventana del JOptionPane que tambien cambia en
	 *                 caso de ganar o de perder.
	 */
	public void mensajeFin(String mensaje, String titulo) {
		int opcion = JOptionPane.showConfirmDialog(ventana, mensaje, titulo, JOptionPane.YES_NO_OPTION);
		if (opcion == JOptionPane.YES_OPTION) {
			reiniciarPartida();
		}
	}

	/**
	 * Método para inicializar el programa
	 */
	public void inicializar() {
		// IMPORTANTE, PRIMERO HACEMOS LA VENTANA VISIBLE Y LUEGO INICIALIZAMOS LOS
		// COMPONENTES.
		ventana.setVisible(true);
		inicializarComponentes();
		inicializarListeners();
	}

}
