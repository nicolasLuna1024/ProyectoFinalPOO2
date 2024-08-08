import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    private JTabbedPane tabbedPane2;
    private JTextField textIdCancha;
    private JTextField textTipoCancha;
    private JTextField textUbicacion;
    private JTextField textJugadoresCancha;
    private JTextField textPrecioCancha;
    private JButton seleccionarUnaImagenButton;
    private JButton insertarCanchaButton;
    private JTextField textField1;
    private JLabel lblFoto;
    private JTextField textID_cancha;
    private JTextArea textBuscarArea;
    private JButton buscarButton;
    private JButton eliminarButton;
    private JLabel lblFotoVer;
    private JTabbedPane PanelTurnos;
    private JTextField textTurnoID;
    private JTextField textDisponibilidadTurno;
    private JTextField textHoraTurno;
    private JTextField textCanchaID;
    private JButton insertarTurnoButton;
    private JTextArea textArea1;
    private JTextField textTurnoIDAcciones;
    private JButton buscarTurnoButton;
    private JButton eliminarTurnoButton;
    private JButton cerrarSesionButton;
    private JTable tablaVerReservas;

    private File selectedFile; // Para almacenar el archivo de imagen seleccionado

    public PantallaAdmin() {
        super("Menu Administrador");
        setContentPane(VentanaPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(825, 550);
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
        //Buscar Cliente
        buscarButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VerRegistro();
            }
        });
        //Actualizar Cliente
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
        //Eliminar Cliente
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
        //Insertar cancha
        insertarCanchaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    IngresarCancha();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        //Subir una Imagen
        seleccionarUnaImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser archivo = new JFileChooser();
                int ventana = archivo.showOpenDialog(null);
                if (ventana == JFileChooser.APPROVE_OPTION) {
                    selectedFile = archivo.getSelectedFile();
                    textField1.setText(String.valueOf(selectedFile));
                    Image foto = getToolkit().getImage(textField1.getText());
                    foto = foto.getScaledInstance(120, 120, Image.SCALE_DEFAULT);
                    lblFoto.setIcon(new ImageIcon(foto));
                }
            }
        });
        // Buscar Cancha con ID
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        //Boton Buscar y Ver Cancha
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VerCancha();
            }
        });
        // Boton para Eliminar Cancha con el IdCancha
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCancha();
            }
        });
        // Boton para Insertar Turnos
        insertarTurnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertarTurnos();
            }
        });
        //Metodo para Buscar Turno
        buscarTurnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarTurno();
            }
        });
        //Metodo para Eliminar Turno
        eliminarTurnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarTurno();
            }
        });
        //Boton Cerrar Cesion
        cerrarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login();
                login.iniciarLogin();
                dispose();
            }
        });
        tabbedPane1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                mostrarReservas();
            }
        });
    }
    //Metodo para Eliminar Turno
    public void eliminarTurno() {
        String turnoID = textTurnoIDAcciones.getText();
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = conexionBase(); // Método para conectar a la base de datos
            String sql = "DELETE FROM Turnos WHERE IdTurnos = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, turnoID);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "TURNO ELIMINADO", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                textArea1.setText(""); // Limpiar el área de texto después de eliminar
            } else {
                JOptionPane.showMessageDialog(this, "NO SE ENCONTRO EL TURNO", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR AL ELIMINAR ", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }
    // Método para mostrar reservas en la tabla
    public void mostrarReservas() {
        try {
            // Conectar a la base de datos
            Connection conn = conexionBase();

            // Consulta para obtener los registros de la tabla Reservas
            String query = "SELECT idCancha, cedula, IdTurnos, nombreCompleto FROM Reservas";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            // Crear el modelo de la tabla y establecer las columnas
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID Cancha");
            model.addColumn("Cédula");
            model.addColumn("ID Turnos");
            model.addColumn("Nombre Completo");

            // Iterar sobre los resultados y agregarlos al modelo
            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = rs.getString("idCancha");
                row[1] = rs.getDouble("cedula");
                row[2] = rs.getString("IdTurnos");
                row[3] = rs.getString("nombreCompleto");
                model.addRow(row);
            }

            // Configurar la tabla para mostrar el modelo
            tablaVerReservas.setModel(model);

            // Cerrar conexiones
            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    //Metodo para Buscar Turno
    public void buscarTurno() {
        String turnoID = textTurnoIDAcciones.getText();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = conexionBase(); // Método para conectar a la base de datos
            String sql = "SELECT * FROM Turnos WHERE IdTurnos = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, turnoID);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String disponibilidad = rs.getString("disponibilidad");
//                String horaTurnos = rs.getString("horaTurnos");
                String idCancha = rs.getString("idCancha");

                textArea1.setText("ID Turno: " + turnoID + "\n" +
                        "Disponibilidad: " + disponibilidad + "\n" +
//                        "Hora Turno: " + horaTurnos + "\n" +
                        "ID Cancha: " + idCancha);
            } else {
                JOptionPane.showMessageDialog(this, "NO SE ENCONTRO EL TURNO", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR AL BUSCAR EL TURNO", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    // Metodo Insertar Turnos
    public void insertarTurnos() {
        String turnoID = textTurnoID.getText();
        String disponibilidadTurno = textDisponibilidadTurno.getText();
        String horaTurno = textHoraTurno.getText();
        String canchaID = textCanchaID.getText();

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = conexionBase(); // Método para conectar a la base de datos
            String sql = "INSERT INTO Turnos (IdTurnos, disponibilidad, idCancha) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, turnoID);
            stmt.setString(2, disponibilidadTurno);
//            stmt.setString(3, horaTurno);
            stmt.setString(3, canchaID);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "TURNO INSERTADO CORRECTAMENTE", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR AL INSERTAR TURNO", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // Método para Eliminar Cancha
    public void eliminarCancha() {
        try {
            String idCancha = textID_cancha.getText().trim(); // Obtener el ID de la cancha

            if (idCancha.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese el ID de la cancha.");
                return;
            }

            Connection conecta = conexionBase();
            String query = "DELETE FROM Canchas WHERE idCancha = ?";
            PreparedStatement pstmt = conecta.prepareStatement(query);
            pstmt.setString(1, idCancha);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                JOptionPane.showMessageDialog(null, "CANCHA NO ENCONTRADA");
            } else {
                JOptionPane.showMessageDialog(null, "CANCHA ELIMINADA EXITOSAMENTE.");
            }

            pstmt.close();
            conecta.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Método para Buscar Cancha
    public void VerCancha() {
        try {
            String idCancha = textID_cancha.getText().trim(); // Obtener el ID de la cancha para las operaciones

            Connection conn = conexionBase();
            String query = "SELECT * FROM Canchas WHERE idCancha = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, idCancha);
            textBuscarArea.setText("");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String tipoCancha = rs.getString("tipo");
                String ubicacion = rs.getString("ubicacion");
                int numJugadores = rs.getInt("numJugadores");
                double precio = rs.getDouble("precio");
                byte[] imagenBytes = rs.getBytes("imagen");

                textBuscarArea.append("Tipo de Cancha: " + tipoCancha + "\n");
                textBuscarArea.append("Ubicacion: " + ubicacion + "\n");
                textBuscarArea.append("Numero de Jugadores admitidos: " + numJugadores + "\n");
                textBuscarArea.append("Precio: " + precio + "\n");

                if (imagenBytes != null) {
                    ImageIcon imageIcon = new ImageIcon(imagenBytes);
                    Image image = imageIcon.getImage();
                    Image scaledImage = image.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    lblFotoVer.setIcon(new ImageIcon(scaledImage));
                } else {
                    lblFotoVer.setIcon(null);
                }
            } else {
                textBuscarArea.append("No se encontró la cancha con el ID proporcionado.\n");
                lblFotoVer.setIcon(null);
            }
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // Método Ingresar datos de Cancha
    public void IngresarCancha() throws SQLException {
        String IDcancha = textIdCancha.getText();
        String tipoCancha = textTipoCancha.getText();
        String ubicacion = textUbicacion.getText();
        String numeroJugadores = textJugadoresCancha.getText();
        String precio = textPrecioCancha.getText();

        Connection conecta = conexionBase();

        String query = "INSERT INTO Canchas(idCancha, tipo, ubicacion, numJugadores, precio, imagen) VALUES(?,?,?,?,?,?)";
        PreparedStatement pstmt = conecta.prepareStatement(query);
        pstmt.setString(1, IDcancha);
        pstmt.setString(2, tipoCancha);
        pstmt.setString(3, ubicacion);
        pstmt.setString(4, numeroJugadores);
        pstmt.setDouble(5, Double.parseDouble(precio));

        // Leer el archivo de imagen y establecerlo en el PreparedStatement
        if (selectedFile != null) {
            try {
                byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());
                pstmt.setBytes(6, imageBytes);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al leer la imagen: " + ex.getMessage());
                return;
            }
        } else {
            pstmt.setNull(6, Types.BLOB);
        }

        // Ejecutar la inserción
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "REGISTRO INSERTADO CORRECTAMENTE");
        }
        pstmt.close();
        conecta.close();
    }
    // Metodo Ingresar datos de Cliente
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
    //Actualizar Cliente
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
    // Metodo para Iniciar la Pantalla de Login
    public void iniciarAdministrador() {
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }




}
