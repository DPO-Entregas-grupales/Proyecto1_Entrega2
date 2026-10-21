package Tienda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Usuarios.Usuario;

public class Venta {
    private String idVenta;
    private String fecha;
    private int hora;
    private String tipo; // Nuevo atributo de tipo String
    private List<ItemVenta> itemsVenta;
    private double subTotal;
    public static String CAFETERIA = "Cafeteria";
    public static String TIENDADEPORTIVA = "TiendaDeportiva";
    static double IMPUESTO = 0.08;
    static double PROPINA_SUGERIDA = 0.10;
    static double PORCENTAJE_PUNTOS = 0.02;
    static double IVA = 0.19;
    private double total;
    private int puntosGanados;
    private double descuento;
    private Usuario usuario;

    // Constructor completo
    public Venta(String idVenta, String fecha, int hora, String tipo, List<ItemVenta> itemsVenta, double subTotal,
         double total, int puntosGanados, double descuento, Usuario usuario) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo = tipo; // Asignación del nuevo atributo
        this.itemsVenta = itemsVenta;
        this.subTotal = subTotal;
        this.total = total;
        this.puntosGanados = puntosGanados;
        this.descuento = descuento;
        this.usuario = usuario;
    }

    // Constructor simplificado
    public Venta(String idVenta, String fecha, int hora, String tipo, Usuario usuario) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo = tipo; // Asignación del nuevo atributo
        this.usuario = usuario;
        this.itemsVenta = new ArrayList<>();
        this.subTotal = 0.0;
        this.total = 0.0;
        this.puntosGanados = 0;
        this.descuento = 0.0;
    }
    
    public void realizarCalculosVentaTiendaDeportiva(List<ItemVenta> itemsVenta, String codigoDescuento, CalculadoraDescuento calculadora) {
        this.subTotal = 0;
        
        // Calcula el precio robusto de la lista de items que se pasa.
        for(ItemVenta item : itemsVenta) {
            double precioItem = item.precioItemVenta();
            this.subTotal += (double)precioItem;
        }
        
        // Calcula cual es el descuento.
        double descuentoCalculado = calculadora.calcularDescuento(this.usuario, codigoDescuento);
        this.descuento = descuentoCalculado;
        
        // Calcular el subTotal con el descuento
        if(descuento != 0) {
            this.subTotal = this.subTotal * descuento;
        }
        
        // Calcula el IVA
        double totalIVA = this.subTotal * IVA;
        
        // Calcular el total de la venta.
        this.total = this.subTotal + totalIVA;

        // Calcular los puntosGanados.
        this.puntosGanados = (int) (this.subTotal * PORCENTAJE_PUNTOS);
    }
    
    public void realizarCalculosVentaCafeteria(List<ItemVenta> itemsVenta) {
        this.subTotal = 0;
        
        // Calcula el precio robusto de la lista de items que se pasa.
        for(ItemVenta item : itemsVenta) {
            double precioItem = item.precioItemVenta();
            this.subTotal += (double)precioItem;
        }
        
        // Calcula el Impuesto
        double totalImpuesto = this.subTotal * IMPUESTO;
        
        // Calcula la propinaSugerida
        double totalPropinaSugerida = this.subTotal * PROPINA_SUGERIDA;
        
        // Calcular el total de la venta.
        this.total = this.subTotal + totalPropinaSugerida + totalImpuesto;

        // Calcular los puntosGanados.
        this.puntosGanados = (int) (this.subTotal * PORCENTAJE_PUNTOS);
    }
    
    public void restarPuntosAlTotal(int puntos) {
        this.total = this.total - puntos;
    }
    
    // Getters y Setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public List<ItemVenta> getItemsVenta() {
        return itemsVenta;
    }

    public void setItemsVenta(List<ItemVenta> itemsVenta) {
        this.itemsVenta = itemsVenta;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPuntosGanados() {
        return puntosGanados;
    }

    public void setPuntosGanados(int puntosGanados) {
        this.puntosGanados = puntosGanados;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}