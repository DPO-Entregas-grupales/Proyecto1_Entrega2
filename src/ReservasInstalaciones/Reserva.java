package ReservasInstalaciones;
import Usuarios.Socio;
import java.time.LocalDateTime;

public class Reserva {
	private String idReserva;
	private Socio socio;
	private Instalacion instalacion;
	private String fecha;
	private String horaInicio;
	private int cantidadJugadores;
	private  String modalidad;
	
	public Reserva(String idReserva, Socio socio, Instalacion instalacion, String fecha, String horaInicio,
			int cantidadJugadores, String modalidad) {
		super();
		this.idReserva = idReserva;
		this.socio = socio;
		this.instalacion = instalacion;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.cantidadJugadores = cantidadJugadores;
		this.modalidad = modalidad;
	}
	
	// La reserva esta activa si su fecha es hoy y su hora de inicio es la hora actual
	public boolean estaActiva() {
		LocalDateTime ahora = LocalDateTime.now();
		if(!ahora.toLocalDate().toString().equals(this.fecha)) {
			return false;
		}
		try {
			return Integer.parseInt(this.horaInicio.trim()) == ahora.getHour();
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	
	public String getIdReserva() {
		return idReserva;
	}
	public Socio getSocio() {
		return socio;
	}
	public Instalacion getInstalacion() {
		return instalacion;
	}
	public String getFecha() {
		return fecha;
	}
	public String getHoraInicio() {
		return horaInicio;
	}
	public int getCantidadJugadores() {
		return cantidadJugadores;
	}
	public String getModalidad() {
		return modalidad;
	}
	

}
