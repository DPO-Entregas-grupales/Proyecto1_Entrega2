package Deportes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Usuarios.Socio;

@DisplayName("Control Disciplinario en Inscripción de Equipo")
public class InscripcionEquipoTest {

    private InscripcionEquipo inscripcion;

    @BeforeEach
    void setUp() {
        Socio socio = new Socio("SOC-10", "Carlos", "Vargas", "carlos.v", "123", 0, 21, true);
        Deporte futbol = new Deporte("Fútbol", 11, null, true, 3, 0, 1);
        Equipo equipo = new Equipo("Séneca FC", 20, 21, futbol);
        inscripcion = new InscripcionEquipo(socio, equipo, "Delantero", 9);
    }

    // 1HU30: Acumula una amarilla sin suspender; al llegar a dos amarillas suspende y reinicia el contador.
    @Test
    @DisplayName("Suspende y reinicia amarillas al acumular dos tarjetas amarillas")
    void testAcumulacionTarjetasAmarillas() {
        inscripcion.registrarTarjetas(1, 0);
        assertEquals(1, inscripcion.getTarjetasAmarillasAcumuladas());
        assertFalse(inscripcion.isEstaSuspendido());

        inscripcion.registrarTarjetas(1, 0);
        assertTrue(inscripcion.isEstaSuspendido());
        assertEquals(0, inscripcion.getTarjetasAmarillasAcumuladas());
    }

    // 1HU30: Suspende inmediatamente por tarjeta roja y rehabilita al cumplir la fecha de sanción.
    @Test
    @DisplayName("Suspende de inmediato por roja y habilita tras cumplir suspensión")
    void testTarjetaRojaYCumplimientoSuspension() {
        inscripcion.registrarTarjetas(0, 1);
        assertTrue(inscripcion.isEstaSuspendido());

        inscripcion.cumplirSuspension();
        assertFalse(inscripcion.isEstaSuspendido());
    }
}
