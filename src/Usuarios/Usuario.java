package Usuarios;

import java.util.ArrayList;
import java.util.List;
import Tienda.Venta;

public abstract class Usuario {
	private String idUsuario;
	private String nombre;
	private String apellido;
	private String login;
	private String contraseña;
	private int puntosFidelidad;
	private List<Venta> ventas;
	
	public Usuario(String idUsuario, String nombre, String apellido, String login, String contraseña, int puntosFidelidad) {
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.apellido = apellido;
		this.login = login;
		this.contraseña = contraseña;
		this.puntosFidelidad = puntosFidelidad;
		this.ventas = new ArrayList<>();
	}
	
	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}


	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public int getPuntosFidelidad() {
		return puntosFidelidad;
	}

	public void setPuntosFidelidad(int puntosFidelidad) {
		this.puntosFidelidad = puntosFidelidad;
	}
	
	public void sumarPuntos(int puntos) {
		this.puntosFidelidad+= puntos;
	}
	public void restarPuntos(int puntos) {
		this.puntosFidelidad -= puntos;
	}

	public List<Venta> getVentas() {
		return ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}

	public void agregarVenta(Venta venta) {
		if(venta != null) {
			this.ventas.add(venta);
		}
		
	}
}
