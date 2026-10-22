package Modelo;

import java.util.List;
import Usuarios.Usuario;

public class Autentificador {

    public Usuario autenticar(String login, String contrasenia, List<Usuario> usuarios) {
        if (login == null || contrasenia == null || usuarios == null) {
            return null;
        }
        for (Usuario usuario : usuarios) {
            if (login.equals(usuario.getLogin()) && verificarContrasenia(usuario, contrasenia)) {
                return usuario;
            }
        }
        return null;
    }

    public boolean esLoginDisponible(String login, List<Usuario> usuarios) {
        if (login == null || login.trim().isEmpty()) {
            return false;
        }
        if (usuarios != null) {
            for (Usuario usuario : usuarios) {
                if (login.equals(usuario.getLogin())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean verificarContrasenia(Usuario usuario, String contrasenia) {
        return usuario.getContraseña() != null && usuario.getContraseña().equals(contrasenia);
    }
}