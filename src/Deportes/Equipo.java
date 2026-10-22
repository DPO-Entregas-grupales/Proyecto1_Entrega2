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
	
	public boolean esPosicionDisponible(String posicion) {
		if(posicion == null || posicion.trim().isEmpty()) {
			return false;
		}
		List<String> validas = this.deporteEquipo.getPosiciones();
		if(validas == null || validas.isEmpty()) {
			return true;
		}
		for(String posicionActual: validas) {
			if(posicionActual.equalsIgnoreCase(posicion.trim())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean esCamisetaDisponible(int camiseta) {
		if(camiseta <= 0) {
			return false;
		}
		for(Socio socio : this.socios) {
			for(InscripcionEquipo inscripcion : socio.getInscripcionesEquipos()) {
				if(inscripcion.getEquipo() == this && inscripcion.getNumeroCamiseta() == camiseta) {
					return false;
				}
			}
		}
		return true;
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
	
	public InscripcionEquipo getInscripcionDe(Socio socio) {
		if(socio != null) {
			for(InscripcionEquipo inscripcion : socio.getInscripcionesEquipos()) {
				if(inscripcion.getEquipo() == this) {
					return inscripcion;
				}
			}
		}
		return null;
	}
	
	public boolean socioPuedeJugar(Socio socio) {
		InscripcionEquipo inscripcion = getInscripcionDe(socio);
		if(inscripcion == null) {
			return false;
		}
		return !inscripcion.isEstaSuspendido();
	}
	
	public int contarJugadoresHabilitados() {
		int habilitados = 0;
		for(Socio socio : this.socios) {
			if(socio.isActivo() && socioPuedeJugar(socio)) {
				habilitados++;
			}
		}
		return habilitados;
	}
	
	public boolean cumpleMinimoJugadores() {
		return contarJugadoresHabilitados() >= this.deporteEquipo.getMinJugadores();
	}
	
	public boolean estaInscritoEn(Competencia competencia) {
		for(InscripcionCompetencia inscripcion : this.inscripcionesCompetencia) {
			if(inscripcion.getCompetencia() == competencia) {
				return true;
			}
		}
		return false;
	}
	
	public String motivoNoPuedeInscribirseEnCompetencia(Competencia competencia, List<Socio> nominaJugadores) {
		if(competencia == null) {
			return "La competencia no existe";
		}
		if(estaInscritoEn(competencia)) {
			return "El equipo ya está inscrito en la competencia";
		}
		if(competencia.getCategoria() != this.categoriaEdad) {
			return "La categoría del equipo no corresponde a la de la competencia";
		}
		int minimo = this.deporteEquipo.getMinJugadores();
		if(nominaJugadores == null || nominaJugadores.size() < minimo) {
			return "La nómina no alcanza el mínimo de jugadores (" + minimo + ")";
		}
		for(Socio socio : nominaJugadores) {
			if(!this.socios.contains(socio)) {
				return "El socio " + socio.getIdUsuario() + " no pertenece al equipo";
			}
			if(!socioPuedeJugar(socio)) {
				return "El socio " + socio.getIdUsuario() + " esta suspendido y no puede estar en la nomina";
			}
		}
		return null;
	}
	
	public boolean sePuedeCrearInscripcionCompetencia(Competencia competencia, List<Socio> nominaJugadores) {
		return motivoNoPuedeInscribirseEnCompetencia(competencia, nominaJugadores) == null;
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
	
	
	public String motivoNoPuedeJugarContra(Equipo equipo2) {
		if(equipo2 == null) {
			return "El equipo rival no existe";
		}
		if(equipo2 == this) {
			return "Los equipos deben ser distintos";
		}
		if(this.categoriaEdad != equipo2.getCategoriaEdad()) {
			return "Los equipos deben ser de la misma categoría";
		}
		if(this.deporteEquipo != equipo2.getDeporteEquipo()) {
			return "Los equipos deben ser del mismo deporte";
		}
		if(!this.cumpleMinimoJugadores()) {
			return "El equipo " + this.nombreEquipo + " no tiene el mínimo de jugadores habilitados (habilitados: "
					+ contarJugadoresHabilitados() + ", mínimo: " + this.deporteEquipo.getMinJugadores() + ")";
		}
		if(!equipo2.cumpleMinimoJugadores()) {
			return "El equipo " + equipo2.getNombreEquipo() + " no tiene el mínimo de jugadores habilitados (habilitados: "
					+ equipo2.contarJugadoresHabilitados() + ", mínimo: " + equipo2.getDeporteEquipo().getMinJugadores() + ")";
		}
		return null;
	}
	
	public boolean sePuedeCrearPartidoInterno(Equipo equipo2) {
		return motivoNoPuedeJugarContra(equipo2) == null;
	}
}

