package tienda.modelo;

//Es una clase concreta que extiende la clase abstracta ProductoBase
public class Producto extends ProductoBase {

    // Constructor sin ID
    public Producto(String nombre, double precio, String descripcion, String imagenPath) {
        super(nombre, precio, descripcion, imagenPath);
    }

    // Constructor con ID
    public Producto(int id, String nombre, double precio, String descripcion, String imagenPath) {
        super(id, nombre, precio, descripcion, imagenPath);
    }
}
