package modelofacturas;

import java.util.Date;

public abstract class FacturaBase implements IFacturas {
	protected int id;
	protected Date fecha;
	
	public FacturaBase(int id, Date fecha) {
        this.id = id;
        this.fecha = fecha;
    }
	
	@Override
    public int getId() {
        return id;
    }

    @Override
    public Date getFecha() {
        return fecha;
    }
}
