package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import VistasCliente.Compras;
import VistasCliente.ExploradorDeProductos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import tienda.modelo.Producto;
import tienda.vistas.VistaAdministrador;

import java.util.ArrayList;

public class PantallaInicio extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ArrayList<Producto> productos = new ArrayList<>();
                    PantallaInicio frame = new PantallaInicio(productos);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public PantallaInicio(ArrayList<Producto> productos) {		
        setTitle("Tienda de Productos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);

        //  la barra de menú
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Menú de Inicio
        JMenu mnInicio = new JMenu("Inicio");
        menuBar.add(mnInicio);
        
        // Menú de Acciones
        JMenu mnAcciones = new JMenu("Tienda");
        menuBar.add(mnAcciones);
        
        
        // Acción de Compras
        JMenuItem mntmCompras = new JMenuItem("Comprar Productos");
        mntmCompras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Compras(productos).setVisible(true);
                dispose();
            }	
        });
        mnAcciones.add(mntmCompras);

        // Contenido principal
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Panel para la imagen de inicio
        JLabel imagenLabel = new JLabel("");
        imagenLabel.setBounds(10, 50, 760, 400);
        //ImageIcon img = new ImageIcon(PantallaInicio.class.getResource("/IMG/tiendapro.png")); // Ruta a la imagen
        //Image imge = img.getImage().getScaledInstance(760, 400, Image.SCALE_SMOOTH);
        //imagenLabel.setIcon(new ImageIcon(imge));
        //contentPane.add(imagenLabel);

        JButton btnAdmin = new JButton("Administrador");
        btnAdmin.setBounds(205, 470, 150, 40);
        btnAdmin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VistaAdministrador(productos, new ExploradorDeProductos(productos)).setVisible(true);
                dispose();
            }
        });
        contentPane.add(btnAdmin);

        JButton btnUsuario = new JButton("Inventario");
        btnUsuario.setBounds(437, 470, 150, 40);
        btnUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ExploradorDeProductos(productos).setVisible(true);
                dispose();
            }
        });
        contentPane.add(btnUsuario);

        JLabel lblInfo = new JLabel("Bienvenido a Supermercados Bravo");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblInfo.setBounds(224, 10, 400, 30);
        contentPane.add(lblInfo);
    }
}
