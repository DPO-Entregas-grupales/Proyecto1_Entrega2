package Persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Modelo.ClubDeportivoSeneca;
import Modelo.Tienda;
import Tienda.ArticuloTienda;
import Tienda.ProductoCafeteria;
import Tienda.Venta;
import Tienda.ItemVenta;
import Tienda.Producto;

public class PersistenciaTiendaJson {

    private static final String ARTICULOS_TIENDA = "articulosTienda";
    private static final String PRODUCTOS_CAFETERIA = "productosCafeteria";
    private static final String VENTAS = "ventas";
    private static final String NOMBRE = "nombre";
    private static final String PRECIO_BASE = "precioBase";
    private static final String CANTIDAD_INVENTARIO = "cantidadInventario";
    private static final String CATEGORIA = "categoria";
    private static final String ES_BEBIDA_CALIENTE = "esBebidaCaliente";
    private static final String ALERGENOS = "alergenos";

    public void cargarTienda(Tienda tienda, String archivo, ClubDeportivoSeneca club) throws IOException {
        File file = new File(archivo);
        if (!file.exists()) {
            return;
        }

        String jsonCompleto = new String(Files.readAllBytes(file.toPath()));
        JSONObject raiz = new JSONObject(jsonCompleto);

        if (raiz.has(ARTICULOS_TIENDA)) {
            cargarArticulosTienda(tienda, raiz.getJSONArray(ARTICULOS_TIENDA));
        }
        if (raiz.has(PRODUCTOS_CAFETERIA)) {
            cargarProductosCafeteria(tienda, raiz.getJSONArray(PRODUCTOS_CAFETERIA));
        }
        if (raiz.has(VENTAS)) {
            cargarVentas(tienda, raiz.getJSONArray(VENTAS), club);
        }
    }

    // Sobrecarga para mantener compatibilidad si no se envía el club
    public void cargarTienda(Tienda tienda, String archivo) throws IOException {
        cargarTienda(tienda, archivo, null);
    }

    private void cargarArticulosTienda(Tienda tienda, JSONArray jArticulos) {
        for (int i = 0; i < jArticulos.length(); i++) {
            JSONObject jArt = jArticulos.getJSONObject(i);
            String nombre = jArt.getString(NOMBRE);
            double precio = jArt.getDouble(PRECIO_BASE);
            int cantidad = jArt.getInt(CANTIDAD_INVENTARIO);
            String categoria = jArt.getString(CATEGORIA);

            tienda.crearArticuloTienda(nombre, precio, cantidad, categoria);
        }
    }

    private void cargarProductosCafeteria(Tienda tienda, JSONArray jProductos) {
        for (int i = 0; i < jProductos.length(); i++) {
            JSONObject jProd = jProductos.getJSONObject(i);
            String nombre = jProd.getString(NOMBRE);
            double precio = jProd.getDouble(PRECIO_BASE);
            int cantidad = jProd.getInt(CANTIDAD_INVENTARIO);
            boolean caliente = jProd.getBoolean(ES_BEBIDA_CALIENTE);
            String alergenos = jProd.optString(ALERGENOS, "");

            tienda.crearProductoCafeteria(nombre, precio, cantidad, caliente, alergenos);
        }
    }

    private void cargarVentas(Tienda tienda, JSONArray jVentas, ClubDeportivoSeneca club) {
        for (int i = 0; i < jVentas.length(); i++) {
            JSONObject jVenta = jVentas.getJSONObject(i);
            String idVenta = jVenta.getString("idVenta");
            String fecha = jVenta.getString("fecha");
            int hora = jVenta.getInt("hora");
            String idUsuario = jVenta.optString("idUsuario", null);
            String tipo = jVenta.optString("tipo", Venta.CAFETERIA);

            List<ItemVenta> items = new ArrayList<>();
            JSONArray jItems = jVenta.optJSONArray("items");
            if (jItems != null) {
                for (int j = 0; j < jItems.length(); j++) {
                    JSONObject jItem = jItems.getJSONObject(j);
                    String nombreProd = jItem.getString("nombreProducto");
                    int cant = jItem.getInt("cantidad");

                    Producto prod = tienda.getArticulosTienda().get(nombreProd);
                    if (prod == null) {
                        prod = tienda.getProductosCafeteria().get(nombreProd);
                    }

                    if (prod != null) {
                        items.add(new ItemVenta(cant, prod));
                    }
                }
            }

            // Invoca crearVenta para reconstituir el registro sin recalcular ni alterar inventarios
            tienda.crearVenta(idVenta, fecha, hora, idUsuario, "", items, club, tipo);
            Venta cargada = tienda.getVentas().get(tienda.getVentas().size() - 1);
            cargada.setSubTotal(jVenta.optDouble("subTotal", 0));
            cargada.setDescuento(jVenta.optDouble("descuento", 0));
            cargada.setTotal(jVenta.optDouble("total", 0));
            cargada.setPuntosGanados(jVenta.optInt("puntosGanados", 0));
        }
    }

