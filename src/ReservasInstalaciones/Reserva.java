package ReservasInstalaciones;
import Usuarios.Socio;

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
