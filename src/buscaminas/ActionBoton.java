package buscaminas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 

/**
 * Clase que implementa el listener de los botones del Buscaminas. De alguna
 * manera tendrá que poder acceder a la ventana principal. Se puede lograr
 * pasando en el constructor la referencia a la ventana. Recuerda que desde la
 * ventana, se puede acceder a la variable de tipo ControlJuego
 * 
 * @author Iván Gil Esteban
 *
 * @version 1.0 
 * @since 1.0 
 */
public class ActionBoton implements ActionListener {

	//Atributos de la clase
	private VentanaPrincipal ventana;
	private int i;
	private int j;

	/**
	 * Constructor que recibe un objeto @see VentanaPrincipal y la posicion horizontal y vertical del boton.
	 */
	public ActionBoton(VentanaPrincipal ventana, int i, int j) {
		this.ventana = ventana;
		this.i = i;
		this.j = j;
	}

	/**
	 * Acción que ocurrirá cuando pulsamos uno de los botones.
	 * Primero comprobamos si la casilla es una mina o no,
	 * si la casilla no es mina mostramos las minas de alrededor 
	 * y actualizamos la puntuacion, si es una mina mostramos el fin del juego.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean fin;
		if (fin = ventana.getJuego().abrirCasilla(i, j)) {
			ventana.mostrarNumMinasAlrededor(i, j);
			ventana.actualizarPuntuacion();
		}
		ventana.mostrarFinJuego(fin,i,j);
	}
}
