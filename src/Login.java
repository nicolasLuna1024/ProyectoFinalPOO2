import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame {
    private JComboBox BoxTipoUsuario;
    private JPasswordField passwordField1;
    private JLabel IconoLogin;
    private JLabel TipoUsuario;
    private JTextField textUsuario;
    private JButton ingresarButton;
    private JPanel PanelLogin;

    public Login() {
        super("Login");
        setContentPane(PanelLogin);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuarioTipo = BoxTipoUsuario.getSelectedItem().toString();
                String nombre = textUsuario.getText();
                String pass = new String(passwordField1.getPassword());

                // Verificación de usuario Administrador
                if (usuarioTipo.equals("Administrador")) {
                    try {
                        if (verificarAdministrador(nombre, pass)) {
                            PantallaAdmin pantallaAdmin = new PantallaAdmin();
                            pantallaAdmin.iniciarAdministrador();
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (usuarioTipo.equals("Jugador")) {
                    try{
                        if (verificarJugador(nombre,pass)){
                            PantallaCliente pantallaCliente = new PantallaCliente();
                            pantallaCliente.iniciarCliente();
                            dispose();
                        }else {
                            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

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

    // Verificar Credenciales Administrador
    public boolean verificarAdministrador(String nombre, String password) throws SQLException {
        Connection conecta = conexionBase();
        String query = "SELECT * FROM Administradores WHERE nombre = ? AND password = ?";
        PreparedStatement pstmt = conecta.prepareStatement(query);
        pstmt.setString(1, nombre);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        boolean resultado = rs.next();
        rs.close();
        pstmt.close();
        conecta.close();
        return resultado;
    }
    //Verificar Credenciales Jugadores
    public boolean verificarJugador(String nombre, String password) throws SQLException {
        Connection conecta = conexionBase();
        String query = "SELECT * FROM Jugador WHERE nombreCompleto = ? AND cedula = ?";
        PreparedStatement pstmt = conecta.prepareStatement(query);
        pstmt.setString(1,nombre);
        pstmt.setString(2,password);
        ResultSet rs = pstmt.executeQuery();
        boolean resultado = rs.next();
        rs.close();
        pstmt.close();
        conecta.close();
        return resultado;

    }

    // Metodo para Iniciar la Pantalla de Login
    public void iniciarLogin() {
        setVisible(true);
        setSize(500, 500);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Login login = new Login();
        login.iniciarLogin();
    }
}
