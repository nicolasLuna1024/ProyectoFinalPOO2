import PDFS.GeneradorPDF;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PantallaCliente extends JFrame {
    private JTabbedPane tabbedPane1;
    private JTable Tabla;
    private JPanel VentanaCliente;
    private JTextField textBusqueda_idCancha;
    private JButton visualizarImagenButton;
    private JTextField textIDCanchaBuscarTurno;
    private JButton verTurnosButton;
    private JTable tableMostrarTurnos;
    private JTextField textIDturnoReserva;
    private JButton reservarButton;
    private JTextField textDueñoReserva;
    private JTextField textCedulaReserva;
    private JButton cerrarSesionButton;

    public PantallaCliente() {
        super("Menu Administrador");
        setContentPane(VentanaCliente);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(655, 325);
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
        // Boton ver turnos en la tabla
        verTurnosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verTurnos();
            }
        });
        // Boton Reservar
        reservarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reservarTurno();
            }
        });
        //Boton Cerrar Secion
        cerrarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login();
                login.iniciarLogin();
                dispose();
            }
        });
    }
    // Método para reservar un turno
    public void reservarTurno() {
        String idTurno = textIDturnoReserva.getText().trim();
        String cedula = textCedulaReserva.getText().trim();
        String nombreCompleto = textDueñoReserva.getText().trim();
        String idCancha = textIDCanchaBuscarTurno.getText().trim(); // ID de cancha que ya has buscado
        String fecha = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()); // Fecha actual

        if (idTurno.isEmpty() || cedula.isEmpty() || nombreCompleto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection conn = conexionBase();
            conn.setAutoCommit(false); // Iniciar transacción

            // Verificar la disponibilidad del turno
            String checkTurnoQuery = "SELECT disponibilidad FROM Turnos WHERE IdTurnos = ?";
            PreparedStatement pstmtCheckTurno = conn.prepareStatement(checkTurnoQuery);
            pstmtCheckTurno.setString(1, idTurno);
            ResultSet rsCheckTurno = pstmtCheckTurno.executeQuery();

            if (rsCheckTurno.next()) {
                String disponibilidad = rsCheckTurno.getString("disponibilidad");
                if ("RESERVADO".equals(disponibilidad)) {
                    JOptionPane.showMessageDialog(this, "El turno ya está reservado. Por favor, elija otro turno.", "Turno no disponible", JOptionPane.WARNING_MESSAGE);
                    rsCheckTurno.close();
                    pstmtCheckTurno.close();
                    conn.rollback();
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el turno especificado.", "Error", JOptionPane.ERROR_MESSAGE);
                rsCheckTurno.close();
                pstmtCheckTurno.close();
                conn.rollback();
                return;
            }

            rsCheckTurno.close();
            pstmtCheckTurno.close();

            // Actualizar la disponibilidad del turno a "RESERVADO"
            String updateTurnoQuery = "UPDATE Turnos SET disponibilidad = 'RESERVADO' WHERE IdTurnos = ?";
            PreparedStatement pstmtUpdateTurno = conn.prepareStatement(updateTurnoQuery);
            pstmtUpdateTurno.setString(1, idTurno);
            int rowsUpdated = pstmtUpdateTurno.executeUpdate();

            if (rowsUpdated == 0) {
                JOptionPane.showMessageDialog(this, "No se encontró el turno especificado.", "Error", JOptionPane.ERROR_MESSAGE);
                conn.rollback();
                pstmtUpdateTurno.close();
                return;
            }

            // Insertar la reserva en la tabla Reservas
            String insertReservaQuery = "INSERT INTO Reservas (idCancha, cedula, IdTurnos, nombreCompleto) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmtInsertReserva = conn.prepareStatement(insertReservaQuery);
            pstmtInsertReserva.setString(1, idCancha);
            pstmtInsertReserva.setDouble(2, Double.parseDouble(cedula));
            pstmtInsertReserva.setString(3, idTurno);
            pstmtInsertReserva.setString(4, nombreCompleto);
            pstmtInsertReserva.executeUpdate();

            // Confirmar la transacción
            conn.commit();

            // Cerrar conexiones
            pstmtUpdateTurno.close();
            pstmtInsertReserva.close();
            conn.close();

            // Generar el PDF de la reserva
            GeneradorPDF.generarFactura(idCancha, idTurno, nombreCompleto, cedula, fecha);

            JOptionPane.showMessageDialog(this, "Reserva realizada con éxito y PDF generado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    // Metodo para conectar a la base de datos
    public Connection conexionBase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/GestionDeCanchas";
        String user = "root";
        String password = "123456";
        return DriverManager.getConnection(url, user, password);
    }

    // Metodo para obtener los datos de la tabla Canchas en Ver canchas
    public DefaultTableModel obtenerDatosDeCanchas(Connection conn) throws SQLException {
        String query = "SELECT idCancha, tipo, ubicacion, numJugadores, precio, imagen FROM Canchas";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Crear el modelo de la tabla y establecer las columnas con títulos en mayúsculas en Ver canchas
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID CANCHA");
        model.addColumn("TIPO");
        model.addColumn("UBICACIÓN");
        model.addColumn("NÚMERO DE JUGADORES");
        model.addColumn("PRECIO");
        model.addColumn("IMAGEN");

        // Iterar sobre los resultados y agregarlos al modelo en Ver canchas
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
    // Metodo para obtener la imagen de la cancha por ID en Ver Canchas
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

    // Metodo para mostrar la imagen en Ver Canchas
    public void mostrarImagen(byte[] imageBytes) {
        ImageIcon imageIcon = new ImageIcon(imageBytes);
        Image image = imageIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(image);

        JOptionPane.showMessageDialog(this, new JLabel(scaledImageIcon), "Imagen de la Cancha", JOptionPane.PLAIN_MESSAGE);
    }
    // Metodo para ver los registros de la tabla Turnos filtrados por idCancha
    public void verTurnos() {
        String idCancha = textIDCanchaBuscarTurno.getText().trim(); // Obtener el valor del JTextField

        if (idCancha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese el ID de la cancha.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Conectar a la base de datos
            Connection conn = conexionBase();

            // Query para seleccionar las columnas deseadas filtradas por idCancha
            String query = "SELECT IdTurnos, disponibilidad, horaTurnos FROM Turnos WHERE idCancha = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, idCancha);
            ResultSet rs = pstmt.executeQuery();

            // Crear el modelo de la tabla y establecer las columnas
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID Turnos");
            model.addColumn("Disponibilidad");
            model.addColumn("Hora Turnos");

            // Iterar sobre los resultados y agregarlos al modelo
            while (rs.next()) {
                Object[] row = new Object[3];
                row[0] = rs.getString("IdTurnos");
                row[1] = rs.getString("disponibilidad");
                row[2] = rs.getString("horaTurnos");
                model.addRow(row);
            }

            rs.close();
            pstmt.close();

            // Configurar la tabla para mostrar el modelo
            tableMostrarTurnos.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    // Metodo para Iniciar la Pantalla de Login
    public void iniciarCliente() {
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}