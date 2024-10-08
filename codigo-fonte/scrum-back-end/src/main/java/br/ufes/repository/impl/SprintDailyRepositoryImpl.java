package br.ufes.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.SprintDailyBasicDTO;
import br.ufes.dto.filter.SprintDailyFilterDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class SprintDailyRepositoryImpl {

	@PersistenceContext
	private EntityManager manager;

	public List<SprintDailyBasicDTO> search(SprintDailyFilterDTO filterDTO) {

		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT distinct new br.ufes.dto.SprintDailyBasicDTO (");
		sqlBuider.append("   sd.id, ");
		sqlBuider.append("   sd.nome, ");
		sqlBuider.append("   sd.inicio ");
		sqlBuider.append(" ) ");

		searchFrom(filterDTO, sqlBuider, params);

		if (!ObjectUtils.isEmpty(filterDTO.getFieldSort()) && !ObjectUtils.isEmpty(filterDTO.getSortOrder())) {
			sqlBuider.append(" ORDER BY sd." + filterDTO.getFieldSort() + " " + filterDTO.getSortOrder().name());
		}

		var query = manager.createQuery(sqlBuider.toString(), SprintDailyBasicDTO.class);
		params.forEach(query::setParameter);

		query.setFirstResult(filterDTO.getPage() * filterDTO.getSize());
		query.setMaxResults(filterDTO.getSize());
		return query.getResultList();
	}

	public Long searchCount(SprintDailyFilterDTO filterDTO) {

		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT count(1) ");
		searchFrom(filterDTO, sqlBuider, params);

		var query = manager.createQuery(sqlBuider.toString(), Long.class);
		params.forEach(query::setParameter);

		var result = query.getResultList();
		return !ObjectUtils.isEmpty(result) ? result.get(0) : 0l;
	}

	private void searchFrom(SprintDailyFilterDTO filterDTO, StringBuilder sqlBuider, HashMap<String, Object> params) {
		sqlBuider.append(" FROM ");
		sqlBuider.append("   SprintDaily sd ");
		sqlBuider.append("   join sd.sprint s ");
		sqlBuider.append(" WHERE 1=1 ");

		if ( !ObjectUtils.isEmpty(filterDTO.getIdSprint())) {
			sqlBuider.append(" and s.id = :idSprint");
			params.put("idSprint", filterDTO.getIdSprint());
		}

	}

}
