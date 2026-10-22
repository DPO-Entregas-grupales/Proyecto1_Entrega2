package Modelo;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import Usuarios.Usuario;
import Usuarios.Socio;
import Tienda.ArticuloTienda;
import Tienda.ProductoCafeteria;
import Tienda.Producto;
import Tienda.Venta;
import Tienda.ItemVenta;
import Tienda.CalculadoraDescuento;
import Persistencia.CentralPersistencia;

public class Tienda {
    private Map<String, ArticuloTienda> articulosTienda;
    private Map<String, ProductoCafeteria> productosCafeteria;
    private List<Venta> ventas;
    private CalculadoraDescuento calculadoraDescuento;

    public Tienda() {
        this.articulosTienda = new HashMap<>();
        this.productosCafeteria = new HashMap<>();
        this.ventas = new ArrayList<>();
        this.calculadoraDescuento = new CalculadoraDescuento();
    }

    // MÉTODOS PRIVADOS DE AGREGACIÓN INTERNA
    private void agregarArticuloTienda(ArticuloTienda articuloTienda) {
        if (articuloTienda != null && articuloTienda.getNombre() != null) {
            this.articulosTienda.put(articuloTienda.getNombre(), articuloTienda);
        }
    }

    private void agregarProductoCafeteria(ProductoCafeteria productoCafeteria) {
        if (productoCafeteria != null && productoCafeteria.getNombre() != null) {
            this.productosCafeteria.put(productoCafeteria.getNombre(), productoCafeteria);
        }
    }

    private void agregarVenta(Venta venta) {
        if (venta != null) {
            this.ventas.add(venta);
        }
    }

    public void crearArticuloTienda(String nombre, Double precioBase, int cantidadInventario, String categoria) {
        ArticuloTienda articulo = new ArticuloTienda(nombre, precioBase, cantidadInventario, categoria);
        agregarArticuloTienda(articulo);
    }

    public void crearProductoCafeteria(String nombre, Double precioBase, int cantidadInventario, Boolean esBebidaCaliente, String alergenos) {
        ProductoCafeteria producto = new ProductoCafeteria(nombre, precioBase, cantidadInventario, esBebidaCaliente);
        if (alergenos != null && !alergenos.trim().isEmpty()) {
            producto.agregarAlergeno(alergenos);
        }
        agregarProductoCafeteria(producto);
    }

    // Valida y arma la venta sin modificar nada. Lanza IllegalArgumentException con el motivo si no se puede por alguna razon.
    private Venta armarVenta(String idVenta, String fecha, int hora, String idUsuario, String codigoDescuento,
                             List<ItemVenta> itemsVenta, ClubDeportivoSeneca clubDeportivo, String tipo) {
        Usuario usuario = null;
        if (clubDeportivo != null && clubDeportivo.getUsuarios() != null) {
            usuario = clubDeportivo.getUsuarios().get(idUsuario);
        }
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        if (itemsVenta == null || itemsVenta.isEmpty()) {
            throw new IllegalArgumentException("La venta no tiene productos");
        }
        for (ItemVenta item : itemsVenta) {
            if (item == null || item.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad de cada producto debe ser mayor a cero");
            }
        }
        boolean esCafeteria = Venta.CAFETERIA.equalsIgnoreCase(tipo);
        
        if (esCafeteria && usuario instanceof Socio && ((Socio) usuario).tieneReservaActiva()) {
            for (ItemVenta item : itemsVenta) {
                if (item.getProducto() instanceof ProductoCafeteria
                        && ((ProductoCafeteria) item.getProducto()).isEsBebidaCaliente()) {
                    throw new IllegalArgumentException("No se venden bebidas calientes a un socio con reserva activa en una cancha techada de pádel o tenis de mesa");
                }
            }
        }
        
        Map<String, Producto> disponibles = new HashMap<>();
        if (esCafeteria) {
            disponibles.putAll(productosCafeteria);
        } else {
            disponibles.putAll(articulosTienda);
        }
        boolean hayInventario;
        if (esCafeteria) {
            hayInventario = sePuedeRealizarVentaCafeteria(itemsVenta, disponibles);
        } else {
            hayInventario = sePuedeRealizarVentaTiendaDeportiva(itemsVenta, disponibles);
        }
        if (!hayInventario) {
            throw new IllegalArgumentException("No hay unidades suficientes de uno de los productos");
        }
        Venta venta = new Venta(idVenta, fecha, hora, tipo, usuario);
        venta.setItemsVenta(itemsVenta);
        if (esCafeteria) {
            venta.realizarCalculosVentaCafeteria(itemsVenta);
        } else {
            venta.realizarCalculosVentaTiendaDeportiva(itemsVenta, codigoDescuento, this.calculadoraDescuento);
        }
        return venta;
    }

