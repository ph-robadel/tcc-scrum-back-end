package br.ufes.validate;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.EventoDTO;
import br.ufes.entity.Sprint;
import br.ufes.entity.Usuario;
import br.ufes.enums.SituacaoSprintEnum;
import br.ufes.exception.BusinessException;
import br.ufes.services.SprintService;

@Component
public class EventoValidate {

	@Autowired
	public SprintService sprintService;

	public void validateCamposEvento(EventoDTO eventoDTO, List<String> erros) {
		if (ObjectUtils.isEmpty(eventoDTO.getNome())) {
			erros.add("Nome não informado");
		}
		if (ObjectUtils.isEmpty(eventoDTO.getInicio())) {
			erros.add("Data e hora de início não informado");
		}
		if(!ObjectUtils.isEmpty(eventoDTO.getInicio()) && !ObjectUtils.isEmpty(eventoDTO.getFim()) && !eventoDTO.getInicio().isBefore(eventoDTO.getFim())) {
			erros.add("Intervalo de datas inválido: A data de inicio não pode ser maior ou igual a que a data de fim");
		}
	}

	public void validateParticipantesEvento(List<String> erros, List<Usuario> timeProjeto,
			List<Long> listaIdsParticipantesDTO) {
		if (!ObjectUtils.isEmpty(listaIdsParticipantesDTO)) {
			var listaUsuariosInvalidos = listaIdsParticipantesDTO.stream()
					.filter(idParticipante -> timeProjeto.stream().noneMatch(u -> u.getId().equals(idParticipante)))
					.map(idParticipante -> idParticipante.toString()).collect(Collectors.toList());
			if (listaUsuariosInvalidos.size() == 1) {
				erros.add("Participante inválido: O usuário " + listaUsuariosInvalidos.get(0) + " não é membro ativo do projeto");
			} else if (listaUsuariosInvalidos.size() > 1) {
				erros.add("Participante inválido: Os usuários " + listaUsuariosInvalidos.stream().collect(Collectors.joining(", "))
						+ " não são membros ativos do projeto");
			}
		}
	}
	
	public void validateUpsertEvento(Sprint sprint) {
		var listaSituacaoImutavelSprint = List.of(SituacaoSprintEnum.CONCLUIDA, SituacaoSprintEnum.CANCELADA);
		if (listaSituacaoImutavelSprint.contains(sprint.getSituacao())) {
			throw new BusinessException("A sprint está " + sprint.getSituacao().getSituacao());
		}
	}

}
