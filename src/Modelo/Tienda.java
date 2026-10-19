package Modelo;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import Usuarios.Usuario;
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

    public void realizarVenta(String idVenta, String fecha, int hora, Double impuesto, String idUsuario, String codigoDescuento, List<ItemVenta> itemsVenta, ClubDeportivoSeneca clubDeportivo, String tipo) {
        Usuario usuario = null;
        if (clubDeportivo != null && clubDeportivo.getUsuarios() != null) {
            usuario = clubDeportivo.getUsuarios().get(idUsuario);
        }

        double imp = (impuesto != null) ? impuesto : 0.08;
        Venta venta = new Venta(idVenta, fecha, hora, imp, usuario, codigoDescuento);
        venta.setItemsVenta(itemsVenta);

        if (Venta.CAFETERIA.equalsIgnoreCase(tipo)) {
            venta.realizarCalculosVentaCafeteria(itemsVenta);
        } else {
            venta.realizarCalculosVentaTiendaDeportiva(itemsVenta, codigoDescuento, this.calculadoraDescuento);
        }

        agregarVenta(venta);

        if (usuario != null) {
            usuario.agregarVenta(venta);
        }

        if (itemsVenta != null) {
            for (ItemVenta item : itemsVenta) {
                if (item != null) {
                    item.quitarProductoInventario();
                }
            }
        }
    }

    public void realizarVentaConPuntos(String idVenta, String fecha, int hora, Double impuesto, String idUsuario, String codigoDescuento, List<ItemVenta> itemsVenta, ClubDeportivoSeneca clubDeportivo, String tipo) {
        Usuario usuario = null;
        if (clubDeportivo != null && clubDeportivo.getUsuarios() != null) {
            usuario = clubDeportivo.getUsuarios().get(idUsuario);
        }

        double imp = (impuesto != null) ? impuesto : 0.08;
        Venta venta = new Venta(idVenta, fecha, hora, imp, usuario, codigoDescuento);
        venta.setItemsVenta(itemsVenta);

        if (Venta.CAFETERIA.equalsIgnoreCase(tipo)) {
            venta.realizarCalculosVentaCafeteria(itemsVenta);
        } else {
            venta.realizarCalculosVentaTiendaDeportiva(itemsVenta, codigoDescuento, this.calculadoraDescuento);
        }

        if (usuario != null) {
            int puntosDisponibles = usuario.getPuntosFidelidad();
            venta.restarPuntosAlTotal(puntosDisponibles);
            usuario.restarPuntos(puntosDisponibles);
            usuario.agregarVenta(venta);
        }

        agregarVenta(venta);

        if (itemsVenta != null) {
            for (ItemVenta item : itemsVenta) {
                if (item != null) {
                    item.quitarProductoInventario();
                }
            }
        }
    }

    public void crearVenta(String idVenta, String fecha, int hora, Double impuesto, String idUsuario, String codigoDescuento, List<ItemVenta> itemsVenta, ClubDeportivoSeneca clubDeportivo, String tipo) {
        Usuario usuario = null;
        if (clubDeportivo != null && clubDeportivo.getUsuarios() != null) {
            usuario = clubDeportivo.getUsuarios().get(idUsuario);
        }

        double imp = (impuesto != null) ? impuesto : 0.08;
        Venta venta = new Venta(idVenta, fecha, hora, imp, usuario, codigoDescuento);
        venta.setItemsVenta(itemsVenta);

        if (Venta.CAFETERIA.equalsIgnoreCase(tipo)) {
            venta.realizarCalculosVentaCafeteria(itemsVenta);
        } else {
            venta.realizarCalculosVentaTiendaDeportiva(itemsVenta, codigoDescuento, this.calculadoraDescuento);
        }

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