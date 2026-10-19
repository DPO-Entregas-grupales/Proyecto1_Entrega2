package Deportes;
import java.util.ArrayList;
import java.util.List;

import Estadisticas.Estadistica;
import Estadisticas.EstadisticaFutbol;
import Usuarios.Socio;

public class Equipo {
	private String nombreEquipo;
	private int cupoMaximo;
	private int categoriaEdad;
	private List<Integer> camisetasDisponibles;
	private List<Socio> socios;
	private Deporte deporteEquipo;
	private List<InscripcionCompetencia> inscripcionesCompetencia;
	
	public Equipo(String nombreEquipo, int cupoMaximo, int categoriaEdad, Deporte deporteEquipo) {
		
		this.nombreEquipo = nombreEquipo;
		this.cupoMaximo = cupoMaximo;
		this.categoriaEdad = categoriaEdad;
		this.deporteEquipo = deporteEquipo;
		
		this.camisetasDisponibles = new ArrayList<>();
		this.socios = new ArrayList<>();
		this.inscripcionesCompetencia = new ArrayList<>();
	}
	
	public String getNombreEquipo() {
		return nombreEquipo;
	}


	public void setNombreEquipo(String nombreEquipo) {
		this.nombreEquipo = nombreEquipo;
	}


	public int getCupoMaximo() {
		return cupoMaximo;
	}


	public void setCupoMaximo(int cupoMaximo) {
		this.cupoMaximo = cupoMaximo;
	}


	public int getCategoriaEdad() {
		return categoriaEdad;
	}


	public void setCategoriaEdad(int categoriaEdad) {
		this.categoriaEdad = categoriaEdad;
	}


	public List<Integer> getCamisetasDisponibles() {
		return camisetasDisponibles;
	}


	public void setCamisetasDisponibles(List<Integer> camisetasDisponibles) {
		this.camisetasDisponibles = camisetasDisponibles;
	}


	public Deporte getDeporteEquipo() {
		return deporteEquipo;
	}


	public void setDeporteEquipo(Deporte deporteEquipo) {
		this.deporteEquipo = deporteEquipo;
	}
	
	
	public List<Socio> getSocios() {
		return socios;
	}

	public void agregarSocio(Socio socio) {
		if(socio != null) {
			this.socios.add(socio);
		}
	}
	
	public List<InscripcionCompetencia> getInscripcionesCompetencia() {
		return inscripcionesCompetencia;
	}

	public void agregarInscripcionCompetencia(InscripcionCompetencia inscripcionCompetencia) {
		if(inscripcionCompetencia != null) {
			this.inscripcionesCompetencia.add(inscripcionCompetencia);
		}
	}
	
	public boolean esPosicionDisponigle(String posicion) {
		if(posicion != null && this.deporteEquipo.getPosiciones() != null) {
			for(String posicionActual: this.deporteEquipo.getPosiciones()) {
				if(posicionActual.equals(posicion)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean esCamisetaDisponible(int camiseta) {
		if(this.getCamisetasDisponibles() != null) {
			for(int camisetaActual: this.getCamisetasDisponibles()) {
				if(camisetaActual==camiseta) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean tieneCupo() {
		int sociosActuales =socios.size();
		if(sociosActuales < cupoMaximo) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean cumpleMinimoJugadores(Equipo equipo) {
		int sociosActuales = equipo.socios.size();
		if(sociosActuales >= deporteEquipo.getMinJugadores()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean socioPuedeJugar(Socio socio) {
		
		int totalAmarillas = 0;
        int totalRojas = 0;
        
		if(socio != null && socio.getEstadisticas() != null) {
			for(Estadistica estadistica : socio.getEstadisticas()) {
				if(estadistica instanceof EstadisticaFutbol) {
					EstadisticaFutbol futbolStats = (EstadisticaFutbol) estadistica;
					totalAmarillas = futbolStats.getTarjetasAmarillas();
					totalRojas = futbolStats.getTarjetasRojas();
				}
			}
			return totalAmarillas <3 && totalRojas==0;
		}
		return true;
	}
	
	public boolean sePuedeCrearInscripcionCompetencia(Competencia competencia, List<Socio> nominaJugadores) {
		if(this.inscripcionesCompetencia != null) {
			for(InscripcionCompetencia competenciaActual: this.inscripcionesCompetencia) {
				if(competenciaActual.getCompetencia().equals(competencia)) {
					return false;
				}
			}
			int minimoJugadores = this.getDeporteEquipo().getMinJugadores();
			if(nominaJugadores == null || nominaJugadores.size() < minimoJugadores) {
				return false;
			}
			if(competencia.getCategoria()!=this.getCategoriaEdad()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean sePuedeCrearPartidoCompetencia(Competencia competencia, List<Socio> nominaJugadores){
		if(competencia !=null && nominaJugadores != null) {
			for(Socio socioNomina: nominaJugadores) {
				if(!this.socios.contains(socioNomina) || !socioPuedeJugar(socioNomina)) {
					return false;
				}
			}
		}
		return true;
	}
	public boolean sePuedeCrearPartidoInterno(Equipo equipo2) {
		if(equipo2 !=null) {
			if(this.getCategoriaEdad() != equipo2.getCategoriaEdad()) {
				return false;
			}
			if(this.getDeporteEquipo() != equipo2.getDeporteEquipo()) {
				return false;
			}
		}
		return true;
	}
}
