package VistaCliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import VistaEmpleados.VistaListaEmpleados;
import GUI.PantallaInicio;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VistaCliente extends JFrame {
    private JTextField idField, nombreField, tipoContactoField, detalleContactoField;
    private JButton vistaListaButton, agregarButton, editarButton, borrarButton, buscarButton, volverButton;

    public VistaCliente() {
        setTitle("Gestión de Clientes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 600);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Panel de gestión de clientes
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Gestión de Clientes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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

        JLabel lblTipoContacto = new JLabel("Tipo Cont.:");
        lblTipoContacto.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTipoContacto.setBounds(20, 120, 120, 25);
        panel.add(lblTipoContacto);

        tipoContactoField = new JTextField();
        tipoContactoField.setBounds(120, 120, 200, 25);
        panel.add(tipoContactoField);
        tipoContactoField.setColumns(10);

        JLabel lblDetalleContacto = new JLabel("Detalle Cont.:");
        lblDetalleContacto.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblDetalleContacto.setBounds(20, 160, 150, 25);
        panel.add(lblDetalleContacto);

        detalleContactoField = new JTextField();
        detalleContactoField.setBounds(120, 160, 200, 25);
        panel.add(detalleContactoField);
        detalleContactoField.setColumns(10);

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

        vistaListaButton = new JButton("Lista de Clientes");
        vistaListaButton.setBounds(150, 500, 150, 30);
        contentPane.add(vistaListaButton);
        
        vistaListaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VistaListaCliente vistaLista = new VistaListaCliente();
                vistaLista.setVisible(true);
                dispose();
            }
        });

        volverButton = new JButton("Volver al Inicio");
        volverButton.setBounds(150, 450, 150, 30);
        contentPane.add(volverButton);

        // Acciones de los botones
        agregarButton.addActionListener(e -> agregarCliente());
        editarButton.addActionListener(e -> editarCliente());
        borrarButton.addActionListener(e -> borrarCliente());
        buscarButton.addActionListener(e -> buscarCliente());

        volverButton.addActionListener(e -> {
            PantallaInicio.main(null);
            dispose();
        });
    } // Fin del constructor


    private void agregarCliente() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos", "root", "12345")) {
            connection.setAutoCommit(false);

            // Insertar cliente
            String sqlCliente = "INSERT INTO clientes (nombre) VALUES (?)";
            PreparedStatement statementCliente = connection.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS);
            statementCliente.setString(1, nombreField.getText());
            statementCliente.executeUpdate();

            // Obtener ID generado
            ResultSet generatedKeys = statementCliente.getGeneratedKeys();
            int clienteId = 0;
            if (generatedKeys.next()) {
                clienteId = generatedKeys.getInt(1);
            }

            // Insertar contacto
            String sqlContacto = "INSERT INTO ContactosClientes (tipocontacto, detallecontacto, clientes_id) VALUES (?, ?, ?)";
            PreparedStatement statementContacto = connection.prepareStatement(sqlContacto);
            statementContacto.setString(1, tipoContactoField.getText());
            statementContacto.setString(2, detalleContactoField.getText());
            statementContacto.setInt(3, clienteId);
            statementContacto.executeUpdate();

            connection.commit();
            JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar cliente: " + ex.getMessage());
        }
    }

    private void editarCliente() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos", "root", "12345")) {
            connection.setAutoCommit(false);

            String sqlCliente = "UPDATE clientes SET nombre = ? WHERE id = ?";
            PreparedStatement statementCliente = connection.prepareStatement(sqlCliente);
            statementCliente.setString(1, nombreField.getText());
            statementCliente.setInt(2, Integer.parseInt(idField.getText()));
            statementCliente.executeUpdate();

            String sqlContacto = "UPDATE ContactosClientes SET tipocontacto = ?, detallecontacto = ? WHERE clientes_id = ?";
            PreparedStatement statementContacto = connection.prepareStatement(sqlContacto);
            statementContacto.setString(1, tipoContactoField.getText());
            statementContacto.setString(2, detalleContactoField.getText());
            statementContacto.setInt(3, Integer.parseInt(idField.getText()));
            statementContacto.executeUpdate();

            connection.commit();
            JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al editar cliente: " + ex.getMessage());
        }
    }

    private void borrarCliente() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos", "root", "12345")) {
            connection.setAutoCommit(false);

            String sqlContacto = "DELETE FROM ContactosClientes WHERE clientes_id = ?";
            PreparedStatement statementContacto = connection.prepareStatement(sqlContacto);
            statementContacto.setInt(1, Integer.parseInt(idField.getText()));
            statementContacto.executeUpdate();

            String sqlCliente = "DELETE FROM clientes WHERE id = ?";
            PreparedStatement statementCliente = connection.prepareStatement(sqlCliente);
            statementCliente.setInt(1, Integer.parseInt(idField.getText()));
            statementCliente.executeUpdate();

            connection.commit();
            JOptionPane.showMessageDialog(this, "Cliente borrado exitosamente.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al borrar cliente: " + ex.getMessage());
        }
    }

    private void buscarCliente() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos", "root", "12345")) {
            String sql = "SELECT clientes.nombre, ContactosClientes.tipocontacto, ContactosClientes.detallecontacto " +
                         "FROM clientes " +
                         "LEFT JOIN ContactosClientes ON clientes.id = ContactosClientes.clientes_id " +
                         "WHERE clientes.id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(idField.getText()));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nombreField.setText(resultSet.getString("nombre"));
                tipoContactoField.setText(resultSet.getString("tipocontacto"));
                detalleContactoField.setText(resultSet.getString("detallecontacto"));
            } else {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar cliente: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                VistaCliente frame = new VistaCliente();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}