package Partidos;

import Deportes.Deporte;
import Deportes.Equipo;

public class PartidoCompetencia extends Partido {
	
	static String TIPO = "Partido Competencia";
	private Equipo equipo;
	private String institucionRival;
	private String escenario;

	public PartidoCompetencia(String idPartido, String fecha, String marcadorFinal, String estadoPartido,
			Deporte deporte, Equipo equipo, String institucionRival, String escenario) {
		super(idPartido, fecha, marcadorFinal, estadoPartido, deporte);
		
		this.equipo = equipo;
		this.institucionRival = institucionRival;
		this.escenario = escenario;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	public String getInstitucionRival() {
		return institucionRival;
	}

	public void setInstitucionRival(String institucionRival) {
		this.institucionRival = institucionRival;
	}

	public String getEscenario() {
		return escenario;
	}

	public void setEscenario(String escenario) {
		this.escenario = escenario;
	}
	
}
