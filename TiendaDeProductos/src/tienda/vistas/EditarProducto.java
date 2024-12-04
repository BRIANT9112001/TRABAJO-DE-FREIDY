package tienda.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EditarProducto extends JFrame {
    private JTextField txtIdProducto;
    private JTextField nombreField;
    private JTextField precioField;
    private JTextField descripcionField;
    private File imagenFile;
    private JTextField imagenPathField;

    public EditarProducto() {
    	setTitle("Editar Producto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 450);
        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Editar Producto", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(10, 22, 413, 300);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblIdProducto = new JLabel("ID Producto:");
        lblIdProducto.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblIdProducto.setBounds(10, 20, 120, 30);
        panel.add(lblIdProducto);

        txtIdProducto = new JTextField();
        txtIdProducto.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtIdProducto.setBounds(140, 20, 200, 30);
        panel.add(txtIdProducto);
        txtIdProducto.setColumns(10);

        JButton btnCargar = new JButton("Cargar Producto");
        btnCargar.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnCargar.setBounds(140, 60, 150, 30);
        panel.add(btnCargar);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNombre.setBounds(10, 100, 100, 30);
        panel.add(lblNombre);

        nombreField = new JTextField();
        nombreField.setFont(new Font("Tahoma", Font.PLAIN, 13));
        nombreField.setBounds(140, 100, 200, 30);
        panel.add(nombreField);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblPrecio.setBounds(10, 140, 100, 30);
        panel.add(lblPrecio);

        precioField = new JTextField();
        precioField.setFont(new Font("Tahoma", Font.PLAIN, 13));
        precioField.setBounds(140, 140, 200, 30);
        panel.add(precioField);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblDescripcion.setBounds(10, 180, 120, 30);
        panel.add(lblDescripcion);

        descripcionField = new JTextField();
        descripcionField.setFont(new Font("Tahoma", Font.PLAIN, 13));
        descripcionField.setBounds(140, 180, 200, 30);
        panel.add(descripcionField);

        JLabel lblImagen = new JLabel("Imagen:");
        lblImagen.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblImagen.setBounds(10, 220, 100, 30);
        panel.add(lblImagen);

        imagenPathField = new JTextField();
        imagenPathField.setFont(new Font("Tahoma", Font.PLAIN, 13));
        imagenPathField.setBounds(140, 220, 156, 30);
        panel.add(imagenPathField);
        imagenPathField.setEditable(false);

        JButton btnExaminar = new JButton("Examinar");
        btnExaminar.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnExaminar.setBounds(306, 220, 85, 30);
        panel.add(btnExaminar);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnGuardar.setBounds(173, 340, 100, 30);
        contentPane.add(btnGuardar);

        // Eventos de los botones
        btnExaminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarImagen();
            }
        });

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarProducto();
            }
        });

        btnCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarProducto();
            }
        });
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagenFile = fileChooser.getSelectedFile();
            imagenPathField.setText(imagenFile.getAbsolutePath());
        }
    }

    private void guardarProducto() {
        int idProducto = Integer.parseInt(txtIdProducto.getText());
        String nombre = nombreField.getText();
        double precio = Double.parseDouble(precioField.getText());
        String descripcion = descripcionField.getText();

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos?serverTimezone=America/Santo_Domingo", "root", "12345");

            // Verificar si se seleccionó una imagen
            String query;
            PreparedStatement pst;

            if (imagenFile != null && !imagenFile.getAbsolutePath().isEmpty()) {
                String imagenPath = imagenFile.getAbsolutePath();
                query = "UPDATE productos SET nombre = ?, precio = ?, descripcion = ?, imagenPath = ? WHERE id = ?";
                pst = con.prepareStatement(query);
                pst.setString(1, nombre);
                pst.setDouble(2, precio);
                pst.setString(3, descripcion);
                pst.setString(4, imagenPath);
                pst.setInt(5, idProducto);
            } else {
                query = "UPDATE productos SET nombre = ?, precio = ?, descripcion = ? WHERE id = ?";
                pst = con.prepareStatement(query);
                pst.setString(1, nombre);
                pst.setDouble(2, precio);
                pst.setString(3, descripcion);
                pst.setInt(4, idProducto);
            }

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");

            pst.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarProducto() {
        int idProducto;
        try {
            // Validar que el ID ingresado es un número
            idProducto = Integer.parseInt(txtIdProducto.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Conexión a la base de datos
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tienda_productos?serverTimezone=America/Santo_Domingo", 
                "root", 
                "12345"
            );

            // Consulta SQL sin la columna imagenPath
            String query = "SELECT nombre, precio, descripcion FROM productos WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, idProducto);

            // Ejecutar la consulta
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                // Rellenar los campos del formulario con los datos obtenidos
                nombreField.setText(rs.getString("nombre"));
                precioField.setText(String.valueOf(rs.getDouble("precio")));
                descripcionField.setText(rs.getString("descripcion"));
                imagenPathField.setText(""); // Vaciar el campo de la imagen, ya que no es obligatorio
                imagenFile = null; // Asegurarse de que no quede una imagen seleccionada previamente
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró un producto con el ID especificado.", "Producto no encontrado", JOptionPane.WARNING_MESSAGE);
            }

            // Cerrar conexión y recursos
            rs.close();
            pst.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
