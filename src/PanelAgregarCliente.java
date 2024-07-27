import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PanelAgregarCliente extends JFrame{
    private JTextField textNombreCompleto;
    private JTextField textEmail;
    private JTextField textTelefono;
    private JTextField textDireccion;
    private JTextField textFechaDeNacimiento;
    private JTextField textCedula;
    private JButton agregarButton;
    private JButton verClienteButton;
    private JPanel panelAgregarCliente;

    public PanelAgregarCliente(){
        super("Agregar Cliente");
        setContentPane(panelAgregarCliente);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setSize(500,500);

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    InsertarDatos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
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
    // Metodo Ingresar
    public void InsertarDatos() throws SQLException {
        String nombreCompleto = textNombreCompleto.getText();
        String email = textEmail.getText();
        String telefono = textTelefono.getText();
        String direccion = textDireccion.getText();
        String fechaNacimiento = textFechaDeNacimiento.getText();
        String cedula = textCedula.getText();

        Connection conecta =conexionBase();

        String query ="insert into Jugador(nombreCompleto,email,telefono,direccion,fechaNacimiento,cedula) values(?,?,?,?,?,?)";
        PreparedStatement pstmt = conecta.prepareStatement(query);
        pstmt.setString(1,nombreCompleto);
        pstmt.setString(2,email);
        pstmt.setInt(3, Integer.parseInt(telefono));
        pstmt.setString(4,direccion);
        pstmt.setString(5,fechaNacimiento);
        pstmt.setDouble(6,Double.parseDouble(cedula));




        // ACTUALIZAR

        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0){
            JOptionPane.showMessageDialog(null,"REGISTRO INSERTADO CORRECTAMENTE");
        }
        pstmt.close();
        conecta.close();

    }
    //Menu Iniciar pantalla de Agregar cliente
    public void iniciarAgregarCliente(){
        setVisible(true);
        setSize(500,500);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
