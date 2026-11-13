package Partidos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import Deportes.Deporte;

public abstract class Torneo {
    public static final String EN_CURSO = "En curso";
    public static final String FINALIZADO = "Finalizado";
    public static final String RECREATIVO = "Recreativo";
    public static final String COMPETITIVO = "Competitivo";
    public static final double PORCENTAJE_PREMIO = 0.80;

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
    protected String modalidad = RECREATIVO;
    protected double tarifaInscripcion;
    protected double totalRecaudado;
    protected double premio;
    protected String ganadorPremio;

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

    public void convertirEnCompetitivo(double tarifa) {
        if (tarifa <= 0) {
            throw new IllegalArgumentException("La tarifa de inscripción debe ser mayor a cero");
        }
        this.modalidad = COMPETITIVO;
        this.tarifaInscripcion = tarifa;
    }

    public boolean isCompetitivo() {
        return COMPETITIVO.equals(modalidad);
    }

    public void cobrarInscripcion() {
        if (isCompetitivo()) {
            this.totalRecaudado += this.tarifaInscripcion;
        }
    }

    public void reembolsarInscripcion() {
        if (isCompetitivo()) {
            this.totalRecaudado = Math.max(0, this.totalRecaudado - this.tarifaInscripcion);
        }
    }

    public double calcularPremio() {
        return this.totalRecaudado * PORCENTAJE_PREMIO;
    }

    public void otorgarPremio(String claveGanador) {
        this.ganadorPremio = claveGanador;
        this.premio = calcularPremio();
    }

    public String getModalidad() { return modalidad; }
    public double getTarifaInscripcion() { return tarifaInscripcion; }
    public double getTotalRecaudado() { return totalRecaudado; }
    public void setTotalRecaudado(double totalRecaudado) { this.totalRecaudado = totalRecaudado; }
    public double getPremio() { return premio; }
    public void setPremio(double premio) { this.premio = premio; }
    public String getGanadorPremio() { return ganadorPremio; }
    public void setGanadorPremio(String ganadorPremio) { this.ganadorPremio = ganadorPremio; }
    
    
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