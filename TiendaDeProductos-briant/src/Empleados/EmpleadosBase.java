package Empleados;

public abstract class EmpleadosBase implements IEmpleados {
	    protected int id;
	    protected String nombre;
	    protected String email;
	    protected String posicion;
	    protected String salario;
	    
	    public EmpleadosBase(int id, String nombre, String email, String posicion, String salario) {
	        this.id = id;
	        this.nombre = nombre;
	        this.email = email;
	        this.posicion = posicion;
	        this.salario = salario;
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
	    public String getEmail() {
	        return email;
	    }
	    
	    @Override
	    public String getPosicion() {
	    	return posicion;
	    }
	    
	    @Override
	    public String getSalario() {
	    	return salario;
	    }
}
