package br.ufes.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufes.dto.SprintBasicDTO;
import br.ufes.dto.SprintDTO;
import br.ufes.dto.filter.SprintFilterDTO;
import br.ufes.entity.Sprint;
import br.ufes.repository.SprintRepository;
import br.ufes.util.ResponseSearch;
import jakarta.persistence.EntityNotFoundException;

@Service
public class SprintService {

	@Autowired
	private SprintRepository sprintRepository;

	public SprintDTO getMock() throws Exception {
		var sprintMock = new SprintDTO();
		sprintMock.setId(1l);
		sprintMock.setNumero(1);
		sprintMock.setDataInicio(LocalDate.now());
		sprintMock.setDataFim(LocalDate.now().plusDays(15));

		return sprintMock;
	}

	public SprintBasicDTO getBasicMock() throws Exception {
		var sprintBasicMock = new SprintBasicDTO();
		sprintBasicMock.setId(1l);
		sprintBasicMock.setNumero(1);

		return sprintBasicMock;
	}

	public SprintBasicDTO obterSprintByData(Long idProjeto, LocalDate data) {
		return sprintRepository.obterSprintByData(idProjeto, data);
	}

	public List<SprintBasicDTO> obterSprintsIntervaloDatas(Long idProjeto, LocalDate dataInicial, LocalDate dataFinal) {
		return sprintRepository.obterSprintsIntervaloDatas(idProjeto, dataInicial, dataFinal);
	}

	public Sprint save(Sprint sprint) {
		return sprintRepository.save(sprint);
	}

	public Integer getProximoNumeroSprintFromProjeto(Long idProjeto) {
		return sprintRepository.getProximoNumeroSprintFromProjeto(idProjeto);
	}

	public Sprint getById(Long idSprint) {
		if (idSprint == null || idSprint <= 0) {
			return null;
		}

		return sprintRepository.findById(idSprint)
				.orElseThrow(() -> new EntityNotFoundException("Sprint não encontrado"));
	}

	public ResponseSearch<SprintBasicDTO> search(SprintFilterDTO sprintFiltroDTO) {
		var listPage = sprintRepository.search(sprintFiltroDTO);
		Long total = sprintRepository.searchCount(sprintFiltroDTO);

		return new ResponseSearch<>(listPage, total);
	}

	public void remove(Long idSprint) {
		sprintRepository.deleteById(idSprint);
	}

}
