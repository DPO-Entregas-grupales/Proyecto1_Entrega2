package Tienda;
import java.util.ArrayList;
import java.util.List;

public class ProductoCafeteria extends Producto {
	
	static String Tipo="ProductoCafeteria";
	private boolean esBebidaCaliente;
	private List<String> alergenos;
	
	public ProductoCafeteria(String nombre, double precioBase, int cantidadInventario, boolean esBebidaCaliente) {
		super(nombre, precioBase, cantidadInventario);
		this.esBebidaCaliente = esBebidaCaliente;
		this.alergenos = new ArrayList<>();	
	}
	
	public boolean isEsBebidaCaliente() {
		return esBebidaCaliente;
	}

	public void setEsBebidaCaliente(boolean esBebidaCaliente) {
		this.esBebidaCaliente = esBebidaCaliente;
	}

	public void agregarAlergeno(String alergeno) {
		this.alergenos.add(alergeno);
	}
	public List<String> getAlergenos(){
		return alergenos;
	}
	
}
