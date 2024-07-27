import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PanelActualizarCliente extends JFrame {
    private JTextField textCedulaBuscar;
    private JTextField textNombreActualizar;
    private JTextField textEmailActualizar;
    private JTextField textTelefonoActualizar;
    private JTextField textDireccionActualizar;
    private JTextField textFechaActualizar;
    private JButton actualizarButton;
    private JButton menuButton;
    private JTextArea textArea1;
    private JPanel panelActulizarCliente;
    private JButton buscarButton;

    public PanelActualizarCliente() {
        super("Actualizar Cliente");
        setContentPane(panelActulizarCliente);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VerRegistro();
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    actualizarCliente();
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

    // Menu Iniciar Pantalla gestion Clientes
    public void iniciarActualizarCliente() {
        setVisible(true);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Metodo para Buscar paciente por la cedula y asi ver los datos del cliente
    public void VerRegistro() {
        try {
            String cedula = textCedulaBuscar.getText().trim(); // Obtener la cédula ingresada

            Connection conn = conexionBase();
            String query = "SELECT * FROM Jugador WHERE cedula = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cedula);
            textArea1.setText(" ");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String NombreCompleto = rs.getString("nombreCompleto");
                String email = rs.getString("email");
                int telefono = rs.getInt("telefono");
                String direccion = rs.getString("direccion");
                String fecha = rs.getString("fechaNacimiento");

                textArea1.append("Nombre: " + NombreCompleto + "\n");
                textArea1.append("Correo Electronico: " + email + "\n");
                textArea1.append("Telefono: " + telefono + "\n");
                textArea1.append("Direccion: " + direccion + "\n");
                textArea1.append("Fecha de Nacimiento: " + fecha + "\n");
                textArea1.append("\n");

                // Setear los valores en los campos de texto para actualizar
                textNombreActualizar.setText(NombreCompleto);
                textEmailActualizar.setText(email);
                textTelefonoActualizar.setText(String.valueOf(telefono));
                textDireccionActualizar.setText(direccion);
                textFechaActualizar.setText(fecha);
            } else {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado");
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para actualizar los datos del cliente
    public void actualizarCliente() throws SQLException {
        String cedula = textCedulaBuscar.getText().trim();
        String nombre = textNombreActualizar.getText().trim();
        String email = textEmailActualizar.getText().trim();
        String telefonoStr = textTelefonoActualizar.getText().trim();
        String direccion = textDireccionActualizar.getText().trim();
        String fechaNacimiento = textFechaActualizar.getText().trim();

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
}
