package VistaCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import GUI.PantallaInicio;
import java.awt.*;
import java.sql.*;

public class VistaListaCliente extends JFrame {
    private JTable tablaClientes;
    private JButton volverButton, vistaClientesButton;

    public VistaListaCliente() {
        setTitle("Lista de Clientes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 400);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Crear tabla con columnas para información de clientes
        tablaClientes = new JTable();
        tablaClientes.setModel(new DefaultTableModel(
        	    new Object[][]{},
        	    new String[]{"ID", "Nombre", "Tipo Cont.", "Detalle Cont."} // Actualizado según los datos
        	));
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        volverButton = new JButton("Volver al Inicio");
        vistaClientesButton = new JButton("Gestión de Clientes");
        panelBotones.add(vistaClientesButton);
        panelBotones.add(volverButton);
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        // Acción del botón "Volver al Inicio"
        volverButton.addActionListener(e -> {
        	 PantallaInicio.main(null);
             dispose();
        });

        // Acción del botón "Gestión de Clientes"
        vistaClientesButton.addActionListener(e -> {
            VistaCliente vistaCliente = new VistaCliente(); // Ajusta si `VistaCliente` necesita parámetros
            vistaCliente.setVisible(true);
            dispose();
        });

        // Cargar clientes al iniciar la vista
        cargarClientes();
    }

    // Método para cargar clientes desde la base de datos
    private void cargarClientes() {
        DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de cargar datos

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos", "root", "12345")) {
            String sql = "SELECT Clientes.id, Clientes.nombre, ContactosClientes.tipocontacto, ContactosClientes.detallecontacto " +
                         "FROM Clientes " +
                         "LEFT JOIN ContactosClientes ON Clientes.id = ContactosClientes.clientes_id";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String tipoContacto = resultSet.getString("tipocontacto");
                String detalleContacto = resultSet.getString("detallecontacto");

                // Manejo de casos donde no haya contacto asociado
                if (tipoContacto == null) tipoContacto = "No registrado";
                if (detalleContacto == null) detalleContacto = "No registrado";

                modelo.addRow(new Object[]{id, nombre, tipoContacto, detalleContacto});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar los clientes: " + ex.getMessage());
        }
    }
}