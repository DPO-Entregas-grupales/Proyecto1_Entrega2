package Usuarios;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import Deportes.Deporte;
import Deportes.InscripcionEquipo;
import Deportes.InscripcionIndividual;
import ReservasInstalaciones.Reserva;
import SesionEntrenamiento.AsistenciaEvaluacion;
import Estadisticas.Estadistica;

public class Socio extends Usuario {
	static String TIPO ="Socio";
	private int edad;
	private boolean activo;
	private List<InscripcionEquipo> inscripcionesEquipos;
	private List<InscripcionIndividual> inscripcionesIndividuales;
	private Map<String, Reserva> reservas;
	private List<AsistenciaEvaluacion> asistenciaEvaluaciones;
	private List<Estadistica> estadisticas;
	

	public Socio(String idUsuario, String nombre, String apellido, String login, String contraseña,
			int puntosFidelidad, int edad, boolean activo) {
		super(idUsuario, nombre, apellido, login, contraseña, puntosFidelidad);
		
		this.edad = edad;
		this.activo = activo;
		
		this.inscripcionesEquipos = new ArrayList<>();
		this.inscripcionesIndividuales = new ArrayList<>();
		this.reservas = new HashMap<>();
		this.asistenciaEvaluaciones = new ArrayList<>();
		this.estadisticas = new ArrayList<>();
	}
	
	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}
	
	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	public List<InscripcionEquipo> getInscripcionesEquipos() {
		return inscripcionesEquipos;
	}

	public List<InscripcionIndividual> getInscripcionesIndividuales() {
		return inscripcionesIndividuales;
	}

	public Map<String, Reserva> getReservas() {
		return reservas;
	}

	public List<AsistenciaEvaluacion> getAsistenciaEvaluaciones() {
		return asistenciaEvaluaciones;
	}

	public List<Estadistica> getEstadisticas() {
		return estadisticas;
	}

	public void agregarInscripcionEquipo(InscripcionEquipo inscripcion) {
		if(inscripcion !=null) {
			this.inscripcionesEquipos.add(inscripcion);
		}
	}
	public void agregarInscripcionIndividual(InscripcionIndividual inscripcion) {
		if(inscripcion != null) {
			this.inscripcionesIndividuales.add(inscripcion);
		}
	}
	
	public void agregarReserva(Reserva reserva) {
		if(reserva != null) {
			this.reservas.put(reserva.getIdReserva(), reserva);
		}
	}
	
	public void agregaAsistenciaEvaluacion(AsistenciaEvaluacion evaluacion) {
		if(evaluacion != null) {
			this.asistenciaEvaluaciones.add(evaluacion);
		}
	}
	
	public void agregarEstadistica(Estadistica estadistica) {
		if(estadistica != null) {
			this.estadisticas.add(estadistica);
		}
	}
	
	public boolean esDeporteNoInscrito(Deporte deporte) {
		if(deporte != null && this.getInscripcionesEquipos()!=null) {
			for(InscripcionEquipo equipo : this.getInscripcionesEquipos()) {
				if(equipo.getEquipo() !=null && deporte.equals(equipo.getEquipo())) {
					return false;
				}
			}
		}
		return true;
	}
}
