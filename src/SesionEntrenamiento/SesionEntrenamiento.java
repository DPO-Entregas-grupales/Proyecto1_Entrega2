package SesionEntrenamiento;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import Usuarios.Socio;
import Usuarios.Entrenador;
import Deportes.Equipo;

public class SesionEntrenamiento {
	private String idEntrenamiento;
	private String fecha;
	private String hora;
	private int duracionMinutos;
	private Entrenador entrenador;
	private Map<String, AsistenciaEvaluacion> asistenciaEvaluaciones;
	private List<Socio> entrenamientoSocios;
	
	public SesionEntrenamiento(String idEntrenamiento, String fecha, String hora, int duracionMinutos,
			Entrenador entrenador) {
		this.idEntrenamiento = idEntrenamiento;
		this.fecha = fecha;
		this.hora = hora;
		this.duracionMinutos = duracionMinutos;
		this.entrenador = entrenador;
		this.asistenciaEvaluaciones = new HashMap<>();
		this.entrenamientoSocios = new ArrayList<>();
	}
	
	public String getIdEntrenamiento() {
		return idEntrenamiento;
	}


	public void setIdEntrenamiento(String idEntrenamiento) {
		this.idEntrenamiento = idEntrenamiento;
	}


	public String getFecha() {
		return fecha;
	}


	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


	public String getHora() {
		return hora;
	}


	public void setHora(String hora) {
		this.hora = hora;
	}


	public int getDuracionMinutos() {
		return duracionMinutos;
	}


	public void setDuracionMinutos(int duracionMinutos) {
		this.duracionMinutos = duracionMinutos;
	}


	public Entrenador getEntrenador() {
		return entrenador;
	}


	public void setEntrenador(Entrenador entrenador) {
		this.entrenador = entrenador;
	}


	public Map<String, AsistenciaEvaluacion> getAsistenciaEvaluaciones() {
		return asistenciaEvaluaciones;
	}


	public List<Socio> getEntrenamientoSocios() {
		return entrenamientoSocios;
	}


	public void setEntrenamientoSocios(List<Socio> entrenamientoSocios) {
		this.entrenamientoSocios = entrenamientoSocios;
	}

	public void agregarAsistenciaEvaluaciones(AsistenciaEvaluacion asistencia) {
		if(asistencia != null) {
			asistenciaEvaluaciones.put(asistencia.getSocio().getIdUsuario(), asistencia);
		}
	}
	
	public void insertarSocio(Socio socio) {
		if(socio != null) {
			this.entrenamientoSocios.add(socio);
		}
	}
	public void insertarEquipo(Equipo equipo) {
		if(equipo != null) {
			if(equipo.getSocios() != null) {
				for(Socio socio: equipo.getSocios()) {
					if(socio != null && this.entrenamientoSocios.contains(socio)){
						this.entrenamientoSocios.add(socio);
					}
				}
			}
		}
	}
}
