package Partidos;

import Deportes.Deporte;
import Usuarios.Socio;
import Usuarios.Entrenador;

public class PartidoIndividual extends Partido {
	
	static String TIPO = "Partido Individual";
	private Socio socio1;
	private Socio socio2;
	private Entrenador entrenador;
	
	public PartidoIndividual(String idPartido, String fecha, String marcadorFinal, String estadoPartido,
			Deporte deporte, Socio socio1, Socio socio2) {
		super(idPartido, fecha, marcadorFinal, estadoPartido, deporte);
		this.socio1 = socio1;
		this.socio2 = socio2;
	}
	public PartidoIndividual(String idPartido, String fecha, String marcadorFinal, String estadoPartido,
			Deporte deporte, Socio socio1, Entrenador entrenador) {
		super(idPartido, fecha, marcadorFinal, estadoPartido, deporte);
		this.socio1 = socio1;
		this.entrenador = entrenador;
	}
	public Socio getSocio1() {
		return socio1;
	}
	public void setSocio1(Socio socio1) {
		this.socio1 = socio1;
	}
	public Socio getSocio2() {
		return socio2;
	}
	public void setSocio2(Socio socio2) {
		this.socio2 = socio2;
	}
	public Entrenador getEntrenador() {
		return entrenador;
	}
	public void setEntrenador(Entrenador entrenador) {
		this.entrenador = entrenador;
	}
	
}
