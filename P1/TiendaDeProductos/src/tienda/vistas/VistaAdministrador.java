package tienda.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import GUI.PantallaInicio;
import VistaEmpleados.VistaEmpleados;
import VistasCliente.ExploradorDeProductos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import tienda.modelo.Producto;

public class VistaAdministrador extends JFrame {
    private ArrayList<Producto> productos;
    private ExploradorDeProductos usuario;
    private JTextField nombreField;
    private JTextField precioField;
    private JTextField descripcionField;
    private JTextField imagenPathField;
    private File imagenFile;

    public VistaAdministrador(ArrayList<Producto> productos, ExploradorDeProductos usuario) {
        this.productos = productos;
        this.usuario = usuario;
        setTitle("Vista de Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 441, 533);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Agregar Producto", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(25, 46, 365, 300);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNombre.setBounds(10, 20, 80, 30);
        panel.add(lblNombre);

        nombreField = new JTextField();
        nombreField.setFont(new Font("Tahoma", Font.PLAIN, 13));
        nombreField.setBounds(100, 20, 200, 30);
        panel.add(nombreField);
        nombreField.setColumns(10);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblPrecio.setBounds(10, 70, 80, 30);
        panel.add(lblPrecio);

        precioField = new JTextField();
        precioField.setFont(new Font("Tahoma", Font.PLAIN, 13));
        precioField.setBounds(100, 70, 200, 30);
        panel.add(precioField);
        precioField.setColumns(10);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblDescripcion.setBounds(10, 120, 100, 30);
        panel.add(lblDescripcion);

        descripcionField = new JTextField();
        descripcionField.setFont(new Font("Tahoma", Font.PLAIN, 13));
        descripcionField.setBounds(120, 120, 180, 30);
        panel.add(descripcionField);
        descripcionField.setColumns(10);

        JLabel lblImagen = new JLabel("Imagen:");
        lblImagen.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblImagen.setBounds(10, 170, 80, 30);
        panel.add(lblImagen);

        imagenPathField = new JTextField();
        imagenPathField.setFont(new Font("Tahoma", Font.PLAIN, 13));
        imagenPathField.setBounds(100, 170, 140, 30);
        panel.add(imagenPathField);
        imagenPathField.setColumns(10);
        imagenPathField.setEditable(false);

        JButton btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnSeleccionarImagen.setBounds(250, 170, 100, 30);
        panel.add(btnSeleccionarImagen);

        btnSeleccionarImagen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    imagenFile = fileChooser.getSelectedFile();
                    imagenPathField.setText(imagenFile.getAbsolutePath());
                }
            }
        });

        JButton btnAgregar = new JButton("Agregar Producto");
        btnAgregar.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnAgregar.setBounds(86, 210, 200, 40);
        panel.add(btnAgregar);

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto();
            }
        });

        JPanel panelNavegacion = new JPanel();
        panelNavegacion.setLayout(new FlowLayout());
        panelNavegacion.setBounds(25, 405, 365, 40);
        contentPane.add(panelNavegacion);

        JButton volverButton = new JButton("Volver al Inicio");
        panelNavegacion.add(volverButton);

        JLabel ImagenLabel = new JLabel("");
        ImagenLabel.setIcon(new ImageIcon(VistaAdministrador.class.getResource("/IMG/nubes banner.png")));
        ImagenLabel.setBounds(0, 0, 725, 36);
        contentPane.add(ImagenLabel);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFiltro = new JMenu("Buscador");
        menuBar.add(mnFiltro);

        JMenuItem mntmBuscador = new JMenuItem("Buscar ID");
        mntmBuscador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuscarProducto buscarProducto = new BuscarProducto();
                buscarProducto.setVisible(true);
            }
        });
        mnFiltro.add(mntmBuscador);

        JMenu mnEliminar = new JMenu("Eliminar");
        menuBar.add(mnEliminar);

        JMenuItem mntmEliminar = new JMenuItem("Eliminar ID");
        mntmEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EliminarProducto eliminarProducto = new EliminarProducto();
                eliminarProducto.setVisible(true);
            }
        });
        mnEliminar.add(mntmEliminar);

        JMenu mnEditar = new JMenu("Editar");
        menuBar.add(mnEditar);

        JMenuItem mntmEditar = new JMenuItem("Editar ID");
        mntmEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditarProducto editarProducto = new EditarProducto();
                editarProducto.setVisible(true);
            }
        });
        mnEditar.add(mntmEditar);
        
        JMenu mnAcceso = new JMenu("Acceso");
        menuBar.add(mnAcceso);

        JMenuItem mntmEmpleados = new JMenuItem("Ver Empleados");
        mntmEmpleados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VistaEmpleados vistaEmpleados = new VistaEmpleados();
                vistaEmpleados.setVisible(true); // Mostrar la VistaEmpleados
                dispose(); // Cerrar la VistaAdministrador
            }
        });
        mnAcceso.add(mntmEmpleados);

        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PantallaInicio.main(null);
                dispose();
            }
        });
    }

    private void agregarProducto() {
        try {
            String nombre = nombreField.getText();
            double precio = Double.parseDouble(precioField.getText());
            String descripcion = descripcionField.getText();
            String imagenPath = (imagenFile != null) ? imagenFile.getAbsolutePath() : null;

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos?serverTimezone=America/Santo_Domingo", "root", "12345");
            String sql = "INSERT INTO productos (nombre, precio, descripcion, imagen_ruta) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nombre);
            statement.setDouble(2, precio);
            statement.setString(3, descripcion);
            statement.setString(4, imagenPath);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                int nuevoId = obtenerIdProducto(nombre, precio, descripcion, imagenPath);
                Producto nuevoProducto = new Producto(nuevoId, nombre, precio, descripcion, imagenPath);
                productos.add(nuevoProducto);
                JOptionPane.showMessageDialog(null, "Producto agregado exitosamente.");
                usuario.actualizarProductos(productos);

                nombreField.setText("");
                precioField.setText("");
                descripcionField.setText("");
                imagenPathField.setText("");
                imagenFile = null;
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo agregar el producto.");
            }
            connection.close();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Precio no válido. Por favor ingrese un número válido.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.");
            ex.printStackTrace();
        }
    }

    private int obtenerIdProducto(String nombre, double precio, String descripcion, String imagenPath) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos?serverTimezone=America/Santo_Domingo", "root", "12345");
        String sql = "SELECT id FROM productos WHERE nombre = ? AND precio = ? AND descripcion = ? AND imagen_ruta = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, nombre);
        statement.setDouble(2, precio);
        statement.setString(3, descripcion);
        statement.setString(4, imagenPath);
        ResultSet resultSet = statement.executeQuery();
        int id = -1;
        if (resultSet.next()) {
            id = resultSet.getInt("id");
        }
        connection.close();
        return id;
    }
}
