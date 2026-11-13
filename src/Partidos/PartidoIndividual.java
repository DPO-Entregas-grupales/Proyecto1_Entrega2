package Partidos;

import Deportes.Deporte;
import Usuarios.Entrenador;
import Usuarios.Socio;
import Usuarios.Usuario;

public class PartidoIndividual extends Partido {
    static String TIPO = "Partido Individual";
    private Socio socio1;
    private Socio socio2;
    private Entrenador entrenador;
    private Usuario jugador1;
    private Usuario jugador2;

    public PartidoIndividual(String idPartido, String fecha, String marcadorFinal, String estadoPartido,
            Deporte deporte, Socio socio1, Socio socio2) {
        super(idPartido, fecha, marcadorFinal, estadoPartido, deporte);
        this.socio1 = socio1;
        this.socio2 = socio2;
        this.jugador1 = socio1;
        this.jugador2 = socio2;
    }

    public PartidoIndividual(String idPartido, String fecha, String marcadorFinal, String estadoPartido,
            Deporte deporte, Socio socio1, Entrenador entrenador) {
        super(idPartido, fecha, marcadorFinal, estadoPartido, deporte);
        this.socio1 = socio1;
        this.entrenador = entrenador;
        this.jugador1 = socio1;
        this.jugador2 = entrenador;
    }

    public PartidoIndividual(String idPartido, String fecha, String marcadorFinal, String estadoPartido,
            Deporte deporte, Usuario jugador1, Usuario jugador2) {
        super(idPartido, fecha, marcadorFinal, estadoPartido, deporte);
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        if (jugador1 instanceof Socio) this.socio1 = (Socio) jugador1;
        if (jugador2 instanceof Socio) this.socio2 = (Socio) jugador2;
        if (jugador2 instanceof Entrenador) this.entrenador = (Entrenador) jugador2;
    }

    public Socio getSocio1() { return socio1; }
    public void setSocio1(Socio socio1) { this.socio1 = socio1; this.jugador1 = socio1; }
    public Socio getSocio2() { return socio2; }
    public void setSocio2(Socio socio2) { this.socio2 = socio2; this.jugador2 = socio2; }
    public Entrenador getEntrenador() { return entrenador; }
    public void setEntrenador(Entrenador entrenador) { this.entrenador = entrenador; this.jugador2 = entrenador; }
    public Usuario getJugador1() { return jugador1; }
    public void setJugador1(Usuario jugador1) { this.jugador1 = jugador1; }
    public Usuario getJugador2() { return jugador2; }
    public void setJugador2(Usuario jugador2) { this.jugador2 = jugador2; }
}