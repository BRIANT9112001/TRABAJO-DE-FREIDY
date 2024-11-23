package VistaEmpleados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import GUI.PantallaInicio;
import java.awt.*;
import java.sql.*;

public class VistaListaEmpleados extends JFrame {
    private JTable tablaEmpleados;
    private JButton volverButton, vistaEmpleadosButton;

    public VistaListaEmpleados() {
        setTitle("Lista de Empleados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 400); // Ajuste del ancho para incluir la nueva columna
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Crear tabla con nueva columna para email
        tablaEmpleados = new JTable();
        tablaEmpleados.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nombre", "Posición", "Salario", "Email"} // Se añadió "Email"
        ));
        JScrollPane scrollPane = new JScrollPane(tablaEmpleados);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        volverButton = new JButton("Volver al Inicio");
        vistaEmpleadosButton = new JButton("Gestión de Empleados");
        panelBotones.add(vistaEmpleadosButton);
        panelBotones.add(volverButton);
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        // Acción del botón "Volver al Inicio"
        volverButton.addActionListener(e -> {
            PantallaInicio.main(null);
            dispose();
        });

        // Acción del botón "Gestión de Empleados"
        vistaEmpleadosButton.addActionListener(e -> {
            VistaEmpleados vistaEmpleados = new VistaEmpleados();
            vistaEmpleados.setVisible(true);
            dispose();
        });

        // Cargar empleados al iniciar la vista
        cargarEmpleados();
    }

    // Método para cargar empleados desde la base de datos
    private void cargarEmpleados() {
        DefaultTableModel modelo = (DefaultTableModel) tablaEmpleados.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de cargar datos

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos", "root", "12345")) {
            String sql = "SELECT Empleados.id, Empleados.Nombre, Empleados.Posicion, Empleados.Salario, ContactosEmpleados.email " +
                         "FROM Empleados " +
                         "LEFT JOIN ContactosEmpleados ON Empleados.id = ContactosEmpleados.Empleados_id";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("Nombre");
                String posicion = resultSet.getString("Posicion");
                String salario = resultSet.getString("Salario");
                String email = resultSet.getString("email");

                // Manejo de casos donde el email puede ser NULL
                if (email == null) {
                    email = "No registrado";
                }

                modelo.addRow(new Object[]{id, nombre, posicion, salario, email});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar los empleados: " + ex.getMessage());
        }
    }
}