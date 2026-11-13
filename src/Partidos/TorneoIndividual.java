package Partidos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import Deportes.Deporte;
import Usuarios.Usuario;

public class TorneoIndividual extends Torneo {
    private List<Usuario> participantesInscritos;
    private Set<String> idsCupoPreferencial;
    private Set<String> idsCupoGeneral;

    public TorneoIndividual(String idTorneo, String nombre, Deporte deporte, int categoria, int cuposTotales) {
        super(idTorneo, nombre, deporte, categoria, cuposTotales);
        this.participantesInscritos = new ArrayList<>();
        this.idsCupoPreferencial = new HashSet<>();
        this.idsCupoGeneral = new HashSet<>();
    }

    public boolean estaParticipanteInscrito(Usuario usuario) {
        return usuario != null && participantesInscritos.contains(usuario);
    }

    public void inscribirParticipante(Usuario usuario, boolean esPreferencial) {
        if (esPreferencial) {
            if (cuposPreferenciales <= 0) {
                throw new IllegalArgumentException("No hay cupos preferenciales disponibles");
            }
            cuposPreferenciales--;
            idsCupoPreferencial.add(usuario.getIdUsuario());
        } else {
            if (cuposGenerales <= 0) {
                throw new IllegalArgumentException("No quedan cupos generales disponibles en el torneo");
            }
            cuposGenerales--;
            idsCupoGeneral.add(usuario.getIdUsuario());
        }
        participantesInscritos.add(usuario);
        tablaPosiciones.put(usuario.getIdUsuario(), 0);
    }

    public void desinscribirParticipante(Usuario usuario, boolean liberarPreferencial) {
        participantesInscritos.remove(usuario);
        tablaPosiciones.remove(usuario.getIdUsuario());
        idsCupoPreferencial.remove(usuario.getIdUsuario());
        idsCupoGeneral.remove(usuario.getIdUsuario());
        if (liberarPreferencial) {
            cuposPreferenciales++;
        } else {
            cuposGenerales++;
        }
    }

    public boolean usoCupoPreferencial(String idUsuario) {
        return idsCupoPreferencial.contains(idUsuario);
    }

    public boolean tieneRegistroTipoCupo(String idUsuario) {
        return idsCupoPreferencial.contains(idUsuario) || idsCupoGeneral.contains(idUsuario);
    }

    public List<Usuario> getParticipantesInscritos() { return participantesInscritos; }
    public Set<String> getIdsCupoPreferencial() { return idsCupoPreferencial; }
}