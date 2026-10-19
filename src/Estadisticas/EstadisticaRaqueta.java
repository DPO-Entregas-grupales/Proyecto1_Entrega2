package Estadisticas;

import Partidos.Partido;
import Usuarios.Socio;

public class EstadisticaRaqueta extends Estadistica {
	
	private int setGanados;
	private int gamesGanados;
	private int aces;
	private int erroresNoForzados;
	private int puntosTotales;
	
	public EstadisticaRaqueta(String idEstadistica, Socio socio, Partido partido, int setGanados, int gamesGanados, int aces, int errores, int puntos) {
		super(idEstadistica, socio, partido);
		
		this.setGanados = setGanados;
		this.gamesGanados = gamesGanados;
		this.aces = aces;
		this.erroresNoForzados = errores;
		this.puntosTotales = puntos;
	}

	public int getSetGanados() {
		return setGanados;
	}

	public int getGamesGanados() {
		return gamesGanados;
	}

	public int getAces() {
		return aces;
	}

	public int getErroresNoForzados() {
		return erroresNoForzados;
	}

	public int getPuntosTotales() {
		return puntosTotales;
	}
	
}
