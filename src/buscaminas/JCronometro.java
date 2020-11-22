package buscaminas;
 
import java.awt.Color;
import java.awt.Font;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Clase que extiende de JPanel e implementa Runnable, contiene un JLabel en el que vamos a ir mostrando
 * el resultado de la operación que va realizando el hilo que creamos para calcular el tiempo que va
 * transcurriendo desde que damos al boton de empezar, como extiende de JPanel en nuestra clase
 * @see VentanaPrincipal solo tendremos que añadirlo como si se tratase de un panel.
 * 
 * @author Iván Gil Esteban
 * 
 * @version 1.0 
 * @since 1.0 
 */
public class JCronometro extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    //Atributos de nuestro componente
    private JLabel contador;
    private double tiempoTranscurrido;
    private double tiempoOriginal;
    private Thread hilo = null;
    private boolean activo;

    //Constructor del componente
    public JCronometro() {
        super();
        this.setLayout(new GridLayout());
        this.setBackground(Color.CYAN);
        contador = new JLabel("00:00");
        contador.setHorizontalAlignment(JLabel.CENTER);
        contador.setFont(new Font("Arial", Font.BOLD, 14));
        this.add(contador);
    }

    /**
     * Método que comprueba si el hilo es nulo y si es nulo lo inicializa y lo inicia.
     * Pone la variable activo a true para que se ejecute el hilo
     */
    public void comenzar() {
        if (hilo == null) {
            activo = true;
            hilo = new Thread(this);
            hilo.start();
        }
    }

    /**
     * Método que cambia el valor de la variable activo a false para que el hilo deje de ejecutarse
     */
    public void parar() {
        if (hilo != null) {
            activo = false;
        }
    }

    /**
     * Método que resetea el tiempo y el contador a 0
     */
    public void resetear() {
        tiempoOriginal = System.nanoTime();
        contador.setText("00:00");
    }

    /**
     * Método de la clase Thread que arranca el hilo
     * En este metodo asignamos el tiempoOriginal a 0 
     * Mientras la variable activo sea true, 
     * calculamos el tiempo transcurrido y actualizamos el contador.
     * Finalmente ponemos el hilo a null.
     */
    @Override
    public void run() {
        try {
            tiempoOriginal = System.nanoTime();
            while (activo) {
                calcularTiempoTranscurrido();
                actualizarPantalla();
            } 
            Thread.sleep(50);
        } catch (InterruptedException e) {

        }
        hilo = null;
    }

    /**
     * Calculamos la diferencia entre el tiempo de inicio y
     * el tiempo actual que nos da los nanosegundos que han pasado
     */
    public void calcularTiempoTranscurrido() {
        tiempoTranscurrido = ((System.nanoTime() - tiempoOriginal));
    }

    /**
     * Convertimos los nanosegundos en segundos y minutos para poder actualizar nuestro contador luego
     * con los segundos y minutos que han transcurrido.
     */
    public void actualizarPantalla() {
        double seconds = (tiempoTranscurrido / 1e+9);
        long s = (long) (seconds % 60);
        long m = (long) ((seconds / 60) % 60);

        contador.setText(String.format("%02d:%02d", m, s));
        this.revalidate();
        this.repaint();
    }

}
