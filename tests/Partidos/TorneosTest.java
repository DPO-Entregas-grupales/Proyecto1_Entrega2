package Partidos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import Deportes.Deporte;
import Deportes.Equipo;
import Usuarios.Entrenador;
import Usuarios.Invitado;
import Usuarios.Socio;

@DisplayName("Cupos, Recaudo y Premiación en Torneos")
public class TorneosTest {

    private Deporte tenis;
    private Deporte futbol;

    @BeforeEach
    void setUp() {
        tenis = new Deporte("Tenis", 1, null, false, 2, 0, 0);
        futbol = new Deporte("Fútbol", 11, null, true, 3, 0, 1);
    }

    // HU-01: Distribuye el 20% entero en cupos preferenciales y el 80% restante en generales.
    @ParameterizedTest(name = "Cupos={0} -> Preferenciales={1}, Generales={2}")
    @CsvSource({
        "4, 0, 4",
        "5, 1, 4",
        "10, 2, 8"
    })
    @DisplayName("Calcula distribución de cupos preferenciales y generales")
    void testDistribucionCupos(int totales, int prefEsperados, int genEsperados) {
        TorneoIndividual torneo = new TorneoIndividual("T-1", "Abierto", tenis, 21, totales);
        assertEquals(prefEsperados, torneo.getCuposPreferenciales());
        assertEquals(genEsperados, torneo.getCuposGenerales());
    }

    // HU-03 / HU-04 / HU-08: Recauda tarifa a socios e invitados, exime empleados, reembolsa retiros y otorga 80% al ganador.
    @Test
    @DisplayName("Gestiona recaudo competitivo, reembolso y cálculo de premio del 80%")
    void testTorneoCompetitivoRecaudoYPremio() {
        TorneoIndividual torneo = new TorneoIndividual("T-COMP", "Master", tenis, 25, 10);
        torneo.convertirEnCompetitivo(40000.0);

        Socio s1 = new Socio("S1", "Ana", "Gómez", "ana", "123", 0, 22, true);
        Invitado inv = new Invitado("INV1", "Luis", "Ext", "inv-1", "", 0, 22);
        Entrenador ent = new Entrenador("E1", "Carlos", "DT", "carlos", "123", 0, "EMP-1");

        torneo.inscribirParticipante(s1, true);
        torneo.inscribirParticipante(inv, false);
        torneo.inscribirParticipante(ent, false);
        assertEquals(80000.0, torneo.getTotalRecaudado(), 0.01);

        torneo.sumarPuntosParticipante("S1", 4);
        torneo.sumarPuntosParticipante("INV1", 2);
        assertEquals("S1", torneo.obtenerPrimerLugar());

        torneo.otorgarPremio("S1");
        assertEquals(64000.0, torneo.getPremio(), 0.01);

        torneo.desinscribirParticipante(inv, false);
        assertEquals(40000.0, torneo.getTotalRecaudado(), 0.01);
    }

    // HU-07: Controla la inscripción de equipos en torneos de conjunto respetando el límite preferencial.
    @Test
    @DisplayName("Impide exceder cupos preferenciales en TorneoConjunto")
    void testCuposPreferencialesTorneoConjunto() {
        TorneoConjunto torneo = new TorneoConjunto("TC-1", "Liga Interna", futbol, 21, 5);
        Entrenador ent = new Entrenador("E1", "Carlos", "DT", "carlos", "123", 0, "EMP-1");
        Equipo eq1 = new Equipo("Eq1", 15, 21, futbol);
        Equipo eq2 = new Equipo("Eq2", 15, 21, futbol);

        torneo.inscribirEquipo(eq1, ent, true);
        assertThrows(IllegalArgumentException.class, () -> torneo.inscribirEquipo(eq2, ent, true));
        torneo.inscribirEquipo(eq2, ent, false);

        assertEquals(2, torneo.getEquiposInscritos().size());
    }
}
