import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuAdministrador extends JFrame{
    private JButton gestionarClientesButton;
    private JButton gestionarCanchasButton;
    private JButton gestionarHorariosButton;
    private JButton salirButton;
    private JPanel MenuAdmin;

    public MenuAdministrador(){
        super("Menu Administrador");
        setContentPane(MenuAdmin);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setSize(500,500);
        gestionarClientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuGestionClien menuGestionClien = new MenuGestionClien();
                menuGestionClien.iniciarGestionClientes();
                dispose();

            }
        });
        gestionarCanchasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuGestionCanchas menuGestionCanchas = new MenuGestionCanchas();
                menuGestionCanchas.iniciarMenuGestionCanchas();
                dispose();
            }
        });
    }



    //Metodo para Iniciar la Pantalla Administrador
    public void iniciarAdmin(){
        setVisible(true);
        setSize(500,500);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
