package SesionEntrenamiento;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import Deportes.Deporte;
import Deportes.Equipo;
import Usuarios.Entrenador;
import Usuarios.Socio;

@DisplayName("Convocatoria a Entrenamientos y Evaluación de Desempeño")
public class SesionYAsistenciaTest {

    // Sesiones de entrenamiento: convoca socios individualmente y por equipo evitando duplicados.
    @Test
    @DisplayName("Convoca deportistas sin duplicados a la sesión de entrenamiento")
    void testConvocatoriaSinDuplicados() {
        Entrenador ent = new Entrenador("E1", "Carlos", "DT", "carlos", "123", 0, "EMP-1");
        SesionEntrenamiento sesion = new SesionEntrenamiento("SE-10", "2026-11-16", "10:00", 90, ent);

        Socio s1 = new Socio("S1", "Ana", "Gómez", "ana", "123", 0, 20, true);
        Socio s2 = new Socio("S2", "Luis", "Paz", "luis", "123", 0, 20, true);
        Deporte futbol = new Deporte("Fútbol", 11, null, true, 3, 0, 1);
        Equipo equipo = new Equipo("Séneca FC", 18, 21, futbol);
        equipo.agregarSocio(s1);
        equipo.agregarSocio(s2);

        sesion.insertarSocio(s1);
        sesion.insertarEquipo(equipo);

        assertEquals(2, sesion.getEntrenamientoSocios().size());
    }

    // Evaluación de desempeño: acepta 0 (sin nota) o notas dentro del rango [1.0, 5.0].
    @ParameterizedTest(name = "Nota={0} -> Valida={1}")
    @CsvSource({
        "-1.0, false",
        "0.0, true",
        "0.9, false",
        "1.0, true",
        "5.0, true",
        "5.1, false"
    })
    @DisplayName("Valida rango permitido para la nota de desempeño")
    void testValidacionNotaDesempeno(double nota, boolean esperado) {
        assertEquals(esperado, AsistenciaEvaluacion.esNotaValida(nota));
    }
}
