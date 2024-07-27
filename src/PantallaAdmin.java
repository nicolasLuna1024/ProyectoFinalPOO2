import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PantallaAdmin extends JFrame{
    private JTabbedPane tabbedPane1;
    private JComboBox comboBox1;
    private JTextField textNombreCompleto;
    public JPanel VentanaPrincipal;
    private JTextField textEmail;
    private JTextField textTelefono;
    private JTextField textDireccion;
    private JTextField textFechaDeNacimiento;
    private JTextField textCedula;
    private JButton buscarButton1;
    private JButton eliminarButton1;
    private JButton agregarButton;
    private JButton actualizarButton1;

    public PantallaAdmin() {
        super("Login");
        setContentPane(VentanaPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        //Agregar
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
        //Buscar
        buscarButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VerRegistro();

            }
        });
        //Actualizar
        actualizarButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    actualizarCliente();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        //Eliminar
        eliminarButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cedulaEliminar = textCedula.getText().trim();
                try {
                    eliminarCliente(cedulaEliminar);
                    JOptionPane.showMessageDialog(null, "Registro del Cliente Eliminado Exitosamente");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al eliminar cliente: " + ex.getMessage());
                }
            }
        });
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
    public void actualizarCliente() throws SQLException {

        String cedula = textCedula.getText().trim();
        String nombre = textNombreCompleto.getText().trim();
        String email = textEmail.getText().trim();
        String telefonoStr = textTelefono.getText().trim();
        String direccion = textDireccion.getText().trim();
        String fechaNacimiento = textFechaDeNacimiento.getText().trim();

        Connection conn = conexionBase();

        // Construir la consulta dinámicamente
        StringBuilder queryBuilder = new StringBuilder("UPDATE Jugador SET ");
        boolean isFirst = true;

        if (!nombre.isEmpty()) {
            queryBuilder.append("nombreCompleto = ?");
            isFirst = false;
        }
        if (!email.isEmpty()) {
            if (!isFirst) queryBuilder.append(", ");
            queryBuilder.append("email = ?");
            isFirst = false;
        }
        if (!telefonoStr.isEmpty()) {
            if (!isFirst) queryBuilder.append(", ");
            queryBuilder.append("telefono = ?");
            isFirst = false;
        }
        if (!direccion.isEmpty()) {
            if (!isFirst) queryBuilder.append(", ");
            queryBuilder.append("direccion = ?");
            isFirst = false;
        }
        if (!fechaNacimiento.isEmpty()) {
            if (!isFirst) queryBuilder.append(", ");
            queryBuilder.append("fechaNacimiento = ?");
        }

        queryBuilder.append(" WHERE cedula = ?");

        PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString());
        int paramIndex = 1;

        if (!nombre.isEmpty()) {
            pstmt.setString(paramIndex++, nombre);
        }
        if (!email.isEmpty()) {
            pstmt.setString(paramIndex++, email);
        }
        if (!telefonoStr.isEmpty()) {
            pstmt.setInt(paramIndex++, Integer.parseInt(telefonoStr));
        }
        if (!direccion.isEmpty()) {
            pstmt.setString(paramIndex++, direccion);
        }
        if (!fechaNacimiento.isEmpty()) {
            pstmt.setString(paramIndex++, fechaNacimiento);
        }
        pstmt.setString(paramIndex, cedula);

        int filasActualizadas = pstmt.executeUpdate();
        if (filasActualizadas > 0) {
            JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente");
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el cliente");
        }

        pstmt.close();
        conn.close();
    }
    // Método para Buscar Cliente
    public void VerRegistro() {
        try {
            //limpiarCampos(); // Limpiar campos antes de la búsqueda
            String cedula = textCedula.getText().trim(); // Obtener la cédula ingresada
            Connection conn = conexionBase();
            String query = "SELECT * FROM Jugador WHERE cedula = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cedula);
            ResultSet rs = pstmt.executeQuery();

            boolean registroEncontrado = false; // Bandera para verificar si se encontró algún registro

            while (rs.next()) {
                String Nombre = rs.getString("nombreCompleto");
                String Email = rs.getString("email");
                Integer Telefono = rs.getInt("telefono");
                String FechaNacimiento = rs.getString("fechaNacimiento");
                String Direccion = rs.getString("direccion");

                textNombreCompleto.setText(Nombre);
                textEmail.setText(Email);
                textTelefono.setText(String.valueOf(Telefono));
                textDireccion.setText(Direccion);
                textFechaDeNacimiento.setText(FechaNacimiento);

                registroEncontrado = true; // Se encontró un registro
            }

            if (!registroEncontrado) {
                JOptionPane.showMessageDialog(this, "NO SE ENCONTRO NINGUN CLIENTE");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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


    // Metodo Conexion base
    public Connection conexionBase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/GestionDeCanchas";
        String user = "root";
        String password = "123456";
        return DriverManager.getConnection(url, user, password);
    }
    // Método para limpiar los campos de texto
    private void limpiarCampos() {
        textNombreCompleto.setText("");
        textEmail.setText("");
        textTelefono.setText("");
        textDireccion.setText("");
        textFechaDeNacimiento.setText("");
        textCedula.setText(""); // Limpiar la cédula ingresada también
    }



}
