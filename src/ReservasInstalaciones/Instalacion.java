package ReservasInstalaciones;
import java.util.HashMap;
import java.util.Map;

public class Instalacion {
	private String nombre;
	private String tipo;
	private boolean esTechado;
	private int capacidadMaximaJugadores;
	private Map<String, Boolean> horariosDisponibles;
	
	public Instalacion(String nombre, String tipo, boolean esTechado, int capacidadMaximaJugadores) {
		this.nombre = nombre;
		this.tipo = tipo;
		this.esTechado = esTechado;
		this.capacidadMaximaJugadores = capacidadMaximaJugadores;
		this.horariosDisponibles = new HashMap<>();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isEsTechado() {
		return esTechado;
	}

	public void setEsTechado(boolean esTechado) {
		this.esTechado = esTechado;
	}

	public int getCapacidadMaximaJugadores() {
		return capacidadMaximaJugadores;
	}

	public void setCapacidadMaximaJugadores(int capacidadMaximaJugadores) {
		this.capacidadMaximaJugadores = capacidadMaximaJugadores;
	}

	public Map<String, Boolean> getHorariosDisponibles() {
		return horariosDisponibles;
	}

	public void crearHorario(String fecha, int horaInicio){
		this.horariosDisponibles.put( fecha + horaInicio,true);
	}
	
	public boolean horarioExiste(String fecha, String horaInicio) {
		if( fecha!=null && horaInicio !=null) {
			if(horariosDisponibles.containsKey(fecha+horaInicio)) {
				return true;
			}
		}
		return false;
	}
	public boolean sePuedeCrearReserva(String fecha, String horaInicio) {
		if(horariosDisponibles!= null && fecha != null) {
			if(horarioExiste(fecha, horaInicio)) {
				return false;
			}
			if(horariosDisponibles.get(fecha+horaInicio)== false) {
				return false;
			}
		}
		return true;
	}
	public void utilizarHorario(String fecha, String horaInicio){

		this.horariosDisponibles.put( fecha + horaInicio,false);

		}
}
