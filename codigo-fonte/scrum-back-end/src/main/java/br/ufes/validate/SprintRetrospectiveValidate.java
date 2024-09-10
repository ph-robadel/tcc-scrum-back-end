package br.ufes.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.SprintRetrospectiveDTO;
import br.ufes.entity.ProjetoUsuario;
import br.ufes.entity.Sprint;
import br.ufes.entity.Usuario;
import br.ufes.exception.BusinessException;
import br.ufes.services.SprintService;

@Component
public class SprintRetrospectiveValidate {

	@Autowired
	public SprintService sprintService;

	@Autowired
	public EventoValidate eventoValidate;

	public void validateSaveRetrospective(Sprint sprint, SprintRetrospectiveDTO retrospectiveDTO, boolean isUpdate) {
		List<String> erros = new ArrayList<>();

		if (!isUpdate && !ObjectUtils.isEmpty(sprint.getRetrospective())) {
			throw new BusinessException("Retrospective já cadastrada para na sprint");
		} else if (isUpdate && ObjectUtils.isEmpty(sprint.getRetrospective())) {
			throw new BusinessException("Retrospective não cadastrada na sprint");
		}

		List<Usuario> timeProjeto = sprint.getProjeto().getTime().stream()
				.filter(projUsr -> projUsr.isAtivo() && projUsr.getUsuario().isAtivo()).map(ProjetoUsuario::getUsuario)
				.collect(Collectors.toList());

		var listaIdsParticipantesDTO = retrospectiveDTO.getIdsParticipantes();

		eventoValidate.validateCamposEvento(retrospectiveDTO, erros);
		eventoValidate.validateParticipantesEvento(erros, timeProjeto, listaIdsParticipantesDTO);

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}

}
