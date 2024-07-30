import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PantallaCliente extends JFrame {
    private JTabbedPane tabbedPane1;
    private JTable Tabla;
    private JPanel VentanaCliente;
    private JTextField textBusqueda_idCancha;
    private JButton visualizarImagenButton;

    public PantallaCliente() {
        super("Menu Administrador");
        setContentPane(VentanaCliente);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(625, 325);
        setLocationRelativeTo(null);

        try {
            // Conectar a la base de datos y configurar la JTable
            Connection conn = conexionBase();
            DefaultTableModel model = obtenerDatosDeCanchas(conn);
            Tabla.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
        visualizarImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idCancha = textBusqueda_idCancha.getText();
                if (!idCancha.isEmpty()) {
                    try {
                        Connection conn = conexionBase();
                        byte[] imageBytes = obtenerImagenDeCancha(conn, idCancha);
                        if (imageBytes != null) {
                            mostrarImagen(imageBytes);
                        } else {
                            JOptionPane.showMessageDialog(VentanaCliente, "No se encontró la imagen para el ID de cancha proporcionado", "Imagen no encontrada", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(VentanaCliente, "Error al conectar a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(VentanaCliente, "Por favor, ingrese un ID de cancha", "Campo vacío", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    // Metodo para conectar a la base de datos
    public Connection conexionBase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/GestionDeCanchas";
        String user = "root";
        String password = "123456";
        return DriverManager.getConnection(url, user, password);
    }

    // Metodo para obtener los datos de la tabla Canchas
    public DefaultTableModel obtenerDatosDeCanchas(Connection conn) throws SQLException {
        String query = "SELECT idCancha, tipo, ubicacion, numJugadores, precio, imagen FROM Canchas";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Crear el modelo de la tabla y establecer las columnas con títulos en mayúsculas
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID CANCHA");
        model.addColumn("TIPO");
        model.addColumn("UBICACIÓN");
        model.addColumn("NÚMERO DE JUGADORES");
        model.addColumn("PRECIO");
        model.addColumn("IMAGEN");

        // Iterar sobre los resultados y agregarlos al modelo
        while (rs.next()) {
            Object[] row = new Object[6];
            row[0] = rs.getString("idCancha");
            row[1] = rs.getString("tipo");
            row[2] = rs.getString("ubicacion");
            row[3] = rs.getInt("numJugadores");
            row[4] = rs.getDouble("precio");
            row[5] = rs.getBytes("imagen"); // Mostraría los bytes de la imagen, podrías cambiar cómo se muestra
            model.addRow(row);
        }

        rs.close();
        stmt.close();

        return model;
    }
    // Metodo para obtener la imagen de la cancha por ID
    public byte[] obtenerImagenDeCancha(Connection conn, String idCancha) throws SQLException {
        String query = "SELECT imagen FROM Canchas WHERE idCancha = ?";
        java.sql.PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, idCancha);
        ResultSet rs = pstmt.executeQuery();

        byte[] imageBytes = null;
        if (rs.next()) {
            imageBytes = rs.getBytes("imagen");
        }

        rs.close();
        pstmt.close();

        return imageBytes;
    }

    // Metodo para mostrar la imagen
    public void mostrarImagen(byte[] imageBytes) {
        ImageIcon imageIcon = new ImageIcon(imageBytes);
        Image image = imageIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(image);

        JOptionPane.showMessageDialog(this, new JLabel(scaledImageIcon), "Imagen de la Cancha", JOptionPane.PLAIN_MESSAGE);
    }


}