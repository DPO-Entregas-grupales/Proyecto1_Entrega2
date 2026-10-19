package Tienda;

public class ItemVenta {
	
	private Producto producto;
	private int cantidad;
	
	public ItemVenta(int cantidad, Producto producto) {
		this.cantidad = cantidad;
		this.producto = producto;
	}
	
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double precioItemVenta() {
		int cantidad = getCantidad();
		double precioProducto = producto.getPrecioBase();
		return cantidad*precioProducto;
	}
	public void quitarProductoInventario() {
        if (this.producto != null) {
            int inventarioActual = this.producto.getCantidadInventario();
            int nuevoInventario = Math.max(0, inventarioActual - this.cantidad);
            this.producto.setCantidadInventario(nuevoInventario);
        }
    }
}
