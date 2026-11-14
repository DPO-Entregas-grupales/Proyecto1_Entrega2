package Modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Usuarios.Administrador;
import Usuarios.Socio;
import Usuarios.Usuario;

@DisplayName("Autenticación y Unicidad de Login")
public class AutentificadorTest {

    private Autentificador autentificador;
    private Socio socio;
    private List<Usuario> usuarios;

    @BeforeEach
    void setUp() {
        autentificador = new Autentificador();
        socio = new Socio("SOC-1", "Ana", "Gómez", "ana.gomez", "Pass123", 10, 22, true);
        Administrador admin = new Administrador("ADM-1", "Sofía", "Pardo", "sofia.admin", "Adm789", 0);
        usuarios = new ArrayList<>(Arrays.asList(socio, admin));
    }

    // Autenticación de usuarios: valida credenciales correctas y resiste claves nulas (ej. usuarios Invitado).
    @Test
    @DisplayName("Autentica credenciales válidas y rechaza claves erróneas o nulas")
    void testAutenticarUsuarios() {
        Usuario sinClave = new Socio("SOC-2", "Pedro", "Gil", "pedro.g", null, 0, 20, true);
        usuarios.add(sinClave);

        assertSame(socio, autentificador.autenticar("ana.gomez", "Pass123", usuarios));
        assertNull(autentificador.autenticar("ana.gomez", "claveMala", usuarios));
        assertNull(autentificador.autenticar("pedro.g", "cualquier", usuarios));
        assertNull(autentificador.autenticar(null, "Pass123", usuarios));
    }

    // Registro de usuarios: verifica que el login sea único y no esté vacío o en blanco.
    @Test
    @DisplayName("Valida disponibilidad y formato de login nuevo")
    void testEsLoginDisponible() {
        assertTrue(autentificador.esLoginDisponible("nuevo.socio", usuarios));
        assertFalse(autentificador.esLoginDisponible("sofia.admin", usuarios));
        assertFalse(autentificador.esLoginDisponible("   ", usuarios));
        assertFalse(autentificador.esLoginDisponible(null, usuarios));
    }
}
