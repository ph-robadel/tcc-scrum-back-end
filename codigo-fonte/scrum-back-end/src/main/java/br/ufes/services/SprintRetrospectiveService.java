package br.ufes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufes.entity.SprintRetrospective;
import br.ufes.repository.SprintRetrospectiveRepository;

@Service
public class SprintRetrospectiveService extends EventoService {

	@Autowired
	private SprintRetrospectiveRepository sprintRetrospectiveRepository;

	public SprintRetrospective save(SprintRetrospective sprintRetrospective) {
		return sprintRetrospectiveRepository.save(sprintRetrospective);
	}

}
