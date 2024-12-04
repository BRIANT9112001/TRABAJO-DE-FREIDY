package VistasCliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import GUI.PantallaInicio;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import tienda.modelo.Producto;

public class ExploradorDeProductos extends JFrame {
    private ArrayList<Producto> productos;
    private JPanel contentPane;
    private JList<Producto> listaProductos;
    private DefaultListModel<Producto> listModel;

    public ExploradorDeProductos(ArrayList<Producto> productos) {
        this.productos = productos;
        setTitle("Vista de los productos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 400);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(new TitledBorder(null, "Productos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(10, 10, 465, 300);
        contentPane.add(panel);
        panel.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        listaProductos = new JList<>(listModel);
        listaProductos.setFont(new Font("Tahoma", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(listaProductos);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botón para volver al inicio
        JButton volverButton = new JButton("Volver al Inicio");
        volverButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
        volverButton.setBounds(165, 320, 150, 30);
        contentPane.add(volverButton);

        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PantallaInicio.main(null);
                dispose();
            } 	
        });

        // Inicializar lista de productos
        actualizarListaProductos();
    }

    // Método para actualizar la lista de productos en la vista de usuario
    public void actualizarProductos(ArrayList<Producto> productos) {
        this.productos = productos;
        actualizarListaProductos();
    }

    private void actualizarListaProductos() {
        listModel.clear();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos?serverTimezone=America/Santo_Domingo", "root", "12345");
            String sql = "SELECT nombre, precio, descripcion, imagen_ruta FROM productos";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                double precio = resultSet.getDouble("precio");
                String descripcion = resultSet.getString("descripcion");
                String imagenRuta = resultSet.getString("imagen_ruta");
                Producto producto = new Producto(nombre, precio, descripcion, imagenRuta);
                listModel.addElement(producto);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos.");
        }
    }
}