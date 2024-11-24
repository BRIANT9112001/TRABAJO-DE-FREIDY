package tienda.modelo;

//la interfaz que define los metodos que la clase de producto debe implementar
public interface IProducto {
    int getId();
    String getNombre();
    double getPrecio();
    String getDescripcion();
    String getImagenPath();
}
