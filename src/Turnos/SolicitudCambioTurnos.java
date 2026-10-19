package Turnos;

public class SolicitudCambioTurnos {
	
	private String idEmpleado;
	private String idTurnoActual;
	private String idTurnoCambio;
	private boolean esAprobado;
	public SolicitudCambioTurnos(String idEmpleado, String idTurnoActual, String idTurnoCambio) {	
		this.idEmpleado = idEmpleado;
		this.idTurnoActual = idTurnoActual;
		this.idTurnoCambio = idTurnoCambio;
	}
	public SolicitudCambioTurnos(String idEmpleado, String idTurnoActual, String idTurnoCambio, boolean esAprobado) {
		this.idEmpleado = idEmpleado;
		this.idTurnoActual = idTurnoActual;
		this.idTurnoCambio = idTurnoCambio;
		this.esAprobado = esAprobado;
	}
	public String getIdTurnoActual() {
		return idTurnoActual;
	}
	public void setIdTurnoActual(String idTurnoActual) {
		this.idTurnoActual = idTurnoActual;
	}
	public boolean isEsAprobado() {
		return esAprobado;
	}
	public void setEsAprobado(boolean esAprobado) {
		this.esAprobado = esAprobado;
	}
	public String getidEmpleado() {
		return idEmpleado;
	}
	public String getIdTurnoCambio() {
		return idTurnoCambio;
	}
}
