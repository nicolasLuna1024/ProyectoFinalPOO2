import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MenuGestionClien extends JFrame{
    private JPanel PanelGestClient;
    private JButton agregarClienteButton;
    private JButton actualizarClienteButton;
    private JButton verClienteButton;
    private JButton eliminarClienteButton;

    public MenuGestionClien(){
        super("Gestionar Clientes");
        setContentPane(PanelGestClient);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        agregarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelAgregarCliente panelAgregarCliente = new PanelAgregarCliente();
                panelAgregarCliente.iniciarAgregarCliente();
                dispose();
            }
        });
        actualizarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelActualizarCliente panelActualizarCliente = new PanelActualizarCliente();
                panelActualizarCliente.iniciarActualizarCliente();
                dispose();
            }
        });
    }



    //Menu Iniciar Pantalla gestion Clientes
    public void iniciarGestionClientes(){
        setVisible(true);
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
