package Estadisticas;

import Partidos.Partido;
import Usuarios.Socio;

public class EstadisticaFutbol extends Estadistica {
	
	private int goles;
	private int asistencias;
	private int tarjetasAmarillas;
	private int tarjetasRojas;
	
	
	
	public EstadisticaFutbol(String idEstadistica, Socio socio, Partido partido, int goles, int asistencias, int tarjetasAmarillas, int tarjetasRojas) {
		super(idEstadistica, socio, partido);
		
		this.goles = goles;
		this.asistencias = asistencias;
		this.tarjetasAmarillas = tarjetasAmarillas;
		this.tarjetasRojas = tarjetasRojas;
	}



	public int getGoles() {
		return goles;
	}



	public void setGoles(int goles) {
		this.goles = goles;
	}



	public int getAsistencias() {
		return asistencias;
	}



	public void setAsistencias(int asistencias) {
		this.asistencias = asistencias;
	}



	public int getTarjetasAmarillas() {
		return tarjetasAmarillas;
	}



	public void setTarjetasAmarillas(int tarjetasAmarillas) {
		this.tarjetasAmarillas = tarjetasAmarillas;
	}



	public int getTarjetasRojas() {
		return tarjetasRojas;
	}



	public void setTarjetasRojas(int tarjetasRojas) {
		this.tarjetasRojas = tarjetasRojas;
	}
	

}
