package br.ufes.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ObjectUtils;

import br.ufes.dto.EventoDTO;
import br.ufes.entity.Evento;
import br.ufes.entity.EventoUsuario;
import br.ufes.entity.Usuario;

public class EventoService {
	
	public void atualizarParticipantesEvento(EventoDTO eventoDTO, List<Usuario> timeProjeto,
			Evento evento) {
		
		if(evento.getParticipantes() == null) {
			evento.setParticipantes(new ArrayList<>());
		}else {
			evento.getParticipantes().clear();
		}
		if (!ObjectUtils.isEmpty(eventoDTO.getIdsParticipantes())) {
			eventoDTO.getIdsParticipantes().stream().forEach(idParticipante -> {
				var usuario = timeProjeto.stream().filter(u -> u.getId().equals(idParticipante)).findFirst();
				var eventoUsuario = new EventoUsuario(evento, usuario.get());
				evento.getParticipantes().add(eventoUsuario);
			});
		}
	}
}
