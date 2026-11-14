package Modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Deportes.Competencia;
import Deportes.Equipo;
import Partidos.Torneo;
import Partidos.TorneoConjunto;
import Partidos.TorneoIndividual;
import Usuarios.Entrenador;
import Usuarios.Socio;

@DisplayName("Flujos de Negocio Integrales de ClubDeportivoSeneca")
public class ClubDeportivoSenecaTest {

    private ClubDeportivoSeneca club;
    private Tienda tienda;

    @BeforeEach
    void setUp() {
        club = new ClubDeportivoSeneca();
        tienda = new Tienda();

        club.crearAdministrador("ADM-1", "Laura", "Admin", "laura.adm", "123", 0);
        club.crearEntrenador("ENT-1", "Carlos", "DT", "carlos.dt", "123", 0, "EMP-1");
        club.crearEntrenador("ENT-2", "Jorge", "DT2", "jorge.dt", "123", 0, "EMP-2");
        club.crearFisioterapeuta("FIS-1", "Diana", "Fisio", "diana.fis", "123", 0, "EMP-3");

        for (int i = 1; i <= 6; i++) {
            club.crearSocio("SOC-" + i, "Socio" + i, "Apellido" + i, "soc" + i, "123", 0, 20, true);
        }

        club.crearDeporte("Fútbol", 2, Arrays.asList("Portero", "Defensa", "Delantero"), true, 3, 0, 1);
        club.crearDeporte("Pádel", 2, Collections.emptyList(), false, 2, 0, 0);
        club.crearDeporte("Tenis", 1, Collections.emptyList(), false, 2, 0, 0);
        club.crearDeporte("Tenis de Mesa", 1, Collections.emptyList(), false, 2, 0, 0);
        club.crearDeporte("Baloncesto", 2, Arrays.asList("Base", "Pívot"), false, 2, 0, 0);
        club.crearDeporte("Voleibol", 2, Arrays.asList("Armador", "Rematador"), false, 3, 0, 0);

        club.crearEquipo("Halcones", 5, 21, "Fútbol", true);
        club.crearEquipo("Cóndores", 5, 21, "Fútbol", true);
    }

    // HU-SOC-02 / HU-ENT-02: Inscribe socios con posición válida y camiseta única, y reporta disponibilidad ante sanciones.
    @Test
    @DisplayName("Gestiona inscripciones a equipos y reporta disponibilidad por sanciones")
    void testInscripcionYDisponibilidadEquipo() {
        club.realizarInscripcionEquipo("SOC-1", "Halcones", "Portero", 1);
        club.realizarInscripcionEquipo("SOC-2", "Halcones", "Delantero", 9);

        assertTrue(club.consultarDisponibilidadEquipo("Halcones").get(0).contains("puede disputar el partido"));
        assertThrows(IllegalArgumentException.class,
                () -> club.realizarInscripcionEquipo("SOC-3", "Halcones", "Defensa", 9));

        club.getEquipos().get("Halcones").getInscripcionDe((Socio) club.getUsuarios().get("SOC-2")).setEstaSuspendido(true);
        List<String> reporte = club.consultarDisponibilidadEquipo("Halcones");
        assertTrue(reporte.get(0).contains("NO puede disputar"));
        assertTrue(reporte.get(1).contains("suspendido"));
    }

    // HU-SOC-01: Exige modalidad Dobles (4 jugadores) en Pádel, aplica límite simultáneo y sugiere canchas libres.
    @Test
    @DisplayName("Controla reservas de Pádel, límite simultáneo por disciplina y sugerencia de alternativas")
    void testReservasPadelYLimiteSimultaneo() {
        club.definirMaximoInstalacionesSimultaneas("Pádel", 1);
        club.crearInstalacion("Pádel 1", "Cancha de Pádel", true, 4, "Pádel");
        club.crearInstalacion("Pádel 2", "Cancha de Pádel", true, 4, "Pádel");
        club.nuevosHorarios("Pádel 1", "2026-11-20", 10, 12);
        club.nuevosHorarios("Pádel 2", "2026-11-20", 10, 12);

        assertThrows(IllegalArgumentException.class,
                () -> club.realizarReserva("R-ERR", "SOC-1", "Pádel 1", "2026-11-20", "10", 2, "Individual"));

        club.realizarReserva("R-OK", "SOC-1", "Pádel 1", "2026-11-20", "10", 4, "Dobles");
        assertEquals(0, club.cuposSimultaneosLibres(club.getDeportes().get("Pádel"), "2026-11-20", "10"));

        IllegalArgumentException exOcupada = assertThrows(IllegalArgumentException.class,
                () -> club.realizarReserva("R-OCUP", "SOC-2", "Pádel 1", "2026-11-20", "10", 4, "Dobles"));
        assertTrue(exOcupada.getMessage().contains("Pádel 2"));

        IllegalArgumentException exLimite = assertThrows(IllegalArgumentException.class,
                () -> club.realizarReserva("R-LIM", "SOC-2", "Pádel 2", "2026-11-20", "10", 4, "Dobles"));
        assertTrue(exLimite.getMessage().contains("instalaciones simultáneas permitidas"));
    }

