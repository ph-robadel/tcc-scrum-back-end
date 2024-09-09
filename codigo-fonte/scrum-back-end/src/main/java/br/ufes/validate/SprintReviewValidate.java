package br.ufes.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ItemReviewDTO;
import br.ufes.dto.SprintReviewDTO;
import br.ufes.entity.ItemBacklogProjeto;
import br.ufes.entity.ItemBacklogSprint;
import br.ufes.entity.ProjetoUsuario;
import br.ufes.entity.Sprint;
import br.ufes.entity.Usuario;
import br.ufes.enums.SituacaoSprintEnum;
import br.ufes.exception.BusinessException;
import br.ufes.services.SprintService;

@Component
public class SprintReviewValidate {

	@Autowired
	public SprintService sprintService;

	@Autowired
	public EventoValidate eventoValidate;

	public void validateSaveReview(Sprint sprint, SprintReviewDTO reviewDTO, boolean isUpdate) {
		List<String> erros = new ArrayList<>();

		if (!isUpdate && !ObjectUtils.isEmpty(sprint.getPlanning())) {
			throw new BusinessException("Review já cadastrada para na sprint");
		} else if (isUpdate && ObjectUtils.isEmpty(sprint.getPlanning())) {
			throw new BusinessException("Review não cadastrada na sprint");
		}

		List<Usuario> timeProjeto = sprint.getProjeto().getTime().stream()
				.filter(projUsr -> projUsr.isAtivo() && projUsr.getUsuario().isAtivo()).map(ProjetoUsuario::getUsuario)
				.collect(Collectors.toList());

		List<ItemBacklogProjeto> itensSprint = !ObjectUtils.isEmpty(sprint.getBacklogPlanejamento()) ? sprint
				.getBacklog().stream().map(ItemBacklogSprint::getItemBacklogProjeto).collect(Collectors.toList())
				: new ArrayList<>();

		var listaSituacaoImutavelSprint = List.of(SituacaoSprintEnum.CONCLUIDA, SituacaoSprintEnum.CANCELADA);
		if (listaSituacaoImutavelSprint.contains(sprint.getSituacao())) {
			throw new BusinessException("A sprint possui a situação " + sprint.getSituacao().getSituacao());
		}

		var listaIdsParticipantesDTO = reviewDTO.getIdsParticipantes();

		eventoValidate.validateCamposEvento(reviewDTO, erros);
		eventoValidate.validateParticipantesEvento(erros, timeProjeto, listaIdsParticipantesDTO);

		validateItensReview(erros, itensSprint, reviewDTO.getItensAprovados(), "aprovados");
		validateItensReview(erros, itensSprint, reviewDTO.getItensRejeitados(), "rejeitados");
		validateItensReview(erros, itensSprint, reviewDTO.getItensAprovadosParcialmente(), "aprovados parcialmente");

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}

	private void validateItensReview(List<String> erros, List<ItemBacklogProjeto> itensSprint,
			List<ItemReviewDTO> listaItensDTO, String descricaoItem) {
		if (!ObjectUtils.isEmpty(listaItensDTO)) {
			var possuiItemInvalido = listaItensDTO.stream()
					.anyMatch(item -> ObjectUtils.isEmpty(item.getIdItemBacklogProjeto()));
			if (possuiItemInvalido) {
				erros.add("O item backlog do projeto deve ser informado em todos os itens " + descricaoItem);
			} else {
				var listaItensInvalidos = listaItensDTO.stream()
						.filter(itemDTO -> itensSprint.stream()
								.noneMatch(ibp -> ibp.getId().equals(itemDTO.getIdItemBacklogProjeto())))
						.map(i -> i.getIdItemBacklogProjeto().toString()).collect(Collectors.toList());
				if (listaItensInvalidos.size() == 1) {
					erros.add("Itens " + descricaoItem + " inválido: O item " + listaItensInvalidos.get(0)
							+ " não está listado no backlog da sprint");
				} else if (listaItensInvalidos.size() > 1) {
					erros.add("Itens " + descricaoItem + " inválido: Os itens "
							+ listaItensInvalidos.stream().collect(Collectors.joining(", "))
							+ " não estão listados no backlog de planejamento da sprint");
				}
			}
		}
	}

}
