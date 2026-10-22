package Deportes;
import Usuarios.Socio;

public class InscripcionEquipo {
	private Socio socio;
	private Equipo equipo;
	private int tarjetasAmarillasAcumuladas;
	private String posicion;
	private int numeroCamiseta;
	private boolean estaSuspendido;
	public InscripcionEquipo(Socio socio, Equipo equipo, String posicion, int numeroCamiseta) {
		this.socio = socio;
		this.equipo = equipo;
		this.posicion = posicion;
		this.numeroCamiseta = numeroCamiseta;
	}
	public Socio getSocio() {
		return socio;
	}
	public void setSocio(Socio socio) {
		this.socio = socio;
	}
	public Equipo getEquipo() {
		return equipo;
	}
	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}
	public int getTarjetasAmarillasAcumuladas() {
		return tarjetasAmarillasAcumuladas;
	}
	public void setTarjetasAmarillasAcumuladas(int tarjetasAmarillasAcumuladas) {
		this.tarjetasAmarillasAcumuladas = tarjetasAmarillasAcumuladas;
	}
	public String getPosicion() {
		return posicion;
	}
	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}
	public int getNumeroCamiseta() {
		return numeroCamiseta;
	}
	public void setNumeroCamiseta(int numeroCamiseta) {
		this.numeroCamiseta = numeroCamiseta;
	}
	
	public boolean isEstaSuspendido() {
		return estaSuspendido;
	}
	
	public void setEstaSuspendido(boolean estaSuspendido) {
		this.estaSuspendido = estaSuspendido;
	}
	
	// Acumula las tarjetas de un partido. Con 2 amarillas acumuladas o una roja el jugador queda suspendido
	public void registrarTarjetas(int amarillas, int rojas) {
		this.tarjetasAmarillasAcumuladas += amarillas;
		if(rojas > 0) {
			this.estaSuspendido = true;
		}
		if(this.tarjetasAmarillasAcumuladas >= 2) {
			this.estaSuspendido = true;
			this.tarjetasAmarillasAcumuladas = 0;
		}
	}
	
	// Cuando el jugador ya cumplio su suspension con el partido de su equipo
	public void cumplirSuspension() {
		this.estaSuspendido = false;
	}
}