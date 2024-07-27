import javax.swing.*;

public class MenuGestionCanchas extends JFrame {
    private JButton agregarCanchaButton;
    private JButton actualizarCanchaButton;
    private JButton buscarCanchaButton;
    private JButton eliminarCanchaButton;
    private JPanel menuGestCanchas;

    public MenuGestionCanchas(){
        super("Gestion de Canchas");
        setContentPane(menuGestCanchas);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

    }
    public void iniciarMenuGestionCanchas(){
        setVisible(true);
    }
}
