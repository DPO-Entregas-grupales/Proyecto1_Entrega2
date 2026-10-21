package ReservasInstalaciones;

import java.util.HashMap;
import java.util.Map;

public class Instalacion {
    private String nombre;
    private String tipo;
    private boolean esTechado;
    private int capacidadMaximaJugadores;
    // Mapa que almacena Key: "YYYY-MM-DD_HORA", Value: Boolean (true si está disponible, false si está ocupado)
    private Map<String, Boolean> horarios;

    public Instalacion(String nombre, String tipo, boolean esTechado, int capacidadMaximaJugadores) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.esTechado = esTechado;
        this.capacidadMaximaJugadores = capacidadMaximaJugadores;
        this.horarios = new HashMap<>();
    }

    public void crearHorario(String fecha, int hora) {
        String clave = fecha + "_" + hora;
        if (!horarios.containsKey(clave)) {
            horarios.put(clave, true);
        }
    }

    public void agregarHorarioEstado(String fecha, String hora, boolean disponible) {
        String clave = fecha + "_" + hora;
        horarios.put(clave, disponible);
    }

    public boolean horarioExiste(String fecha, String hora) {
        return horarios.containsKey(fecha + "_" + hora);
    }

    public boolean sePuedeCrearReserva(String fecha, String hora) {
        String clave = fecha + "_" + hora;
        Boolean disponible = horarios.get(clave);
        return disponible != null && disponible;
    }

    public void utilizarHorario(String fecha, String hora) {
        String clave = fecha + "_" + hora;
        horarios.put(clave, false);
    }

    public Map<String, Boolean> getHorarios() {
        return horarios;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public boolean isEsTechado() { return esTechado; }
    public void setEsTechado(boolean esTechado) { this.esTechado = esTechado; }

    public int getCapacidadMaximaJugadores() { return capacidadMaximaJugadores; }
    public void setCapacidadMaximaJugadores(int capacidadMaximaJugadores) { this.capacidadMaximaJugadores = capacidadMaximaJugadores; }
}