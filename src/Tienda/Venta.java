package Tienda;

import java.util.ArrayList;
import java.util.List;

import Usuarios.Socio;
import Usuarios.Usuario;

public class Venta {
    private String idVenta;
    private String fecha;
    private int hora;
    private String tipo;
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
    private boolean bonoAplicado;
    private String codigoBonoAplicado;

    public Venta(String idVenta, String fecha, int hora, String tipo, List<ItemVenta> itemsVenta, double subTotal,
            double total, int puntosGanados, double descuento, Usuario usuario) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo = tipo;
        this.itemsVenta = itemsVenta;
        this.subTotal = subTotal;
        this.total = total;
        this.puntosGanados = puntosGanados;
        this.descuento = descuento;
        this.usuario = usuario;
        this.bonoAplicado = false;
        this.codigoBonoAplicado = null;
    }

    public Venta(String idVenta, String fecha, int hora, String tipo, Usuario usuario) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo = tipo;
        this.usuario = usuario;
        this.itemsVenta = new ArrayList<>();
        this.subTotal = 0.0;
        this.total = 0.0;
        this.puntosGanados = 0;
        this.descuento = 0.0;
        this.bonoAplicado = false;
        this.codigoBonoAplicado = null;
    }

    public void realizarCalculosVentaTiendaDeportiva(List<ItemVenta> itemsVenta, String codigoDescuento, CalculadoraDescuento calculadora) {
        this.subTotal = 0;
        for (ItemVenta item : itemsVenta) {
            this.subTotal += item.precioItemVenta();
        }
        double porcentaje = calculadora.calcularDescuento(this.usuario, codigoDescuento);
        this.descuento = this.subTotal * porcentaje;
        double baseGravable = this.subTotal - this.descuento;
        double totalIVA = baseGravable * IVA;
        this.total = baseGravable + totalIVA;
        this.puntosGanados = (int) (this.total * PORCENTAJE_PUNTOS);
    }

    public void realizarCalculosVentaCafeteria(List<ItemVenta> itemsVenta) {
        this.subTotal = 0;
        for (ItemVenta item : itemsVenta) {
            this.subTotal += item.precioItemVenta();
        }
        this.descuento = 0;
        double totalImpuesto = this.subTotal * IMPUESTO;
        double totalPropinaSugerida = this.subTotal * PROPINA_SUGERIDA;
        this.total = this.subTotal + totalPropinaSugerida + totalImpuesto;
        this.puntosGanados = (int) (this.total * PORCENTAJE_PUNTOS);
    }

    public void aplicarBonoDescuento(String codigoBono, CalculadoraDescuento calculadora) {
        if (this.bonoAplicado) {
            throw new IllegalArgumentException("Los bonos de descuento no son acumulables");
        }
        if (!(this.usuario instanceof Socio) || !((Socio) this.usuario).tieneBonosDisponibles()) {
            throw new IllegalArgumentException("No tiene ningún bono de descuento por redimir");
        }
        String codigoAUsar = codigoBono;
        if (codigoAUsar == null || codigoAUsar.trim().isEmpty()) {
            codigoAUsar = ((Socio) this.usuario).getBonosDescuento().get(0);
        }
        realizarCalculosVentaTiendaDeportiva(this.itemsVenta, codigoAUsar, calculadora);
        this.bonoAplicado = true;
        this.codigoBonoAplicado = codigoAUsar.trim();
    }

    public void restarPuntosAlTotal(int puntos) {
        this.total = Math.max(0, this.total - puntos);
        this.puntosGanados = (int) (this.total * PORCENTAJE_PUNTOS);
    }

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

    public boolean isBonoAplicado() {
        return bonoAplicado;
    }

    public void setBonoAplicado(boolean bonoAplicado) {
        this.bonoAplicado = bonoAplicado;
    }

    public String getCodigoBonoAplicado() {
        return codigoBonoAplicado;
    }

    public void setCodigoBonoAplicado(String codigoBonoAplicado) {
        this.codigoBonoAplicado = codigoBonoAplicado;
    }
}