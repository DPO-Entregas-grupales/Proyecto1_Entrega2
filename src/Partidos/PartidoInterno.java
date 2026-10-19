package Partidos;

import Deportes.Deporte;
import Deportes.Equipo;

public class PartidoInterno extends Partido {
	
	static String TIPO = "Partido Interno";
	private Equipo equipo1;
	private Equipo equipo2;
	
	public PartidoInterno(String idPartido, String fecha, String marcadorFinal, String estadoPartido, Deporte deporte, Equipo equipo1, Equipo equipo2) {
		super(idPartido, fecha, marcadorFinal, estadoPartido, deporte);
		this.equipo1 = equipo1;
		this.equipo2 = equipo2;
	}

	public Equipo getEquipo1() {
		return equipo1;
	}
	public Equipo getEquipo2() {
		return equipo2;
	}
}
