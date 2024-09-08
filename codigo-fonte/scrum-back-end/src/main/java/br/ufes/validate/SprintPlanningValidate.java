package br.ufes.validate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.SprintPlanningDTO;
import br.ufes.entity.ItemBacklogPlanejamento;
import br.ufes.entity.ItemBacklogProjeto;
import br.ufes.entity.ProjetoUsuario;
import br.ufes.entity.Sprint;
import br.ufes.entity.Usuario;
import br.ufes.enums.SituacaoSprintEnum;
import br.ufes.exception.BusinessException;
import br.ufes.services.SprintService;

@Component
public class SprintPlanningValidate {

	@Autowired
	public SprintService sprintService;
	
	@Autowired
	public EventoValidate eventoValidate;

	
	public void validateSavePlanning(Sprint sprint, SprintPlanningDTO planningDTO, boolean isUpdate) {
		List<String> erros = new ArrayList<>();
		var nomeEvento = "Planning";
		
		if (!isUpdate && !ObjectUtils.isEmpty(sprint.getPlanning())) {
			throw new BusinessException(nomeEvento + " já cadastrada para na sprint");
		} else if(isUpdate && ObjectUtils.isEmpty(sprint.getPlanning())) {
			throw new BusinessException(nomeEvento + " não cadastrada na sprint");
		}

		List<Usuario> timeProjeto = sprint.getProjeto().getTime().stream()
				.filter(projUsr -> projUsr.isAtivo() && projUsr.getUsuario().isAtivo()).map(ProjetoUsuario::getUsuario)
				.collect(Collectors.toList());
		List<ItemBacklogProjeto> itensPlanejamento = !ObjectUtils.isEmpty(sprint.getBacklogPlanejamento())
				? sprint.getBacklogPlanejamento().stream().map(ItemBacklogPlanejamento::getItemBacklogProjeto).collect(
						Collectors.toList())
				: new ArrayList<>();
		var listaCapacidadeDTO = planningDTO.getCapacidadeTime();
		var listaIdsParticipantesDTO = planningDTO.getIdsParticipantes();
		var listaItensDTO = planningDTO.getItensSelecionados();

		var listaSituacaoImutavelSprint = List.of(SituacaoSprintEnum.CONCLUIDA, SituacaoSprintEnum.CANCELADA);
		if (listaSituacaoImutavelSprint.contains(sprint.getSituacao())) {
			throw new BusinessException("A sprint possui a situação " + sprint.getSituacao().getSituacao());
		}

		eventoValidate.validateCamposEvento(planningDTO, erros);
		eventoValidate.validateParticipantesEvento(erros, timeProjeto, listaIdsParticipantesDTO);

		if (!ObjectUtils.isEmpty(listaCapacidadeDTO)) {
			var possuiCapacidadeTimeInvalida = listaCapacidadeDTO.stream()
					.anyMatch(capacidadeTime -> ObjectUtils.isEmpty(capacidadeTime.getIdUsuario())
							|| ObjectUtils.isEmpty(capacidadeTime.getHoras()) || BigDecimal.ZERO.compareTo(capacidadeTime.getHoras()) >= 0);
			if (possuiCapacidadeTimeInvalida) {
				erros.add("O usuário e horas devem ser informados em todos os registros de capacidade do time");
			} else {
				var listaUsuariosInvalidos = listaCapacidadeDTO.stream().filter(
						capacidade -> timeProjeto.stream().noneMatch(u -> u.getId().equals(capacidade.getIdUsuario())))
						.map(u -> u.getIdUsuario().toString()).collect(Collectors.toList());
				if (listaUsuariosInvalidos.size() == 1) {
					erros.add("Capacidade sprint: O usuário " + listaUsuariosInvalidos.get(0) + " não é membro ativo do projeto");
				} else if (listaUsuariosInvalidos.size() > 1) {
					erros.add("Capacidade sprint: Os Usuários " + listaUsuariosInvalidos.stream().collect(Collectors.joining(", "))
							+ " não são membros ativos do projeto");
				}
			}
		}

		if (!ObjectUtils.isEmpty(listaItensDTO)) {
			var possuiItemInvalido = listaItensDTO.stream()
					.anyMatch(item -> ObjectUtils.isEmpty(item.getIdItemBacklogProjeto()));
			if (possuiItemInvalido) {
				erros.add("O item backlog do projeto deve ser informado em todos os itens selecionados");
			} else {
				var listaItensInvalidos = listaItensDTO.stream()
						.filter(itemDTO -> itensPlanejamento.stream()
								.noneMatch(ibp -> ibp.getId().equals(itemDTO.getIdItemBacklogProjeto())))
						.map(i -> i.getIdItemBacklogProjeto().toString()).collect(Collectors.toList());
				if (listaItensInvalidos.size() == 1) {
					erros.add("Item backlog inválido: O item " + listaItensInvalidos.get(0)
							+ " não está listado no backlog de planejamento da sprint");
				} else if (listaItensInvalidos.size() > 1) {
					erros.add("Item backlog inválido: Os itens " + listaItensInvalidos.stream().collect(Collectors.joining(", "))
							+ " não estão listados no backlog de planejamento da sprint");
				}
				
				var listaUsuariosInvalidos = listaItensDTO.stream().filter(
						itemDTO -> timeProjeto.stream().noneMatch(u -> u.getId().equals(itemDTO.getIdResponsavelRealizacao())))
						.map(itemDTO -> itemDTO.getIdResponsavelRealizacao().toString()).collect(Collectors.toList());
				if (listaUsuariosInvalidos.size() == 1) {
					erros.add("Item backlog inválido: O usuário " + listaUsuariosInvalidos.get(0) + " não é membro ativo do projeto");
				} else if (listaUsuariosInvalidos.size() > 1) {
					erros.add("Item backlog inválido: Os Usuários " + listaUsuariosInvalidos.stream().collect(Collectors.joining(", "))
							+ " não são membros ativos do projeto");
				}
			}
		}

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}

	public void validateConcluirPlanning(Sprint sprint) {
		List<String> erros = new ArrayList<>();
		var planning = sprint.getPlanning();
		
		if(!SituacaoSprintEnum.EM_PLANEJAMENTO.equals(sprint.getSituacao())) {
			throw new BusinessException("A sprint não está " + SituacaoSprintEnum.EM_PLANEJAMENTO.getSituacao());
		}
		
		if(ObjectUtils.isEmpty(planning.getParticipantes())) {
			erros.add("Informe os participantes da planning");
		}
		
		if(ObjectUtils.isEmpty(planning.getCapacidadeTime())) {
			erros.add("Informe na planning a capacidade do time na sprint");
		}
		
		if(ObjectUtils.isEmpty(planning.getItensSelecionados())) {
			erros.add("Informe na planning os itens selecionados do planejamento para a sprint");
		}
		
		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}

}
