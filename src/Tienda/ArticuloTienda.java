package Tienda;

public class ArticuloTienda extends Producto {
	
	static String TIPO ="ArticuloTienda";
	private String categoria;
	
	public ArticuloTienda(String nombre, double precioBase, int cantidadInventario, String categoria) {
		super(nombre, precioBase, cantidadInventario);
		this.categoria = categoria;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
}