    private void registrarVenta(Venta venta) {
        Usuario usuario = venta.getUsuario();
        usuario.sumarPuntos((int) venta.getPuntosGanados());
        usuario.agregarVenta(venta);
        agregarVenta(venta);
        for (ItemVenta item : venta.getItemsVenta()) {
            item.quitarProductoInventario();
        }
    }

    public void realizarVenta(String idVenta, String fecha, int hora, Double impuesto, String idUsuario, String codigoDescuento, List<ItemVenta> itemsVenta, ClubDeportivoSeneca clubDeportivo, String tipo) {
        Venta venta = armarVenta(idVenta, fecha, hora, idUsuario, codigoDescuento, itemsVenta, clubDeportivo, tipo);
        registrarVenta(venta);
    }

    public void realizarVentaConPuntos(String idVenta, String fecha, int hora, String idUsuario, String codigoDescuento, List<ItemVenta> itemsVenta, ClubDeportivoSeneca clubDeportivo, String tipo, int puntosARedimir) {
        Venta venta = armarVenta(idVenta, fecha, hora, idUsuario, codigoDescuento, itemsVenta, clubDeportivo, tipo);
        Usuario usuario = venta.getUsuario();
        if (puntosARedimir <= 0) {
            throw new IllegalArgumentException("Los puntos a redimir deben ser mayores a cero");
        }
        if (puntosARedimir > usuario.getPuntosFidelidad()) {
            throw new IllegalArgumentException("No tiene puntos suficientes");
        }
        if (puntosARedimir > venta.getTotal()) {
            throw new IllegalArgumentException("Los puntos a redimir superan el total de la venta");
        }
        venta.restarPuntosAlTotal(puntosARedimir);
        usuario.restarPuntos(puntosARedimir);
        registrarVenta(venta);
    }

    public String generarIdVenta() {
        int max = 0;
        for (Venta venta : ventas) {
            String digitos = venta.getIdVenta().replaceAll("[^0-9]", "");
            if (!digitos.isEmpty()) {
                max = Math.max(max, Integer.parseInt(digitos));
            }
        }
        return "V-" + (max + 1);
    }

    public void reabastecer(String nombreProducto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        Producto producto = articulosTienda.get(nombreProducto);
        if (producto == null) {
            producto = productosCafeteria.get(nombreProducto);
        }
        if (producto == null) {
            throw new IllegalArgumentException("El producto no existe");
        }
        producto.agregarProducto(cantidad);
    }

    public void crearVenta(String idVenta, String fecha, int hora, String idUsuario, String codigoDescuento, List<ItemVenta> itemsVenta, ClubDeportivoSeneca clubDeportivo, String tipo) {
        Usuario usuario = null;
        if (clubDeportivo != null && clubDeportivo.getUsuarios() != null) {
            usuario = clubDeportivo.getUsuarios().get(idUsuario);
        }

        Venta venta = new Venta(idVenta, fecha, hora, tipo, usuario);
        venta.setItemsVenta(itemsVenta);

        agregarVenta(venta);

        if (usuario != null) {
            usuario.agregarVenta(venta);
        }
    }

    // REQUISITOS FUNCIONALES
    public Boolean sePuedeRealizarVentaCafeteria(List<ItemVenta> itemsVenta, Map<String, Producto> productosDisponibles) {
        if (itemsVenta == null || productosDisponibles == null) {
            return false;
        }
        for (ItemVenta item : itemsVenta) {
            if (item == null || item.getProducto() == null) {
                return false;
            }
            Producto disponible = productosDisponibles.get(item.getProducto().getNombre());
            if (disponible == null || disponible.getCantidadInventario() < item.getCantidad()) {
                return false;
            }
        }
        return true;
    }

    public Boolean sePuedeRealizarVentaTiendaDeportiva(List<ItemVenta> itemsVenta, Map<String, Producto> productosDisponibles) {
        if (itemsVenta == null || productosDisponibles == null) {
            return false;
        }
        for (ItemVenta item : itemsVenta) {
            if (item == null || item.getProducto() == null) {
                return false;
            }
            Producto disponible = productosDisponibles.get(item.getProducto().getNombre());
            if (disponible == null || disponible.getCantidadInventario() < item.getCantidad()) {
                return false;
            }
        }
        return true;
    }

    public Map<String, ProductoCafeteria> obtenerProductosCafeteria() {
        return productosCafeteria;
    }

    public Map<String, ArticuloTienda> obtenerArticulosTienda() {
        return articulosTienda;
    }

