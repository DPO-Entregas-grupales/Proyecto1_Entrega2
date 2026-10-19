package Deportes;
import java.util.ArrayList;
import java.util.List;
import Partidos.PartidoCompetencia;

public class Competencia {
	private String nombreCompetencia;
	private String entidadOrganizadora;
	static String ASCUN = "Ascun";
	static String CERROS = "Cerros";
	private String anioTemporada;
	private String formato;
	private String faseActual;
	private int categoria;
	private List<InscripcionCompetencia> inscripcionesCompetencia;
	private List<PartidoCompetencia> partidosCompetencia;
	
	public Competencia(String nombreCompetencia, String entidadOrganizadora, String anioTemporada,
			String formato, String faseActual, int categoria) {
		this.nombreCompetencia = nombreCompetencia;
		this.entidadOrganizadora = entidadOrganizadora;
		this.anioTemporada = anioTemporada;
		this.formato = formato;
		this.faseActual = faseActual;
		this.categoria = categoria;
		
		this.inscripcionesCompetencia = new ArrayList<>();
		this.partidosCompetencia = new ArrayList<>();
	}
	
	
	public String getNombreCompetencia() {
		return nombreCompetencia;
	}

	public void setNombreCompetencia(String nombreCompetencia) {
		this.nombreCompetencia = nombreCompetencia;
	}

	public String getEntidadOrganizadora() {
		return entidadOrganizadora;
	}

	public void setEntidadOrganizadora(String entidadOrganizadora) {
		this.entidadOrganizadora = entidadOrganizadora;
	}

	public String getAnioTemporada() {
		return anioTemporada;
	}

	public void setAnioTemporada(String anioTemporada) {
		this.anioTemporada = anioTemporada;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getFaseActual() {
		return faseActual;
	}

	public void setFaseActual(String faseActual) {
		this.faseActual = faseActual;
	}
	
	public int getCategoria() {
		return categoria;
	}


	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}


	public List<InscripcionCompetencia> getInscripcionesCompetencia() {
		return inscripcionesCompetencia;
	}

	
	public void agregarInscripcionCompetencia(InscripcionCompetencia inscripcionCompetencia) {
		if(inscripcionCompetencia != null) {
			this.inscripcionesCompetencia.add(inscripcionCompetencia);
		}
	}

	public List<PartidoCompetencia> getPartidosCompetencia() {
		return partidosCompetencia;
	}
	
	public void agregarPartidosCompetencia(PartidoCompetencia partido) {
		if(partido != null) {
			this.partidosCompetencia.add(partido);
		}
		
	}
	
}
