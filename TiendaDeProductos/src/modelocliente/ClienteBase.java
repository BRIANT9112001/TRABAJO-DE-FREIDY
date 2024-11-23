package modelocliente;


public abstract class ClienteBase implements ICliente {
	    protected int id;
	    protected String nombre;
	    protected String tipocontacto;
	    protected String detallecontacto;

    public ClienteBase(int id, String nombre, String tipocontacto, String detallecontacto) {
        this.id = id;
        this.nombre = nombre;
        this.tipocontacto = tipocontacto;
        this.detallecontacto = detallecontacto;
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
    public String getDetallecontacto() {
        return detallecontacto;
    }
    
    @Override
    public String getTipocontacto() {
    	return tipocontacto;
    }

}