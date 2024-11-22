package tienda.modelo;


//Clase padre de los productos, para que cualquier tipo de producto tenga los atributos y métodos 
public abstract class ProductoBase implements IProducto {
    protected int id;
    protected String nombre;
    protected double precio;
    protected String descripcion;
    protected String imagenPath;
    
 // Constructor con ID y sin ImagenPath
    public ProductoBase(int id, String nombre, double precio, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    // Constructor sin ID
    public ProductoBase(String nombre, double precio, String descripcion, String imagenPath) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagenPath = imagenPath;
    }

    // Constructor con ID
    public ProductoBase(int id, String nombre, double precio, String descripcion, String imagenPath) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagenPath = imagenPath;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public double getPrecio() {
        return precio;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String getImagenPath() {
        return imagenPath;
    }

    @Override
    public String toString() {
        return nombre + " - $" + precio; // Personaliza esto según lo que quieras mostrar
    }
}
