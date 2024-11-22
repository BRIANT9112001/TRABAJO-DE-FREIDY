package VistasCliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import GUI.PantallaInicio;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import tienda.modelo.Producto;

public class Compras extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<Producto> carrito;
    private ArrayList<Producto> productos;

    // Constructor que acepta una lista de productos
    public Compras(ArrayList<Producto> productos) {
        this.productos = productos;
        setTitle("Compras");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JLabel lblTitulo = new JLabel("Catálogo de Productos");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        carrito = new ArrayList<>();

        String[] columnNames = {"ID", "Nombre", "Precio", "Descripción"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        cargarProductos();

        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        JButton btnAgregarCarrito = new JButton("Agregar al Carrito");
        panelBotones.add(btnAgregarCarrito);

        JButton btnVerCarrito = new JButton("Ver Carrito");
        panelBotones.add(btnVerCarrito);

        JButton btnVolverInicio = new JButton("Volver al Inicio");
        panelBotones.add(btnVolverInicio);
        
        JButton btnRealizarCompra = new JButton("Realizar Compra");
        panelBotones.add(btnRealizarCompra);
        
        JButton btnMostrarFacturas = new JButton("Mostrar Facturas");
        panelBotones.add(btnMostrarFacturas);

        btnMostrarFacturas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFacturas();
            }
        });

        btnRealizarCompra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarCompra();
            }
        }); 

        btnAgregarCarrito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProductoAlCarrito();
            }
        });

        btnVerCarrito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarCarrito();
            }
        });

        btnVolverInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PantallaInicio.main(null);
                dispose();
            }
        });
    }

    private void cargarProductos() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos?serverTimezone=America/Santo_Domingo", "root", "12345");
            String sql = "SELECT id, nombre, precio, descripcion FROM productos";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                double precio = resultSet.getDouble("precio");
                String descripcion = resultSet.getString("descripcion");
                Producto producto = new Producto(id, nombre, precio, descripcion, null);
                Object[] row = {producto.getId(), producto.getNombre(), producto.getPrecio(), producto.getDescripcion()};
                tableModel.addRow(row);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos.");
        }
    }

    private void agregarProductoAlCarrito() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String nombre = (String) tableModel.getValueAt(selectedRow, 1);
            double precio = (double) tableModel.getValueAt(selectedRow, 2);
            String descripcion = (String) tableModel.getValueAt(selectedRow, 3);

            Producto producto = new Producto(id, nombre, precio, descripcion, null);
            carrito.add(producto);
            JOptionPane.showMessageDialog(this, "Producto agregado al carrito.");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para agregar al carrito.");
        }
    }

    private void mostrarCarrito() {
        StringBuilder sb = new StringBuilder();
        double total = 0;

        for (Producto producto : carrito) {
            sb.append(producto.getNombre()).append(" - $").append(producto.getPrecio()).append("\n");
            total += producto.getPrecio();
        }

        sb.append("\nTotal: $").append(total);
        JOptionPane.showMessageDialog(this, sb.toString(), "Carrito de Compras", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void realizarCompra() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío. Agrega productos antes de realizar la compra.");
            return;
        }

        try {
            // Establecer conexión con la base de datos
            Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tienda_productos?serverTimezone=America/Santo_Domingo",
                "root", "12345"
            );

            // Insertar nueva factura en la tabla Factura
            String insertFacturaSQL = "INSERT INTO Factura (fecha) VALUES (?)";
            PreparedStatement insertFacturaStatement = connection.prepareStatement(insertFacturaSQL, Statement.RETURN_GENERATED_KEYS);

            Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
            insertFacturaStatement.setTimestamp(1, fechaActual);
            insertFacturaStatement.executeUpdate();

            // Obtener el ID de la factura recién insertada
            ResultSet generatedKeys = insertFacturaStatement.getGeneratedKeys();
            int facturaId = 0;
            if (generatedKeys.next()) {
                facturaId = generatedKeys.getInt(1);
            }

            // Preparar la inserción de productos en la tabla Factura_Productos
            String insertFacturaProductosSQL = "INSERT INTO Factura_Productos (factura_id, productos_nombre, productos_precio) VALUES (?, ?, ?)";
            PreparedStatement insertProductosStatement = connection.prepareStatement(insertFacturaProductosSQL);

            double totalCompra = 0;

            // Recorrer el carrito e insertar los productos en Factura_Productos
            for (Producto producto : carrito) {
                insertProductosStatement.setInt(1, facturaId);          // factura_id
                insertProductosStatement.setString(2, producto.getNombre()); // productos_nombre
                insertProductosStatement.setDouble(3, producto.getPrecio()); // productos_precio
                insertProductosStatement.executeUpdate();

                totalCompra += producto.getPrecio();
            }

            // Solicitar el monto de pago al usuario
            double montoPago = 0;
            boolean pagoValido = false;

            while (!pagoValido) {
                String montoPagoStr = JOptionPane.showInputDialog(
                    this,
                    String.format("El total es %.2f. Por favor, introduce el monto de pago:", totalCompra)
                );

                try {
                    montoPago = Double.parseDouble(montoPagoStr);

                    if (montoPago >= totalCompra) {
                        pagoValido = true;
                    } else {
                        JOptionPane.showMessageDialog(this, "El monto ingresado no es suficiente para cubrir el total.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Por favor, introduce un monto válido.");
                }
            }

            // Calcular y mostrar la devuelta
            double devuelta = montoPago - totalCompra;
            JOptionPane.showMessageDialog(this, String.format("Compra realizada con éxito. Tu devuelta es: %.2f", devuelta));

            // Limpiar el carrito después de la compra
            carrito.clear();

            // Cerrar conexiones
            insertProductosStatement.close();
            insertFacturaStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al procesar la compra.");
        }
    }
    
    private void mostrarFacturas() {
        // Crear una nueva ventana para mostrar las facturas
        JFrame frameFacturas = new JFrame("Facturas Agrupadas");
        frameFacturas.setSize(800, 400);
        frameFacturas.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Modelo de la tabla
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID Factura");
        tableModel.addColumn("Fecha");
        tableModel.addColumn("Productos Comprados");
        tableModel.addColumn("Total");

        JTable tableFacturas = new JTable(tableModel);

        try {
            // Conexión con la base de datos
            Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tienda_productos?serverTimezone=America/Santo_Domingo", 
                "root", "12345"
            );

            // Consulta SQL para agrupar facturas
            String query = """
            	    SELECT 
    F.id AS 'ID Factura', 
    MAX(F.fecha) AS 'Fecha', 
    GROUP_CONCAT(FP.productos_nombre SEPARATOR ', ') AS 'Productos Comprados', 
    SUM(FP.productos_precio) AS 'Total'
FROM Factura F
JOIN Factura_Productos FP ON F.id = FP.factura_id
GROUP BY F.id
ORDER BY MAX(F.fecha) DESC;
            	""";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Agregar datos a la tabla
            while (resultSet.next()) {
                int idFactura = resultSet.getInt("ID Factura");
                String fecha = resultSet.getString("fecha");
                String productosComprados = resultSet.getString("Productos Comprados");
                double total = resultSet.getDouble("Total");
                
                tableModel.addRow(new Object[]{idFactura, fecha, productosComprados, total});
            }

            // Cerrar conexiones
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener las facturas.");
        }

        // Agregar la tabla a un JScrollPane y configurarlo en la ventana
        JScrollPane scrollPane = new JScrollPane(tableFacturas);
        frameFacturas.add(scrollPane);

        // Hacer visible la ventana
        frameFacturas.setVisible(true);
    }
}
