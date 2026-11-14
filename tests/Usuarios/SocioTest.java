package Usuarios;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import Deportes.Deporte;
import Deportes.Equipo;
import Deportes.InscripcionEquipo;
import ReservasInstalaciones.Instalacion;
import ReservasInstalaciones.Reserva;

@DisplayName("Elegibilidad, Reservas Activas y Bonos del Socio")
public class SocioTest {

    private Socio socioActivo;
    private Deporte futbol;

    @BeforeEach
    void setUp() {
        socioActivo = new Socio("SOC-01", "Camila", "Rojas", "camila.r", "clave123", 150, 20, true);
        futbol = new Deporte("Fútbol", 11, Arrays.asList("Portero", "Defensa", "Delantero"), true, 3, 0, 1);
    }

    // HU-SOC-02: Evalúa edad límite vs categoría, único equipo por disciplina, cupo disponible y estado activo.
    @Test
    @DisplayName("Valida restricciones de inscripción de socio en equipos")
    void testElegibilidadInscripcionEquipo() {
        Equipo equipoSub21 = new Equipo("Séneca Sub-21", 18, 21, futbol);
        Equipo equipoSub19 = new Equipo("Séneca Sub-19", 18, 19, futbol);

        assertTrue(socioActivo.sePuedeCrearInscripcionEquipo(equipoSub21));
        assertTrue(socioActivo.motivoNoPuedeInscribirse(equipoSub19).contains("Incompatibilidad de categoría"));

        socioActivo.agregarInscripcionEquipo(new InscripcionEquipo(socioActivo, equipoSub21, "Delantero", 9));
        Equipo otroEquipoFutbol = new Equipo("Séneca B", 18, 25, futbol);
        assertTrue(socioActivo.motivoNoPuedeInscribirse(otroEquipoFutbol).contains("único equipo por disciplina"));

        socioActivo.setActivo(false);
        assertEquals("El socio está inactivo", socioActivo.motivoNoPuedeInscribirse(equipoSub21));
    }

    // HU-JUG-01 / 1HU14: Detecta reserva activa solo en cancha de pádel o tenis de mesa techada en la hora actual.
    @ParameterizedTest(name = "Tipo={0}, Techada={1}, Ahora={2} -> Activa={3}")
    @CsvSource({
        "Cancha de Pádel, true, true, true",
        "Mesa de Tenis de Mesa, true, true, true",
        "Cancha de Pádel, false, true, false",
        "Cancha de Fútbol, true, true, false",
        "Cancha de Pádel, true, false, false"
    })
    @DisplayName("Detecta reserva activa restringida en escenarios techados")
    void testTieneReservaActiva(String tipo, boolean esTechada, boolean esAhora, boolean esperado) {
        Instalacion inst = new Instalacion("Inst", tipo, esTechada, 4);
        String fecha = esAhora ? LocalDate.now().toString() : LocalDate.now().plusDays(1).toString();
        String hora = String.valueOf(LocalTime.now().getHour());
        socioActivo.agregarReserva(new Reserva("RES-1", socioActivo, inst, fecha, hora, 4, "Dobles"));

        assertEquals(esperado, socioActivo.tieneReservaActiva());
    }

    // HU-02 / HU-06: Almacena bonos de torneo sin duplicados y los elimina tras ser consumidos una única vez.
    @Test
    @DisplayName("Gestiona almacenamiento sin duplicados y consumo único de bonos")
    void testCicloVidaBonosDescuento() {
        socioActivo.agregarBonoDescuento("  BONO-T1-SOC-01  ");
        socioActivo.agregarBonoDescuento("BONO-T1-SOC-01");
        socioActivo.agregarBonoDescuento("   ");

        assertEquals(1, socioActivo.getBonosDescuento().size());
        assertTrue(socioActivo.tieneBonoDescuento("BONO-T1-SOC-01"));

        assertTrue(socioActivo.usarBonoDescuento("BONO-T1-SOC-01"));
        assertFalse(socioActivo.usarBonoDescuento("BONO-T1-SOC-01"));
        assertFalse(socioActivo.tieneBonosDisponibles());
    }
}
