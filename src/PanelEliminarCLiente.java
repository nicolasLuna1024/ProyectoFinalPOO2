import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PanelEliminarCLiente extends JFrame{
    private JTextField textCedulaEliminar;
    private JButton eliminarButton;
    private JButton menuButton;
    private JPanel PanelEliminarCliente2;

    public PanelEliminarCLiente() {
        super("Eliminar Cliente");
        setContentPane(PanelEliminarCliente2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cedulaEliminar = textCedulaEliminar.getText().trim();
                try {
                    eliminarCliente(cedulaEliminar);
                    JOptionPane.showMessageDialog(null, "Registro del Cliente Eliminado Exitosamente");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al eliminar cliente: " + ex.getMessage());
                }
            }
        });
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuAdministrador menuAdministrador = new MenuAdministrador();
                menuAdministrador.iniciarAdmin();
                dispose();
            }
        });
    }

    // Metodo Conexion base
    public Connection conexionBase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/GestionDeCanchas";
        String user = "root";
        String password = "123456";
        return DriverManager.getConnection(url, user, password);
    }

    // Metodo para Iniciar la Pantalla de Eliminar Cliente
    public void iniciarEliminarCliente() {
        setVisible(true);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
    }

    // Eliminar Cliente
    public void eliminarCliente(String cedula) throws SQLException {
        Connection conecta = conexionBase();
        String query = "DELETE FROM Jugador WHERE cedula = ?";
        PreparedStatement pstmt = conecta.prepareStatement(query);
        pstmt.setString(1, cedula);
        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected == 0) {
            JOptionPane.showMessageDialog(null, "Cedula No Encontrada");
        }

        pstmt.close();
        conecta.close();
    }
}
