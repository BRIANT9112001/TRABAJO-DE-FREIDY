package tienda.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class EliminarProducto extends JFrame {
    private JTextField txtIdProducto;

    public EliminarProducto() {
        setTitle("Eliminar Producto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 200);
        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Eliminar Producto", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(25, 25, 380, 100);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblIdProducto = new JLabel("Ingrese ID:");
        lblIdProducto.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblIdProducto.setBounds(10, 20, 100, 30);
        panel.add(lblIdProducto);

        txtIdProducto = new JTextField();
        txtIdProducto.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtIdProducto.setBounds(120, 20, 200, 30);
        panel.add(txtIdProducto);
        txtIdProducto.setColumns(10);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnEliminar.setBounds(120, 60, 100, 30);
        panel.add(btnEliminar);

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProducto();
            }
        });
    }

    private void eliminarProducto() {
        int idProducto = Integer.parseInt(txtIdProducto.getText());

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos?serverTimezone=America/Santo_Domingo", "root", "12345");
            String query = "DELETE FROM productos WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, idProducto);
            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el producto con el ID especificado.");
            }

            pst.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