    public List<String> reporteVentas(String fechaInicio, String fechaFin) {
        List<String> lineas = new ArrayList<>();
        double totalTienda = 0;
        double totalCafeteria = 0;
        int ventasTienda = 0;
        int ventasCafeteria = 0;
        for (Venta venta : reporteVentasFechasYTipo(fechaInicio, fechaFin, "")) {
            String nombre = "sin usuario";
            if (venta.getUsuario() != null) {
                nombre = venta.getUsuario().getNombre();
            }
            lineas.add(venta.getIdVenta() + " / " + venta.getFecha() + " " + venta.getHora() + "h / " + venta.getTipo()
                    + " / " + nombre + " / total: " + venta.getTotal());
            if (Venta.CAFETERIA.equalsIgnoreCase(venta.getTipo())) {
                totalCafeteria += venta.getTotal();
                ventasCafeteria++;
            } else {
                totalTienda += venta.getTotal();
                ventasTienda++;
            }
        }
        lineas.add("Tienda deportiva: " + ventasTienda + " ventas, total " + totalTienda);
        lineas.add("Cafetería: " + ventasCafeteria + " ventas, total " + totalCafeteria);
        lineas.add("Total general: " + (totalTienda + totalCafeteria));
        return lineas;
    }

    public List<String> reporteInventarioProductos() {
        List<String> lineas = new ArrayList<>();
        for (ArticuloTienda articulo : articulosTienda.values()) {
            lineas.add("Tienda / " + articulo.getNombre() + " / " + articulo.getCategoria()
                    + " / $" + articulo.getPrecioBase() + " / stock: " + articulo.getCantidadInventario());
        }
        for (ProductoCafeteria producto : productosCafeteria.values()) {
            String tipo = "fría";
            if (producto.isEsBebidaCaliente()) {
                tipo = "caliente";
            }
            lineas.add("Cafetería / " + producto.getNombre() + " / bebida " + tipo
                    + " / $" + producto.getPrecioBase() + " / stock: " + producto.getCantidadInventario());
        }
        return lineas;
    }
    
    public List<Venta> reporteVentasFechasYTipo(String fechaInicio, String fechaFin, String tipoVenta) {
        List<Venta> resultado = new ArrayList<>();
        for (Venta venta : ventas) {
            boolean cumpleFecha = true;

            if (venta.getFecha() != null) {
                if (fechaInicio != null && !fechaInicio.isEmpty() && venta.getFecha().compareTo(fechaInicio) < 0) {
                    cumpleFecha = false;
                }
                if (fechaFin != null && !fechaFin.isEmpty() && venta.getFecha().compareTo(fechaFin) > 0) {
                    cumpleFecha = false;
                }
            }
            
            if (tipoVenta != null && !tipoVenta.isEmpty() && !tipoVenta.equalsIgnoreCase(venta.getTipo())) {
                cumpleFecha = false;
            }
            
            if (cumpleFecha) {
                resultado.add(venta);
            }
        }
        return resultado;
    }

    public List<String> informarAlergenos(Producto producto) {
        if (producto instanceof ProductoCafeteria) {
            return ((ProductoCafeteria) producto).getAlergenos();
        }
        return new ArrayList<>();
    }

    // PERSISTENCIA Y GETTERS
    public void cargarTienda(String archivo) {
        try {
            CentralPersistencia.getPersistenciaTienda().cargarTienda(this, archivo);
        } catch (Exception e) {
            System.err.println("Error al cargar la tienda: " + e.getMessage());
        }
    }
    
    //Esto necesita que los usuarios ya esten cargados en el club.
    public void cargarTienda(String archivo, ClubDeportivoSeneca club) {
        try {
            CentralPersistencia.getPersistenciaTienda().cargarTienda(this, archivo, club);
        } catch (Exception e) {
            System.err.println("Error al cargar la tienda: " + e.getMessage());
        }
    }
    
    public void salvarTienda(String archivo) {
        try {
            CentralPersistencia.getPersistenciaTienda().salvarTienda(this, archivo);
        } catch (Exception e) {
            System.err.println("Error al salvar la tienda: " + e.getMessage());
        }
    }

    public Map<String, ArticuloTienda> getArticulosTienda() {
        return articulosTienda;
    }

    public Map<String, ProductoCafeteria> getProductosCafeteria() {
        return productosCafeteria;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public CalculadoraDescuento getCalculadoraDescuento() {
        return calculadoraDescuento;
    }

    public void setCalculadoraDescuento(CalculadoraDescuento calculadoraDescuento) {
        this.calculadoraDescuento = calculadoraDescuento;
    }
}