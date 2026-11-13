package Partidos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import Deportes.Deporte;

public abstract class Torneo {
    public static final String EN_CURSO = "En curso";
    public static final String FINALIZADO = "Finalizado";

    protected String idTorneo;
    protected String nombre;
    protected Deporte deporte;
    protected int categoria;
    protected String estado;
    protected int cuposTotales;
    protected int cuposPreferenciales;
    protected int cuposGenerales;
    protected List<Partido> listaPartidos;
    protected Map<String, Integer> tablaPosiciones;

    public Torneo(String idTorneo, String nombre, Deporte deporte, int categoria, int cuposTotales) {
        if (cuposTotales <= 0) {
            throw new IllegalArgumentException("El número de cupos totales debe ser mayor a cero");
        }
        this.idTorneo = idTorneo;
        this.nombre = nombre;
        this.deporte = deporte;
        this.categoria = categoria;
        this.estado = EN_CURSO;
        this.cuposTotales = cuposTotales;
        this.cuposPreferenciales = (int) Math.floor(cuposTotales * 0.20);
        this.cuposGenerales = cuposTotales - this.cuposPreferenciales;
        this.listaPartidos = new ArrayList<Partido>();
        this.tablaPosiciones = new LinkedHashMap<String, Integer>();
    }

    public void agregarPartido(Partido partido) {
        if (partido != null) {
            this.listaPartidos.add(partido);
        }
    }

    public void sumarPuntosParticipante(String claveParticipante, int puntos) {
        if (claveParticipante != null && tablaPosiciones.containsKey(claveParticipante)) {
            tablaPosiciones.put(claveParticipante, tablaPosiciones.get(claveParticipante) + puntos);
        }
    }

    public List<String> obtenerPosicionesOrdenadas() {
        List<String> pendientes = new ArrayList<String>(tablaPosiciones.keySet());
        List<String> ordenadas = new ArrayList<String>();
        while (!pendientes.isEmpty()) {
            String mejor = pendientes.get(0);
            for (String candidato : pendientes) {
                if (tablaPosiciones.get(candidato) > tablaPosiciones.get(mejor)) {
                    mejor = candidato;
                }
            }
            ordenadas.add(mejor);
            pendientes.remove(mejor);
        }
        return ordenadas;
    }

    public String obtenerPrimerLugar() {
        List<String> ordenadas = obtenerPosicionesOrdenadas();
        if (ordenadas.isEmpty()) {
            return null;
        }
        return ordenadas.get(0);
    }

    public String getIdTorneo() { return idTorneo; }
    public void setIdTorneo(String idTorneo) { this.idTorneo = idTorneo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Deporte getDeporte() { return deporte; }
    public void setDeporte(Deporte deporte) { this.deporte = deporte; }
    public int getCategoria() { return categoria; }
    public void setCategoria(int categoria) { this.categoria = categoria; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getCuposTotales() { return cuposTotales; }
    public int getCuposPreferenciales() { return cuposPreferenciales; }
    public void setCuposPreferenciales(int cuposPreferenciales) { this.cuposPreferenciales = cuposPreferenciales; }
    public int getCuposGenerales() { return cuposGenerales; }
    public void setCuposGenerales(int cuposGenerales) { this.cuposGenerales = cuposGenerales; }
    public List<Partido> getListaPartidos() { return listaPartidos; }
    public Map<String, Integer> getTablaPosiciones() { return tablaPosiciones; }
}