package Modelo;

import java.util.List;
import Usuarios.Usuario;

public class Autentificador {

    public Usuario autenticar(String idUsuario, String contrasenia, List<Usuario> usuarios) {
        if (idUsuario == null || contrasenia == null || usuarios == null) {
            return null;
        }

        for (Usuario usuario : usuarios) {
            if (usuario.getIdUsuario().equals(idUsuario) && verificarContrasenia(usuario, contrasenia)) {
                return usuario;
            }
        }
        return null;
    }
    private boolean verificarContrasenia(Usuario usuario, String contrasenia) {
        return usuario.getContraseña() != null && usuario.getContraseña().equals(contrasenia);
    }
}