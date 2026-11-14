package Deportes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import Usuarios.Socio;

@DisplayName("Progresión de Nivel y Ranking Individual")
public class InscripcionIndividualTest {

    // Ranking individual: Actualiza el nivel (Principiante < 4, Intermedio [4, 10), Avanzado >= 10) según puntaje.
    @ParameterizedTest(name = "Puntaje={0} -> Nivel={1}, Posicion={2}")
    @CsvSource({
        "3.9, Principiante, 0",
        "4.0, Intermedio, 1",
        "9.9, Intermedio, 1",
        "10.0, Avanzado, 2"
    })
    @DisplayName("Actualiza nivel y jerarquía según umbrales de puntaje")
    void testActualizacionNivelPorPuntaje(double puntaje, String nivelEsperado, int posicionEsperada) {
        Socio socio = new Socio("SOC-20", "Elena", "Mora", "elena.m", "123", 0, 22, true);
        Deporte tenis = new Deporte("Tenis", 1, null, false, 2, 0, 0);
        InscripcionIndividual inscripcion = new InscripcionIndividual(socio, tenis, InscripcionIndividual.PRINCIPIANTE, 0.0);

        inscripcion.sumarPuntosRanking(puntaje);

        assertEquals(nivelEsperado, inscripcion.getNivel());
        assertEquals(posicionEsperada, inscripcion.posicionNivel());
    }
}
