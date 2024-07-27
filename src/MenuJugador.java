import javax.swing.*;
import java.awt.*;

public class MenuJugador extends JFrame {
    private JButton reservarCanchasButton;
    private JButton detallesDeCanchasButton;
    private JButton salirButton;
    private JPanel MenuJugador;

    public MenuJugador(){
        super("Menu Jugador");
        setContentPane(MenuJugador);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla



    }

    //Metodo para Iniciar la Pantalla Menu Jugador
    public void iniciarJugador(){
        setVisible(true);
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
    }
}
