package ReservasInstalaciones;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Usuarios.Socio;

@DisplayName("Disponibilidad de Horarios y Reglas de Instalaciones")
public class InstalacionYReservaTest {

    // HU-SOC-01: Crea bloques horarios disponibles y los bloquea al ser utilizados en una reserva.
    @Test
    @DisplayName("Gestiona disponibilidad y ocupación de bloques horarios")
    void testBloqueoHorarioTrasReserva() {
        Instalacion cancha = new Instalacion("Cancha 1", "Fútbol", false, 22);
        cancha.crearHorario("2026-11-20", 9);

        assertTrue(cancha.sePuedeCrearReserva("2026-11-20", "9"));
        cancha.utilizarHorario("2026-11-20", "9");
        assertFalse(cancha.sePuedeCrearReserva("2026-11-20", "9"));
    }

    // HU-JUG-01 / 1HU14: Identifica canchas techadas de pádel/tenis de mesa y reservas activas en curso.
    @Test
    @DisplayName("Identifica escenarios techados restringidos y reservas en curso")
    void testInstalacionTechadaYReservaActiva() {
        Instalacion padelTechada = new Instalacion("Pádel 1", "Cancha de Pádel", true, 4);
        Instalacion padelAbierta = new Instalacion("Pádel 2", "Cancha de Pádel", false, 4);
        Socio socio = new Socio("SOC-1", "Ana", "Gómez", "ana", "123", 0, 22, true);

        String hoy = LocalDate.now().toString();
        String horaActual = String.valueOf(LocalTime.now().getHour());

        Reserva activa = new Reserva("R-1", socio, padelTechada, hoy, horaActual, 4, "Dobles");
        Reserva futura = new Reserva("R-2", socio, padelTechada, LocalDate.now().plusDays(1).toString(), horaActual, 4, "Dobles");

        assertTrue(padelTechada.esPadelOTenisDeMesaTechada());
        assertFalse(padelAbierta.esPadelOTenisDeMesaTechada());
        assertTrue(activa.estaActiva());
        assertFalse(futura.estaActiva());
    }
}
