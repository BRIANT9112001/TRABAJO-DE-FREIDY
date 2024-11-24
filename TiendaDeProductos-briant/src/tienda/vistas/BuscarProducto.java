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
import java.sql.ResultSet;

public class BuscarProducto extends JFrame {
    private JTextField txtBuscar;
    private JTextArea txtResultado;

    public BuscarProducto() {
        setTitle("Buscar Producto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 400);
        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 0, 0));
        contentPane.setForeground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(new TitledBorder(null, "Buscar Producto", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(25, 25, 380, 100);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblBuscar = new JLabel("Ingrese nombre:");
        lblBuscar.setForeground(new Color(0, 0, 0));
        lblBuscar.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblBuscar.setBounds(10, 20, 140, 30);
        panel.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtBuscar.setBounds(160, 20, 200, 30);
        panel.add(txtBuscar);
        txtBuscar.setColumns(10);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setForeground(new Color(0, 0, 0));
        btnBuscar.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnBuscar.setBounds(160, 60, 100, 30);
        panel.add(btnBuscar);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(25, 150, 380, 200);
        contentPane.add(scrollPane);

        txtResultado = new JTextArea();
        txtResultado.setForeground(new Color(0, 0, 0));
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtResultado.setEditable(false);
        scrollPane.setViewportView(txtResultado);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProducto();
            }
        });
    }

    private void buscarProducto() {
        String nombreProducto = txtBuscar.getText();
        String resultado = "";

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_productos?serverTimezone=America/Santo_Domingo", "root", "12345");
            String query = "SELECT * FROM productos WHERE nombre = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, nombreProducto);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                resultado += "ID: " + rs.getInt("id") + "\n";
                resultado += "Nombre: " + rs.getString("nombre") + "\n";
                resultado += "Precio: " + rs.getDouble("precio") + "\n";
                resultado += "Descripción: " + rs.getString("descripcion") + "\n";
                resultado += "---------------------------\n";
            }

            txtResultado.setText(resultado);
            rs.close();
            pst.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
