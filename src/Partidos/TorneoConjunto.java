package Partidos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import Deportes.Deporte;
import Deportes.Equipo;
import Usuarios.Entrenador;

public class TorneoConjunto extends Torneo {
    private String disciplina;
    private List<Equipo> equiposInscritos;
    private Set<String> equiposCupoPreferencial;
    private Map<String, Entrenador> entrenadoresInscriptores;

    public TorneoConjunto(String idTorneo, String nombre, Deporte deporte, int categoria, int cuposTotales) {
        super(idTorneo, nombre, deporte, categoria, cuposTotales);
        this.disciplina = deporte != null ? deporte.getNombreDeporte() : nombre;
        this.equiposInscritos = new ArrayList<>();
        this.equiposCupoPreferencial = new HashSet<>();
        this.entrenadoresInscriptores = new HashMap<>();
    }

    // Constructor compatible con la firma previa
    public TorneoConjunto(Deporte deporte, String idTorneo, String disciplina) {
        this(idTorneo, disciplina, deporte, 99, 10);
        this.disciplina = disciplina;
    }

    public boolean estaEquipoInscrito(Equipo equipo) {
        return equipo != null && equiposInscritos.contains(equipo);
    }

    public void inscribirEquipo(Equipo equipo, Entrenador entrenador, boolean esPreferencial) {
        if (esPreferencial) {
            if (cuposPreferenciales <= 0) {
                throw new IllegalArgumentException("No quedan cupos preferenciales disponibles en el torneo");
            }
            cuposPreferenciales--;
            equiposCupoPreferencial.add(equipo.getNombreEquipo());
        } else {
            if (cuposGenerales <= 0) {
                throw new IllegalArgumentException("No quedan cupos generales disponibles en el torneo");
            }
            cuposGenerales--;
        }
        equiposInscritos.add(equipo);
        tablaPosiciones.put(equipo.getNombreEquipo(), 0);
        if (entrenador != null) {
            entrenadoresInscriptores.put(equipo.getNombreEquipo(), entrenador);
        }
        
        cobrarInscripcion();
    }

    public String getDisciplina() { return disciplina; }
    public void setDisciplina(String disciplina) { this.disciplina = disciplina; }
    public List<Equipo> getEquiposInscritos() { return equiposInscritos; }
    public Set<String> getEquiposCupoPreferencial() { return equiposCupoPreferencial; }
    public Map<String, Entrenador> getEntrenadoresInscriptores() { return entrenadoresInscriptores; }
}