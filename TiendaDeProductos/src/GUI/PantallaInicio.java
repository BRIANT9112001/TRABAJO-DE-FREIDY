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
    	
    	//importar la libreria

    	try {

    	for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

    	if ("Nimbus".equals(info.getName())) {

    	UIManager.setLookAndFeel(info.getClassName());

    	break;

    	}

    	}

    	} catch (Exception e) {

    	// Si Nimbus no está disponible, puedes establecer otro Look and Feel.

    	try {

    	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

    	} catch (Exception ex) {

    	// Manejo de excepción

    	}

    	}
    	
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
        setTitle("Tienda Bravo");
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
        ImageIcon img = new ImageIcon("C:\\Users\\Usuario\\Desktop\\Uni\\Programacion 1\\TiendaDeProductos\\src\\IMG\\bravo.png"); // Ruta a la imagen
        Image imgEscalada = img.getImage().getScaledInstance(760, 400, Image.SCALE_SMOOTH); // Escala la imagen
        imagenLabel.setIcon(new ImageIcon(imgEscalada));
        contentPane.add(imagenLabel);

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
        lblInfo.setFont(new Font("Serif", Font.PLAIN, 25));
        lblInfo.setBounds(224, 10, 400, 30);
        contentPane.add(lblInfo);
    }
}