    public void salvarTienda(Tienda tienda, String archivo) throws IOException {
        JSONObject jobject = new JSONObject();

        salvarArticulosTienda(tienda, jobject);
        salvarProductosCafeteria(tienda, jobject);
        salvarVentas(tienda, jobject);

        try (PrintWriter pw = new PrintWriter(archivo)) {
            pw.println(jobject.toString(4));
        }
    }

    private void salvarArticulosTienda(Tienda tienda, JSONObject jobject) {
        JSONArray jArticulos = new JSONArray();
        for (ArticuloTienda art : tienda.getArticulosTienda().values()) {
            JSONObject jArt = new JSONObject();
            jArt.put(NOMBRE, art.getNombre());
            jArt.put(PRECIO_BASE, art.getPrecioBase());
            jArt.put(CANTIDAD_INVENTARIO, art.getCantidadInventario());
            jArt.put(CATEGORIA, art.getCategoria());
            jArticulos.put(jArt);
        }
        jobject.put(ARTICULOS_TIENDA, jArticulos);
    }

    private void salvarProductosCafeteria(Tienda tienda, JSONObject jobject) {
        JSONArray jProductos = new JSONArray();
        for (ProductoCafeteria prod : tienda.getProductosCafeteria().values()) {
            JSONObject jProd = new JSONObject();
            jProd.put(NOMBRE, prod.getNombre());
            jProd.put(PRECIO_BASE, prod.getPrecioBase());
            jProd.put(CANTIDAD_INVENTARIO, prod.getCantidadInventario());
            jProd.put(ES_BEBIDA_CALIENTE, prod.isEsBebidaCaliente());
            
            if (prod.getAlergenos() != null && !prod.getAlergenos().isEmpty()) {
                jProd.put(ALERGENOS, String.join(", ", prod.getAlergenos()));
            }
            jProductos.put(jProd);
        }
        jobject.put(PRODUCTOS_CAFETERIA, jProductos);
    }

    private void salvarVentas(Tienda tienda, JSONObject jobject) {
        JSONArray jVentas = new JSONArray();
        for (Venta venta : tienda.getVentas()) {
            JSONObject jVenta = new JSONObject();
            jVenta.put("idVenta", venta.getIdVenta());
            jVenta.put("fecha", venta.getFecha());
            jVenta.put("hora", venta.getHora());
            jVenta.put("tipo", venta.getTipo());
            
            jVenta.put("subTotal", venta.getSubTotal());
            jVenta.put("descuento", venta.getDescuento());
            jVenta.put("total", venta.getTotal());
            jVenta.put("puntosGanados", venta.getPuntosGanados());
            
            if (venta.getUsuario() != null) {
                jVenta.put("idUsuario", venta.getUsuario().getIdUsuario());
            }

            JSONArray jItems = new JSONArray();
            if (venta.getItemsVenta() != null) {
                for (ItemVenta item : venta.getItemsVenta()) {
                    if (item != null && item.getProducto() != null) {
                        JSONObject jItem = new JSONObject();
                        jItem.put("nombreProducto", item.getProducto().getNombre());
                        jItem.put("cantidad", item.getCantidad());
                        jItems.put(jItem);
                    }
                }
            }
            jVenta.put("items", jItems);
            jVentas.put(jVenta);
        }
        jobject.put(VENTAS, jVentas);
    }
}