package br.ufes.validate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ProjetoUpsertDTO;
import br.ufes.entity.Projeto;
import br.ufes.exception.BusinessException;

@Component
public class ProjetoValidate {

	public void validateSave(ProjetoUpsertDTO projetoInsertDTO) throws Exception {
		List<String> erros = new ArrayList<>();

		if (ObjectUtils.isEmpty(projetoInsertDTO.getNome())) {
			erros.add("Informe o nome do projeto");
		}

		if (ObjectUtils.isEmpty(projetoInsertDTO.getQuantidadeDiasSprint())) {
			erros.add("Informe a quantidade de dias úteis da sprint");
		}

		if (ObjectUtils.isEmpty(projetoInsertDTO.getDuracaoHorasDaily())) {
			erros.add("Informe em horas a duração da daily");
		}

		if (ObjectUtils.isEmpty(projetoInsertDTO.getDuracaoHorasPlanning())) {
			erros.add("Informe em horas a duração da planning");
		}

		if (ObjectUtils.isEmpty(projetoInsertDTO.getDuracaoHorasReview())) {
			erros.add("Informe em horas a duração da review");
		}

		if (ObjectUtils.isEmpty(projetoInsertDTO.getDuracaoHorasRetrospective())) {
			erros.add("Informe em horas a duração da sprint retrospective");
		}

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}

	public void validateProjetoAtivo(Projeto projeto) {
		if (!ObjectUtils.isEmpty(projeto) && !projeto.isAtivo()) {
			throw new BusinessException("O projeto está inaivo");
		}
	}
}
