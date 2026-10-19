package Tienda;
public abstract class Producto {
	
	private String nombre;
	private double precioBase;
	private int cantidadInventario;
	
	public Producto(String nombre, double precioBase, int cantidadInventario) {
		this.nombre = nombre;
		this.precioBase = precioBase;
		this.cantidadInventario = cantidadInventario;
	}
	public double getPrecioBase() {
		return precioBase;
	}
	public void setPrecioBase(double precioBase) {
		this.precioBase = precioBase;
	}
	public int getCantidadInventario() {
		return cantidadInventario;
	}
	public void setCantidadInventario(int cantidadInventario) {
		this.cantidadInventario = cantidadInventario;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void agregarProducto(int cantidad) {
		this.cantidadInventario =+ cantidad;
	}
	public void quitarProducto(int cantidad) {
		this.cantidadInventario =- cantidad;
	}
}
