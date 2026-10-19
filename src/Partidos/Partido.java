package Partidos;

import java.util.ArrayList;
import java.util.List;

import Deportes.Deporte;
import Estadisticas.Estadistica;

public abstract class Partido {
	
	private String idPartido;
	private String fecha;
	private String marcadorFinal;
	private String estadoPartido;
	private Deporte deporte;
	private List<Estadistica> estadisticas;
	public Partido(String idPartido, String fecha, String marcadorFinal, String estadoPartido, Deporte deporte) {
		this.idPartido = idPartido;
		this.fecha = fecha;
		this.marcadorFinal = marcadorFinal;
		this.estadoPartido = estadoPartido;
		this.deporte = deporte;
		
		this.estadisticas = new ArrayList<>();
	}
	public String getIdPartido() {
		return idPartido;
	}
	public void setIdPartido(String idPartido) {
		this.idPartido = idPartido;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getMarcadorFinal() {
		return marcadorFinal;
	}
	public void setMarcadorFinal(String marcadorFinal) {
		this.marcadorFinal = marcadorFinal;
	}
	public String getEstadoPartido() {
		return estadoPartido;
	}
	public void setEstadoPartido(String estadoPartido) {
		this.estadoPartido = estadoPartido;
	}
	public Deporte getDeporte() {
		return deporte;
	}
	public void setDeporte(Deporte deporte) {
		this.deporte = deporte;
	}
	public List<Estadistica> getEstadisticas() {
		return estadisticas;
	}
	
	public void agregarEstadistica(Estadistica estadistica) {
		if(estadistica != null) {
			this.estadisticas.add(estadistica);
		}
	}
}
