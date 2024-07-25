import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Login extends JFrame{
    private JComboBox BoxTipoUsuario;
    private JPasswordField passwordField1;
    private JLabel IconoLogin;
    private JLabel TipoUsuario;
    private JTextField textUsuario;
    private JButton ingresarButton;
    private JPanel PanelLogin;

    public Login(){
        super("Login");
        setContentPane(PanelLogin);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);

        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }




    //Metodo para Iniciar la Pantalla de Login
    public void iniciarLogin(){
        setVisible(true);
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


}
