package Deportes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Usuarios.Socio;

@DisplayName("Reglas de Elegibilidad, Disponibilidad y Ranking de Equipos")
public class EquipoTest {

    private Deporte tenisDobles;
    private Deporte baloncesto;
    private Equipo equipo1;
    private Equipo equipo2;
    private Socio socio1;
    private Socio socio2;
    private Socio socio3;

    @BeforeEach
    void setUp() {
        tenisDobles = new Deporte("Tenis Dobles", 2, Arrays.asList("Drive", "Revés"), false, 2, 0, 0);
        baloncesto = new Deporte("Baloncesto", 5, Arrays.asList("Base", "Alero", "Pívot"), false, 2, 0, 0);
        equipo1 = new Equipo("Séneca A", 4, 21, tenisDobles);
        equipo2 = new Equipo("Séneca B", 4, 21, tenisDobles);
        socio1 = new Socio("S1", "Ana", "Gómez", "ana", "123", 0, 20, true);
        socio2 = new Socio("S2", "Luis", "Pérez", "luis", "123", 0, 20, true);
        socio3 = new Socio("S3", "Marta", "Díaz", "marta", "123", 0, 20, true);
    }

    private void inscribir(Socio socio, Equipo equipo, String posicion, int camiseta) {
        InscripcionEquipo insc = new InscripcionEquipo(socio, equipo, posicion, camiseta);
        socio.agregarInscripcionEquipo(insc);
        equipo.agregarSocio(socio);
    }

    // HU-SOC-02: Verifica posiciones válidas del deporte y camisetas únicas positivas en el equipo.
    @Test
    @DisplayName("Valida disponibilidad de posiciones y camisetas únicas")
    void testPosicionesYCamisetasDisponibles() {
        inscribir(socio1, equipo1, "Drive", 10);

        assertTrue(equipo1.esPosicionDisponible("revés"));
        assertFalse(equipo1.esPosicionDisponible("Portero"));
        assertFalse(equipo1.esPosicionDisponible("  "));
        assertFalse(equipo1.esCamisetaDisponible(10));
        assertFalse(equipo1.esCamisetaDisponible(0));
        assertTrue(equipo1.esCamisetaDisponible(7));
    }

    // HU-ENT-02: Calcula jugadores habilitados, mínimo reglamentario y motivos de no disponibilidad.
    @Test
    @DisplayName("Identifica jugadores habilitados y motivos de suspensión o inactividad")
    void testDisponibilidadJugadoresYMinimo() {
        inscribir(socio1, equipo1, "Drive", 1);
        inscribir(socio2, equipo1, "Revés", 2);
        inscribir(socio3, equipo1, "Drive", 3);

        assertEquals(3, equipo1.contarJugadoresHabilitados());
        assertTrue(equipo1.cumpleMinimoJugadores());

        equipo1.getInscripcionDe(socio2).setEstaSuspendido(true);
        socio3.setActivo(false);

        assertEquals(1, equipo1.contarJugadoresHabilitados());
        assertFalse(equipo1.cumpleMinimoJugadores());
        assertEquals("suspendido", equipo1.motivoNoDisponible(socio2));
        assertEquals("inactivo", equipo1.motivoNoDisponible(socio3));
    }

    // HU-07: Suma puntaje de ranking base y puntos acumulados en competencias para cupos preferenciales.
    @Test
    @DisplayName("Calcula el puntaje total de ranking del equipo")
    void testObtenerPuntajeRankingTotal() {
        equipo1.setPuntajeRanking(12.5);
        equipo1.sumarPuntosRanking(2.5);
        Competencia comp = new Competencia("Liga", "Cerros", "2026", "Liga", "Grupos", 21);
        equipo1.agregarInscripcionCompetencia(new InscripcionCompetencia(9, "1", equipo1, comp));

        assertEquals(24.0, equipo1.obtenerPuntajeRankingTotal(), 0.001);
    }

    // Reglas de competencia externa y partido interno: valida categoría, nómina habilitada y mínimo por equipo.
    @Test
    @DisplayName("Valida condiciones para inscribirse a competencia y disputar partido interno")
    void testElegibilidadCompetenciaYPartidoInterno() {
        Competencia comp21 = new Competencia("Copa 21", "Ascun", "2026", "Copa", "Grupos", 21);
        Competencia comp18 = new Competencia("Copa 18", "Ascun", "2026", "Copa", "Grupos", 18);
        inscribir(socio1, equipo1, "Drive", 1);
        inscribir(socio2, equipo1, "Revés", 2);
        List<Socio> nomina = Arrays.asList(socio1, socio2);

        assertTrue(equipo1.sePuedeCrearInscripcionCompetencia(comp21, nomina));
        assertTrue(equipo1.motivoNoPuedeInscribirseEnCompetencia(comp18, nomina).contains("categoría"));
        assertTrue(equipo1.motivoNoPuedeInscribirseEnCompetencia(comp21, Collections.singletonList(socio1)).contains("mínimo"));

        inscribir(socio3, equipo2, "Drive", 1);
        Socio socio4 = new Socio("S4", "Juan", "Sol", "juan", "123", 0, 20, true);
        inscribir(socio4, equipo2, "Revés", 2);

        assertTrue(equipo1.sePuedeCrearPartidoInterno(equipo2));
        assertEquals("Los equipos deben ser distintos", equipo1.motivoNoPuedeJugarContra(equipo1));
        assertEquals("Los equipos deben ser del mismo deporte",
                equipo1.motivoNoPuedeJugarContra(new Equipo("Basket 21", 10, 21, baloncesto)));
    }
}
