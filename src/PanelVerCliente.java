import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PanelVerCliente extends JFrame {
    private JTextField textField1;
    private JTextArea textAreaBuscar;
    private JPanel PanelBuscarCliente;
    private JButton buscarButton;
    private JButton menuButton;


    public PanelVerCliente(){
        super("Buscar Cliente");
        setContentPane(PanelBuscarCliente);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setSize(500, 500);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VerRegistro();
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
    // Metodo para Buscar Cliente
    public void VerRegistro(){
        try{
            String cedula = textField1.getText().trim(); // Obtener la cédula ingresada


            Connection conn = conexionBase();
            String query = "SELECT * FROM Jugador WHERE cedula = ?";
            Statement stmt = conn.createStatement();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cedula);
            textAreaBuscar.setText(" ");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                String Nombre = rs.getString("nombreCompleto");
                String Email = rs.getString("email");
                Integer Telefono = rs.getInt("telefono");
                String FechaNacimiento = rs.getString("fechaNacimiento");
                Double Cedula = rs.getDouble("cedula");



                textAreaBuscar.append("Nombre Completo: " + Nombre + "\n");
                textAreaBuscar.append("Correo Electronico: " + Email + "\n");
                textAreaBuscar.append("Telefono: " + Telefono + "\n");
                textAreaBuscar.append("Fecha de Nacimiento: " + FechaNacimiento + "\n");
                textAreaBuscar.append("Cedula: " + Cedula + "\n");

            }
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    // Metodo para Iniciar la Pantalla de Buscar Cliente
    public void iniciarBuscarCliente() {
        setVisible(true);
        setSize(500, 500);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
