package Persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONObject;

import Modelo.Tienda;
import Tienda.ArticuloTienda;
import Tienda.ProductoCafeteria;

public class PersistenciaTiendaJson {

    private static final String ARTICULOS_TIENDA = "articulosTienda";
    private static final String PRODUCTOS_CAFETERIA = "productosCafeteria";
    private static final String NOMBRE = "nombre";
    private static final String PRECIO_BASE = "precioBase";
    private static final String CANTIDAD_INVENTARIO = "cantidadInventario";
    private static final String CATEGORIA = "categoria";
    private static final String ES_BEBIDA_CALIENTE = "esBebidaCaliente";

    public void cargarTienda(Tienda tienda, String archivo) throws IOException {
        String jsonCompleto = new String(Files.readAllBytes(new File(archivo).toPath()));
        JSONObject raiz = new JSONObject(jsonCompleto);

        if (raiz.has(ARTICULOS_TIENDA)) {
            cargarArticulosTienda(tienda, raiz.getJSONArray(ARTICULOS_TIENDA));
        }
        if (raiz.has(PRODUCTOS_CAFETERIA)) {
            cargarProductosCafeteria(tienda, raiz.getJSONArray(PRODUCTOS_CAFETERIA));
        }
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
            String alergenos = jProd.optString("alergenos", "");

            tienda.crearProductoCafeteria(nombre, precio, cantidad, caliente, alergenos);
        }
    }

    public void salvarTienda(Tienda tienda, String archivo) throws IOException {
        JSONObject jobject = new JSONObject();

        salvarArticulosTienda(tienda, jobject);
        salvarProductosCafeteria(tienda, jobject);

        PrintWriter pw = new PrintWriter(archivo);
        jobject.write(pw, 2, 0);
        pw.close();
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
            jProductos.put(jProd);
        }
        jobject.put(PRODUCTOS_CAFETERIA, jProductos);
    }
}