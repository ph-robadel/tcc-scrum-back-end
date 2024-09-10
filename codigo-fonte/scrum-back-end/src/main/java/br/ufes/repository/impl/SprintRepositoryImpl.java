package br.ufes.repository.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.SprintBasicDTO;
import br.ufes.dto.filter.SprintFilterDTO;
import br.ufes.enums.SituacaoSprintEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class SprintRepositoryImpl {

	@PersistenceContext
	private EntityManager manager;

	public List<SprintBasicDTO> search(SprintFilterDTO filterDTO) {

		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT new br.ufes.dto.SprintBasicDTO (");
		sqlBuider.append("   s.id, ");
		sqlBuider.append("   s.numero, ");
		sqlBuider.append("   s.situacao ");
		sqlBuider.append(" ) ");

		searchFrom(filterDTO, sqlBuider, params);

		if (!ObjectUtils.isEmpty(filterDTO.getFieldSort()) && !ObjectUtils.isEmpty(filterDTO.getSortOrder())) {
			sqlBuider.append(" ORDER BY s." + filterDTO.getFieldSort() + " " + filterDTO.getSortOrder().name());
		}

		var query = manager.createQuery(sqlBuider.toString(), SprintBasicDTO.class);
		params.forEach(query::setParameter);

		query.setFirstResult(filterDTO.getPage() * filterDTO.getSize());
		query.setMaxResults(filterDTO.getSize());
		return query.getResultList();
	}

	public Long searchCount(SprintFilterDTO filterDTO) {

		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT count(1) ");
		searchFrom(filterDTO, sqlBuider, params);

		var query = manager.createQuery(sqlBuider.toString(), Long.class);
		params.forEach(query::setParameter);

		var result = query.getResultList();
		return !ObjectUtils.isEmpty(result) ? result.get(0) : 0l;
	}

	private void searchFrom(SprintFilterDTO filterDTO, StringBuilder sqlBuider, HashMap<String, Object> params) {
		sqlBuider.append(" FROM ");
		sqlBuider.append("   Sprint s ");
		sqlBuider.append("   join s.projeto p");
		sqlBuider.append(" WHERE 1=1 ");

		if (!ObjectUtils.isEmpty(filterDTO.getIdProjeto())) {
			sqlBuider.append(" and p.id = :idProjeto");
			params.put("idProjeto", filterDTO.getIdProjeto());
		}

		if (!ObjectUtils.isEmpty(filterDTO.getNumero())) {
			sqlBuider.append(" and s.numero = :numero");
			params.put("numero", filterDTO.getNumero());
		}
	}
	
	public SprintBasicDTO obterSprintByData(Long idProjeto, LocalDate data) {
		var sqlBuider = new StringBuilder();
		sqlBuider.append(" SELECT new br.ufes.dto.SprintBasicDTO ( ");
		sqlBuider.append("   s.id, ");
		sqlBuider.append("   s.numero, ");
		sqlBuider.append("   s.situacao ");
		sqlBuider.append(" ) ");
		sqlBuider.append(" FROM Sprint s ");
		sqlBuider.append(" WHERE s.dataInicio >= :data ");
		sqlBuider.append("       and s.dataFim <= :data ");
		sqlBuider.append("       and s.projeto.id = :idProjeto ");
		
		var query = manager.createQuery(sqlBuider.toString(), SprintBasicDTO.class);
		query.setParameter("data", data);
		query.setParameter("idProjeto", idProjeto);

		var result = query.getResultList();
		return !ObjectUtils.isEmpty(result) && !ObjectUtils.isEmpty(result.get(0)) ? result.get(0) : null;
	}
	
	public List<SprintBasicDTO> obterSprintsIntervaloDatas(Long idProjeto, LocalDate dataInicial, LocalDate dataFinal) {
		var sqlBuider = new StringBuilder();
		sqlBuider.append(" SELECT new br.ufes.dto.SprintBasicDTO ( ");
		sqlBuider.append("   s.id, ");
		sqlBuider.append("   s.numero, ");
		sqlBuider.append("   s.situacao ");
		sqlBuider.append(" ) ");
		sqlBuider.append(" FROM Sprint s ");
		sqlBuider.append(" WHERE s.projeto.id = :idProjeto ");
		sqlBuider.append(" 		 AND s.situacao != :cancelada ");
		sqlBuider.append(" 	     and ((s.dataInicio >= :dataInicial and s.dataInicio <= :dataFinal ) or ");
		sqlBuider.append("       (s.dataFim >= :dataInicial and s.dataFim <= :dataFinal )) ");
		
		
		var query = manager.createQuery(sqlBuider.toString(), SprintBasicDTO.class);
		query.setParameter("idProjeto", idProjeto);
		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);
		query.setParameter("cancelada", SituacaoSprintEnum.CANCELADA);
		
		return query.getResultList();
	}
	
	public Integer getProximoNumeroSprintFromProjeto(Long idProjeto) {
		var sqlBuider = new StringBuilder();
		sqlBuider.append(" SELECT  ");
		sqlBuider.append("   max(s.numero) ");
		sqlBuider.append(" FROM Sprint s ");
		sqlBuider.append(" WHERE s.projeto.id = :idProjeto ");
		
		
		var query = manager.createQuery(sqlBuider.toString(), Integer.class);
		query.setParameter("idProjeto", idProjeto);
		
		var result = query.getResultList();
		return !ObjectUtils.isEmpty(result) && !ObjectUtils.isEmpty(result.get(0)) ? result.get(0) + 1 : 1;
	}

}
