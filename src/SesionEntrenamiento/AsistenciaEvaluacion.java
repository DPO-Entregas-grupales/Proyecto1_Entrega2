package SesionEntrenamiento;
import Usuarios.Socio;

public class AsistenciaEvaluacion {
	private String estadoAsistencia;
	private double notaDesempeno;
	private String comentario;
	private Socio socio;
	private SesionEntrenamiento sesionEntrenamiento;
	
	public AsistenciaEvaluacion(String estadoAsistencia, double notaDesempeno, String comentario, Socio socio,
			SesionEntrenamiento sesionEntrenamiento) {
		this.estadoAsistencia = estadoAsistencia;
		this.notaDesempeno = notaDesempeno;
		this.comentario = comentario;
		this.socio = socio;
		this.sesionEntrenamiento = sesionEntrenamiento;
	}
	public String getEstadoAsistencia() {
		return estadoAsistencia;
	}
	public double getNotaDesempeno() {
		return notaDesempeno;
	}
	public String getComentario() {
		return comentario;
	}
	public Socio getSocio() {
		return socio;
	}
	public SesionEntrenamiento getSesionEntrenamiento() {
		return sesionEntrenamiento;
	}
	
	
}
