package Persistencia;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import Modelo.ClubDeportivoSeneca;
import Modelo.Tienda;
import Partidos.TorneoIndividual;
import Tienda.ItemVenta;
import Usuarios.Invitado;
import Usuarios.Socio;

@DisplayName("Serialización y Reconstrucción JSON del Sistema")
public class PersistenciaTest {

    // Persistencia JSON: guarda y reconstruye usuarios (incluyendo bonos de socio e invitados), club, torneos y tienda.
    @Test
    @DisplayName("Salva y recarga usuarios, estado del club, torneos y tienda en JSON")
    void testCicloCompletoPersistenciaJson(@TempDir Path tempDir) {
        ClubDeportivoSeneca clubOriginal = new ClubDeportivoSeneca();
        clubOriginal.crearAdministrador("ADM-1", "Diego", "Ruiz", "diego.r", "pass4", 0);
        clubOriginal.crearEntrenador("ENT-1", "Carlos", "Méndez", "carlos.m", "pass2", 50, "DESC-C");
        clubOriginal.crearSocio("SOC-1", "Camila", "Rojas", "camila.r", "pass1", 120, 21, true);
        ((Socio) clubOriginal.getUsuarios().get("SOC-1")).agregarBonoDescuento("BONO-T1-SOC-1");
        clubOriginal.crearSocio("SOC-2", "Juan", "Pérez", "juan.p", "pass5", 80, 21, true);
        clubOriginal.crearInvitado("INV-1", "Mateo", "Silva", 19);

        clubOriginal.crearDeporte("Fútbol", 2, Arrays.asList("Portero", "Delantero"), true, 3, 0, 1);
        clubOriginal.definirMaximoInstalacionesSimultaneas("Fútbol", 2);
        clubOriginal.crearDeporte("Tenis", 1, Collections.emptyList(), false, 2, 0, 0);

        clubOriginal.crearEquipo("Séneca FC", 18, 21, "Fútbol", true);
        clubOriginal.realizarInscripcionEquipo("SOC-1", "Séneca FC", "Portero", 1);
        clubOriginal.crearInscripcionIndividual("SOC-1", "Tenis", "Intermedio", 6.0);
        clubOriginal.crearInscripcionIndividual("SOC-2", "Tenis", "Principiante", 1.0);
        clubOriginal.crearTorneoIndividualCompetitivo("ADM-1", "TOR-1", "Copa Séneca", "Tenis", 25, 8, 30000.0);
        clubOriginal.inscribirSocioTorneoIndividual("SOC-1", "TOR-1");
        clubOriginal.inscribirSocioTorneoIndividual("SOC-2", "TOR-1");
        clubOriginal.registrarPartidoTorneoIndividual("TOR-1", "PT-1", "2026-11-20", "SOC-1", "SOC-2", 2, 0);

        Tienda tiendaOriginal = new Tienda();
        tiendaOriginal.crearArticuloTienda("Camiseta Club", 45000.0, 10, "Indumentaria");
        tiendaOriginal.realizarVenta("V-1", "2026-11-15", 14, 0.19, "SOC-1", "",
                Collections.singletonList(new ItemVenta(1, tiendaOriginal.getArticulosTienda().get("Camiseta Club"))),
                clubOriginal, "TiendaDeportiva");

        Path fUsuarios = tempDir.resolve("usuarios.json");
        Path fClub = tempDir.resolve("club.json");
        Path fTienda = tempDir.resolve("tienda.json");

        clubOriginal.salvarUsuarios(fUsuarios.toString());
        clubOriginal.salvarClubDeportivo(fClub.toString());
        tiendaOriginal.salvarTienda(fTienda.toString());

        ClubDeportivoSeneca clubCargado = new ClubDeportivoSeneca();
        clubCargado.cargarUsuarios(fUsuarios.toString());
        clubCargado.cargarClubDeportivo(fClub.toString());

        Tienda tiendaCargada = new Tienda();
        tiendaCargada.cargarTienda(fTienda.toString(), clubCargado);

        assertEquals(5, clubCargado.getUsuarios().size());
        assertTrue(((Socio) clubCargado.getUsuarios().get("SOC-1")).tieneBonoDescuento("BONO-T1-SOC-1"));
        assertEquals(19, ((Invitado) clubCargado.getUsuarios().get("INV-1")).getEdad());
        assertEquals(2, clubCargado.getDeportes().get("Fútbol").getMaxInstalacionesSimultaneas());
        TorneoIndividual torneoCargado = (TorneoIndividual) clubCargado.getTorneos().get("TOR-1");
        assertEquals(60000.0, torneoCargado.getTotalRecaudado(), 0.01);
        assertEquals("SOC-1", torneoCargado.obtenerPrimerLugar());
        assertEquals(9, tiendaCargada.getArticulosTienda().get("Camiseta Club").getCantidadInventario());
        assertEquals(1, tiendaCargada.getVentas().size());
    }
}