    // HU-JUG-01 CA-3 / 1HU14: Bloquea el ingreso con bebida caliente a canchas techadas de pádel o tenis de mesa.
    @Test
    @DisplayName("Impide ingreso con bebida caliente en cancha de pádel techada")
    void testIngresoInstalacionConBebidaCaliente() {
        club.crearInstalacion("Pádel Techada", "Cancha de Pádel", true, 4, "Pádel");
        String hoy = LocalDate.now().toString();
        int hora = LocalTime.now().getHour();
        club.nuevosHorarios("Pádel Techada", hoy, hora, hora + 1);
        club.realizarReserva("R-ING", "SOC-1", "Pádel Techada", hoy, String.valueOf(hora), 4, "Dobles");

        assertDoesNotThrow(() -> club.ingresarAInstalacion("SOC-1", "R-ING", false));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> club.ingresarAInstalacion("SOC-1", "R-ING", true));
        assertTrue(ex.getMessage().contains("bebida caliente"));
    }

    // 1HU30: Registra partidos internos, suspende por tarjetas y levanta la sanción tras jugarse el siguiente partido.
    @Test
    @DisplayName("Aplica suspensión por tarjeta roja y la levanta tras el siguiente partido del equipo")
    void testPartidosInternosYSuspensiones() {
        club.realizarInscripcionEquipo("SOC-1", "Halcones", "Portero", 1);
        club.realizarInscripcionEquipo("SOC-2", "Halcones", "Delantero", 9);
        club.realizarInscripcionEquipo("SOC-3", "Cóndores", "Portero", 1);
        club.realizarInscripcionEquipo("SOC-4", "Cóndores", "Delantero", 10);

        club.programarPartidoInterno("P-1", "2026-11-15", "Halcones", "Cóndores");
        club.registrarMarcadorPartido("P-1", "2-1");
        assertTrue(club.registrarEstadisticaFutbol("P-1", "SOC-2", 2, 0, 0, 1));
        assertFalse(club.getEquipos().get("Halcones").cumpleMinimoJugadores());

        club.realizarInscripcionEquipo("SOC-5", "Halcones", "Defensa", 4);
        club.programarPartidoInterno("P-2", "2026-11-18", "Halcones", "Cóndores");
        club.registrarMarcadorPartido("P-2", "1-1");

        assertFalse(club.getEquipos().get("Halcones").getInscripcionDe((Socio) club.getUsuarios().get("SOC-2")).isEstaSuspendido());
    }

    // Competencias externas y otros deportes: tabla de posiciones, goleador, no empate en baloncesto y estadísticas.
    @Test
    @DisplayName("Gestiona competencias externas, tabla de posiciones, goleadores y deportes adicionales")
    void testCompetenciasYGoleadoresYOtrosDeportes() {
        club.crearCompetencia("Copa Ascun", Competencia.ASCUN, "2026", "Liga", "Grupos", 21);
        club.realizarInscripcionEquipo("SOC-1", "Halcones", "Portero", 1);
        club.realizarInscripcionEquipo("SOC-2", "Halcones", "Delantero", 9);
        List<Socio> nomina = Arrays.asList((Socio) club.getUsuarios().get("SOC-1"), (Socio) club.getUsuarios().get("SOC-2"));
        club.realizarInscripcionCompetencia(0, "1", "Halcones", "Copa Ascun", nomina);

        club.programarPartidoCompetencia("PC-1", "2026-11-15", "Halcones", "Copa Ascun", "U. Nacional", "Estadio");
        club.registrarMarcadorPartido("PC-1", "3-0");
        club.registrarEstadisticaFutbol("PC-1", "SOC-2", 3, 0, 0, 0);

        assertEquals(3, club.golesEnCompetencia("Copa Ascun", (Socio) club.getUsuarios().get("SOC-2")));
        assertSame(club.getUsuarios().get("SOC-2"), club.goleadorTorneoCompetencia("Copa Ascun"));
        assertTrue(club.reporteTablaPosiciones("Copa Ascun").get(0).contains("puntos: 3"));

        club.crearEquipo("Basket A", 5, 21, "Baloncesto", true);
        club.crearEquipo("Basket B", 5, 21, "Baloncesto", true);
        club.realizarInscripcionEquipo("SOC-3", "Basket A", "Base", 5);
        club.realizarInscripcionEquipo("SOC-4", "Basket A", "Pívot", 15);
        club.realizarInscripcionEquipo("SOC-5", "Basket B", "Base", 7);
        club.realizarInscripcionEquipo("SOC-6", "Basket B", "Pívot", 12);

        club.realizarPartidoInterno("PB-1", "2026-11-16", "0-0", "Programado", "Basket A", "Basket B");
        assertThrows(IllegalArgumentException.class, () -> club.registrarMarcadorPartido("PB-1", "50-50"));
        club.registrarMarcadorPartido("PB-1", "62-58");
        club.registrarEstadisticaBaloncesto("PB-1", "SOC-3", 22, 8);
        assertEquals(2, club.getEstadisticas().size());
    }

    // Deportes individuales: actualiza ranking, valida estadísticas diferenciadas de tenis/tenis de mesa y reporta ranking.
    @Test
    @DisplayName("Registra resultados individuales, estadísticas de raqueta/mesa y ranking")
    void testDeportesIndividualesYRanking() {
        club.realizarInscripcionIndividual("SOC-1", "Tenis");
        club.realizarInscripcionIndividual("SOC-2", "Tenis");
        club.programarPartidoIndividual("PI-10", "2026-11-16", "SOC-1", "SOC-2", "Tenis");

        assertThrows(IllegalArgumentException.class, () -> club.registrarResultadoIndividual("PI-10", "SOC-1", 1, 1));
        club.registrarResultadoIndividual("PI-10", "SOC-1", 2, 0);

        assertThrows(IllegalArgumentException.class,
                () -> club.registrarEstadisticaRaqueta("PI-10", "SOC-1", 2, 12, 6, 3, 50));
        club.registrarEstadisticaRaqueta("PI-10", "SOC-1", 2, 12, 6, 3, 0);

        club.realizarInscripcionIndividual("SOC-3", "Tenis de Mesa");
        club.realizarInscripcionIndividual("SOC-4", "Tenis de Mesa");
        club.programarPartidoIndividual("PM-1", "2026-11-16", "SOC-3", "SOC-4", "Tenis de Mesa");
        club.registrarResultadoIndividual("PM-1", "SOC-4", 3, 1);
        club.registrarEstadisticaRaqueta("PM-1", "SOC-4", 3, 0, 0, 0, 42);

        assertTrue(club.reporteRankingIndividual("Tenis").get(0).contains("2.0"));
    }

    // Sesiones, turnos y bajas: valida notas, cobertura mínima al cambiar turno y bloqueo de baja con partidos activos.
    @Test
    @DisplayName("Gestiona entrenamientos, cambios de turno con cobertura mínima y bajas administrativas")
    void testEntrenamientosTurnosYBajas() {
        club.realizarInscripcionEquipo("SOC-1", "Halcones", "Portero", 1);
        club.realizarInscripcionEquipo("SOC-2", "Halcones", "Delantero", 9);
        club.realizarInscripcionEquipo("SOC-3", "Cóndores", "Portero", 1);
        club.realizarInscripcionEquipo("SOC-4", "Cóndores", "Delantero", 10);
        club.crearInstalacion("Cancha 1", "Fútbol", false, 22, "Fútbol");

        club.programarSesionEntrenamiento("ENT-10", "2026-11-17", "08:00", 90, "ENT-1", "Cancha 1", "Halcones", null, null);
        club.registrarAsistencia("ENT-10", "SOC-1", "Asistió", 4.5, "Buen trabajo");
        assertThrows(IllegalArgumentException.class,
                () -> club.registrarAsistencia("ENT-10", "SOC-1", "Asistió", 4.0, "Duplicado"));

        club.crearTurno("T-AM", 1, true);
        club.crearTurno("T-PM", 1, false);
        club.asignarTurnoAEmpleado("T-AM", "ENT-1");
        club.asignarTurnoAEmpleado("T-AM", "ENT-2");
        club.asignarTurnoAEmpleado("T-AM", "FIS-1");
        assertFalse(club.realizarSolicitudCambioTurno("ENT-1", "T-AM", "T-PM"));

        club.crearEntrenador("ENT-3", "Mario", "DT3", "mario.dt", "123", 0, "EMP-4");
        club.asignarTurnoAEmpleado("T-AM", "ENT-3");
        assertTrue(club.realizarSolicitudCambioTurno("ENT-1", "T-AM", "T-PM"));

        club.programarPartidoInterno("P-PROG", "2026-11-25", "Halcones", "Cóndores");
        assertThrows(IllegalArgumentException.class, () -> club.realizarBajaEquipo("Halcones"));
        club.registrarMarcadorPartido("P-PROG", "1-0");
        club.realizarBajaEquipo("Halcones");
        assertFalse(club.getEquipos().containsKey("Halcones"));
    }

    // HU-01 a HU-05: Torneo recreativo individual con cupo preferencial top 20%, retiro y bono de descuento al campeón.
    @Test
    @DisplayName("Ejecuta ciclo completo de torneo recreativo individual y entrega bono al campeón")
    void testTorneoIndividualRecreativo() {
        for (int i = 1; i <= 5; i++) {
            club.crearInscripcionIndividual("SOC-" + i, "Tenis", i == 1 ? "Avanzado" : "Principiante", i == 1 ? 20.0 : 1.0);
        }
        TorneoIndividual torneo = club.crearTorneoIndividual("ADM-1", "TOR-IND-1", "Abierto Séneca", "Tenis", 25, 5);

        club.inscribirSocioTorneoIndividual("SOC-1", "TOR-IND-1");
        club.inscribirSocioTorneoIndividual("SOC-2", "TOR-IND-1");
        club.inscribirSocioTorneoIndividual("SOC-3", "TOR-IND-1");
        club.desinscribirSocioTorneoIndividual("SOC-3", "TOR-IND-1");
        assertEquals(3, torneo.getCuposGenerales());

        club.registrarPartidoTorneoIndividual("TOR-IND-1", "PT-1", "2026-11-20", "SOC-1", "SOC-2", 2, 0);
        assertTrue(club.consultarHistorialTorneo("SOC-1", "TOR-IND-1").contains("Abierto Séneca"));

        club.finalizarTorneo("ADM-1", "TOR-IND-1", tienda);
        Socio campeon = (Socio) club.getUsuarios().get("SOC-1");
        assertEquals(Torneo.FINALIZADO, torneo.getEstado());
        assertTrue(campeon.tieneBonoDescuento("BONO-TOR-IND-1-SOC-1"));
        assertTrue(tienda.getBonosValidos().contains("BONO-TOR-IND-1-SOC-1"));
    }

    // HU-08 / Proyecto 2: Torneo competitivo con invitados y empleados; no entrega premio monetario si gana un empleado.
    @Test
    @DisplayName("Gestiona torneo competitivo con invitados y empleados sin premiar empleados")
    void testTorneoCompetitivoInvitadosYEmpleados() {
        club.crearInscripcionIndividual("SOC-1", "Tenis", "Intermedio", 5.0);
        club.crearInvitado("INV-1", "Andrés", "Externo", 21);
        TorneoIndividual torneo = club.crearTorneoIndividualCompetitivo("ADM-1", "TOR-COMP-1", "Copa Oro", "Tenis", 25, 10, 50000.0);

        club.inscribirSocioTorneoIndividual("SOC-1", "TOR-COMP-1");
        club.inscribirInvitadoTorneoIndividual("ADM-1", "INV-1", "TOR-COMP-1");
        club.inscribirEmpleadoTorneoIndividual("ENT-1", "TOR-COMP-1");
        assertEquals(100000.0, torneo.getTotalRecaudado(), 0.01);

        club.desinscribirParticipanteTorneoIndividual("INV-1", "TOR-COMP-1");
        assertEquals(50000.0, torneo.getTotalRecaudado(), 0.01);

        club.registrarPartidoTorneoIndividual("TOR-COMP-1", "PTC-1", "2026-11-21", "ENT-1", "SOC-1", 2, 1);
        club.finalizarTorneo("ADM-1", "TOR-COMP-1", tienda);

        assertNull(torneo.getGanadorPremio());
        assertEquals(0.0, torneo.getPremio(), 0.01);
    }

    // HU-01 / HU-02 / HU-07: Torneo de conjunto competitivo entrega el 80% del recaudo al equipo ganador.
    @Test
    @DisplayName("Gestiona torneo de conjunto competitivo y otorga premio al equipo campeón")
    void testTorneoConjuntoCompetitivo() {
        Equipo eq1 = club.getEquipos().get("Halcones");
        eq1.setPuntajeRanking(30.0);

        TorneoConjunto torneo = club.crearTorneoConjuntoCompetitivo("ADM-1", "TOR-CONJ-1", "Copa Club", "Fútbol", 21, 5, 100000.0);
        club.inscribirEquipoTorneoConjunto("ENT-1", "TOR-CONJ-1", "Halcones");
        club.inscribirEquipoTorneoConjunto("ENT-2", "TOR-CONJ-1", "Cóndores");

        assertEquals(1, club.obtenerTorneosDeParticipante("ENT-1").size());

        club.registrarPartidoTorneoConjunto("TOR-CONJ-1", "PCJ-1", "2026-11-22", "Halcones", "Cóndores", "3-1");
        club.finalizarTorneo("ADM-1", "TOR-CONJ-1", tienda);

        assertEquals("Halcones", torneo.getGanadorPremio());
        assertEquals(160000.0, torneo.getPremio(), 0.01);
    }
}
