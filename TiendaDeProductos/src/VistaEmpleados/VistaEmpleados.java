package VistaEmpleados;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import GUI.PantallaInicio;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VistaEmpleados extends JFrame {
    private JTextField idField, nombreField, emailField, posicionField, salarioField;
    private JButton vistaListaButton, agregarButton, editarButton, borrarButton, buscarButton, volverButton;

    public VistaEmpleados() {
        setTitle("Gestión de Empleados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 600);
        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Panel de gestión de empleados
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Gestión de Empleados", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(30, 30, 420, 400);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblId = new JLabel("ID:");
        lblId.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblId.setBounds(20, 40, 80, 25);
        panel.add(lblId);

        idField = new JTextField();
        idField.setBounds(120, 40, 200, 25);
        panel.add(idField);
        idField.setColumns(10);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNombre.setBounds(20, 80, 80, 25);
        panel.add(lblNombre);

        nombreField = new JTextField();
        nombreField.setBounds(120, 80, 200, 25);
        panel.add(nombreField);
        nombreField.setColumns(10);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblEmail.setBounds(20, 120, 80, 25);
        panel.add(lblEmail);

        emailField = new JTextField();
        emailField.setBounds(120, 120, 200, 25);
        panel.add(emailField);
        emailField.setColumns(10);

        JLabel lblPosicion = new JLabel("Posición:");
        lblPosicion.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPosicion.setBounds(20, 160, 80, 25);
        panel.add(lblPosicion);

        posicionField = new JTextField();
        posicionField.setBounds(120, 160, 200, 25);
        panel.add(posicionField);
        posicionField.setColumns(10);

        JLabel lblSalario = new JLabel("Salario:");
        lblSalario.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblSalario.setBounds(20, 200, 80, 25);
        panel.add(lblSalario);

        salarioField = new JTextField();
        salarioField.setBounds(120, 200, 200, 25);
        panel.add(salarioField);
        salarioField.setColumns(10);

        // Botones
        agregarButton = new JButton("Agregar");
        agregarButton.setBounds(20, 250, 120, 30);
        panel.add(agregarButton);

        editarButton = new JButton("Editar");
        editarButton.setBounds(150, 250, 120, 30);
        panel.add(editarButton);

        borrarButton = new JButton("Borrar");
        borrarButton.setBounds(280, 250, 120, 30);
        panel.add(borrarButton);

        buscarButton = new JButton("Buscar");
        buscarButton.setBounds(150, 300, 120, 30);
        panel.add(buscarButton);
        
        vistaListaButton = new JButton("Lista de Empleados");
        vistaListaButton.setBounds(150, 500, 150, 30);
        contentPane.add(vistaListaButton);
        
     // Acción del botón "Ver Lista de Empleados"
        vistaListaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VistaListaEmpleados vistaLista = new VistaListaEmpleados();
                vistaLista.setVisible(true);
                dispose();
            }
        });


        // Botón volver al inicio
        volverButton = new JButton("Volver al Inicio");
        volverButton.setBounds(150, 450, 150, 30);
        contentPane.add(volverButton);

        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Código para volver al inicio
                PantallaInicio.main(null);
                dispose();
            }
        });

        // Acciones de los botones
        agregarButton.addActionListener(e -> agregarEmpleado());
        editarButton.addActionListener(e -> editarEmpleado());
        borrarButton.addActionListener(e -> borrarEmpleado());
        buscarButton.addActionListener(e -> buscarEmpleado());
    }

    // Función para agregar un empleado
    private void agregarEmpleado() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos", "root", "12345")) {
            connection.setAutoCommit(false); // Usar una transacción para garantizar consistencia

            // Insertar empleado
            String sqlEmpleado = "INSERT INTO Empleados (Nombre, Posicion, Salario) VALUES (?, ?, ?)";
            PreparedStatement statementEmpleado = connection.prepareStatement(sqlEmpleado, Statement.RETURN_GENERATED_KEYS);
            statementEmpleado.setString(1, nombreField.getText());
            statementEmpleado.setString(2, posicionField.getText());
            statementEmpleado.setString(3, salarioField.getText());
            statementEmpleado.executeUpdate();

            // Obtener el ID generado del empleado
            ResultSet generatedKeys = statementEmpleado.getGeneratedKeys();
            int empleadoId = 0;
            if (generatedKeys.next()) {
                empleadoId = generatedKeys.getInt(1);
            }

            // Insertar email del empleado
            String sqlEmail = "INSERT INTO ContactosEmpleados (email, Empleados_id) VALUES (?, ?)";
            PreparedStatement statementEmail = connection.prepareStatement(sqlEmail);
            statementEmail.setString(1, emailField.getText());
            statementEmail.setInt(2, empleadoId);
            statementEmail.executeUpdate();

            connection.commit(); // Confirmar la transacción
            JOptionPane.showMessageDialog(this, "Empleado agregado exitosamente.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar empleado: " + ex.getMessage());
        }
    }

    // Función para editar un empleado
    private void editarEmpleado() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos", "root", "12345")) {
            connection.setAutoCommit(false); // Usar transacción

            // Actualizar los datos en la tabla Empleados
            String sqlEmpleado = "UPDATE Empleados SET Nombre = ?, Posicion = ?, Salario = ? WHERE id = ?";
            PreparedStatement statementEmpleado = connection.prepareStatement(sqlEmpleado);
            statementEmpleado.setString(1, nombreField.getText());
            statementEmpleado.setString(2, posicionField.getText());
            statementEmpleado.setString(3, salarioField.getText());
            statementEmpleado.setInt(4, Integer.parseInt(idField.getText()));
            int rowsUpdatedEmpleado = statementEmpleado.executeUpdate();

            // Actualizar el email en la tabla ContactosEmpleados
            String sqlEmail = "UPDATE ContactosEmpleados SET email = ? WHERE Empleados_id = ?";
            PreparedStatement statementEmail = connection.prepareStatement(sqlEmail);
            statementEmail.setString(1, emailField.getText());
            statementEmail.setInt(2, Integer.parseInt(idField.getText()));
            int rowsUpdatedEmail = statementEmail.executeUpdate();

            connection.commit(); // Confirmar transacción

            if (rowsUpdatedEmpleado > 0 || rowsUpdatedEmail > 0) {
                JOptionPane.showMessageDialog(this, "Empleado actualizado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Empleado no encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al editar empleado: " + ex.getMessage());
        }
    }

    // Función para borrar un empleado
    private void borrarEmpleado() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos", "root", "12345")) {
            connection.setAutoCommit(false); // Usar transacción

            // Borrar el email del empleado en ContactosEmpleados
            String sqlEmail = "DELETE FROM ContactosEmpleados WHERE Empleados_id = ?";
            PreparedStatement statementEmail = connection.prepareStatement(sqlEmail);
            statementEmail.setInt(1, Integer.parseInt(idField.getText()));
            statementEmail.executeUpdate();

            // Borrar el registro del empleado en Empleados
            String sqlEmpleado = "DELETE FROM Empleados WHERE id = ?";
            PreparedStatement statementEmpleado = connection.prepareStatement(sqlEmpleado);
            statementEmpleado.setInt(1, Integer.parseInt(idField.getText()));
            int rowsDeleted = statementEmpleado.executeUpdate();

            connection.commit(); // Confirmar transacción

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Empleado borrado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Empleado no encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al borrar empleado: " + ex.getMessage());
        }
    }

    // Función para buscar un empleado
    private void buscarEmpleado() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos", "root", "12345")) {
            // Consulta para obtener el empleado y su email
            String sql = "SELECT Empleados.Nombre, Empleados.Posicion, Empleados.Salario, ContactosEmpleados.email " +
                         "FROM Empleados " +
                         "LEFT JOIN ContactosEmpleados ON Empleados.id = ContactosEmpleados.Empleados_id " +
                         "WHERE Empleados.id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(idField.getText()));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nombreField.setText(resultSet.getString("Nombre"));        // Campo Nombre
                posicionField.setText(resultSet.getString("Posicion"));    // Campo Posición
                salarioField.setText(resultSet.getString("Salario"));      // Campo Salario
                emailField.setText(resultSet.getString("email"));          // Campo Email
            } else {
                JOptionPane.showMessageDialog(this, "Empleado no encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar empleado: " + ex.getMessage());
        }
    }
}