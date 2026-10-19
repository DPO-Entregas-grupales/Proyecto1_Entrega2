package Estadisticas;

import Partidos.Partido;
import Usuarios.Socio;

public class EstadisticaBaloncesto extends Estadistica {
	
	private int puntos;
	private int rebotes;

	public EstadisticaBaloncesto(String idEstadistica, Socio socio, Partido partido, int puntos, int rebotes) {
		super(idEstadistica, socio, partido);
		this.puntos = puntos;
		this.rebotes = rebotes;	
	}

	public int getPuntos() {
		return puntos;
	}

	public int getRebotes() {
		return rebotes;
	}
	
}
