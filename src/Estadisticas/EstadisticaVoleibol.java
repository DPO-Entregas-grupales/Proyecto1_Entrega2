package Estadisticas;

import Partidos.Partido;
import Usuarios.Socio;

public class EstadisticaVoleibol extends Estadistica {
	
	private int aces;
	private int bloqueos;

	public EstadisticaVoleibol(String idEstadistica, Socio socio, Partido partido, int aces, int bloqueos) {
		super(idEstadistica, socio, partido);
		
		this.aces = aces;
		this.bloqueos = bloqueos;	
	}

	public int getAces() {
		return aces;
	}

	public int getBloqueos() {
		return bloqueos;
	}
	
}
