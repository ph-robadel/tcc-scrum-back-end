package br.ufes.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ItemBacklogSprintBasicDTO;
import br.ufes.dto.filter.ItemBacklogSprintFilterDTO;
import br.ufes.entity.ItemBacklogSprint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ItemBacklogSprintRepositoryImpl {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private ModelMapper modelMapper;

	public List<ItemBacklogSprintBasicDTO> search(ItemBacklogSprintFilterDTO filterDTO) {
		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT ibs ");
		searchFrom(filterDTO, sqlBuider, params);

		if (!ObjectUtils.isEmpty(filterDTO.getFieldSort()) && !ObjectUtils.isEmpty(filterDTO.getSortOrder())) {
			sqlBuider.append(" ORDER BY " + filterDTO.getFieldSort() + " " + filterDTO.getSortOrder().name());
		}

		var query = manager.createQuery(sqlBuider.toString(), ItemBacklogSprint.class);
		params.forEach(query::setParameter);

		query.setFirstResult(filterDTO.getPage() * filterDTO.getSize());
		query.setMaxResults(filterDTO.getSize());
		var result = query.getResultList();
		
		return result.stream().map(item -> modelMapper.map(item, ItemBacklogSprintBasicDTO.class)).collect(Collectors.toList());
	}

	public Long searchCount(ItemBacklogSprintFilterDTO filterDTO) {
		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT count(1) ");
		searchFrom(filterDTO, sqlBuider, params);

		var query = manager.createQuery(sqlBuider.toString(), Long.class);
		params.forEach(query::setParameter);

		var result = query.getResultList();
		return !ObjectUtils.isEmpty(result) ? result.get(0) : 0l;
	}

	private void searchFrom(ItemBacklogSprintFilterDTO filterDTO, StringBuilder sqlBuider,
			HashMap<String, Object> params) {
		sqlBuider.append(" FROM ItemBacklogSprint ibs ");
		sqlBuider.append("      JOIN ibs.itemBacklogProjeto ibp");
		sqlBuider.append("      JOIN ibs.sprint sprint ");
		sqlBuider.append("      LEFT JOIN ibs.responsavelRealizacao responsavel ");
		sqlBuider.append(" WHERE 1=1 ");

		if (!ObjectUtils.isEmpty(filterDTO.getIdSprint())) {
			sqlBuider.append(" and sprint.id = :idSprint ");
			params.put("idSprint", filterDTO.getIdSprint());
		}

		if (!ObjectUtils.isEmpty(filterDTO.getTitulo())) {
			sqlBuider.append(" and ibp.titulo ilike :titulo ");
			params.put("titulo", "%" + filterDTO.getTitulo() + "%");
		}

		if (!ObjectUtils.isEmpty(filterDTO.getSituacao())) {
			sqlBuider.append(" and ibs.situacao = :situacao ");
			params.put("situacao", filterDTO.getSituacao());
		}

		if (!ObjectUtils.isEmpty(filterDTO.getIdResponsavelRealizacao())) {
			sqlBuider.append(" and responsavel.id = :idResponsavelRealizacao ");
			params.put("idResponsavelRealizacao", filterDTO.getIdResponsavelRealizacao());
		}

	}

	public Long obterNumeroPrioridadeNovoItem(Long idSprint) {
		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT max(ibs.prioridade) ");
		sqlBuider.append(" FROM ItemBacklogSprint ibs ");
		sqlBuider.append(" WHERE ibs.sprint.id = :idSprint ");

		params.put("idSprint", idSprint);

		var query = manager.createQuery(sqlBuider.toString(), Long.class);
		params.forEach(query::setParameter);

		var result = query.getResultList();

		return !ObjectUtils.isEmpty(result) && !ObjectUtils.isEmpty(result.get(0)) ? result.get(0) + 1l : 1l;
	}

	public void aumentarPrioridadeItem(Long idItemBacklogSprint, Long antigaPrioridade, Long novaPrioridade) {
		var hqlBuilder = new StringBuilder();
		hqlBuilder.append(" update ");
		hqlBuilder.append(" 	ItemBacklogSprint ibs ");
		hqlBuilder.append(" set ");
		hqlBuilder.append(" 	ibs.prioridade = ibs.prioridade + 1 ");
		hqlBuilder.append(" where ");
		hqlBuilder.append(" 	ibs.prioridade < :antiga ");
		hqlBuilder.append(" 	and ibs.prioridade >= :nova ");
		hqlBuilder.append(" 	and ibs.sprint.id = ( ");
		hqlBuilder.append(" 	select ");
		hqlBuilder.append(" 		ibsSub.sprint.id ");
		hqlBuilder.append(" 	from ");
		hqlBuilder.append(" 		ItemBacklogSprint ibsSub ");
		hqlBuilder.append(" 	where ");
		hqlBuilder.append(" 		ibsSub.id = :idItemBacklogSprint) ");
		executeUpdatePrioridade(idItemBacklogSprint, antigaPrioridade, novaPrioridade, hqlBuilder.toString());
		updateItem(idItemBacklogSprint, novaPrioridade);
	}

	public void diminuirPrioridadeItem(Long idItemBacklogSprint, Long antigaPrioridade, Long novaPrioridade) {
		var hqlBuilder = new StringBuilder();
		hqlBuilder.append(" update ");
		hqlBuilder.append(" 	ItemBacklogSprint ibs ");
		hqlBuilder.append(" set ");
		hqlBuilder.append(" 	ibs.prioridade = ibs.prioridade - 1 ");
		hqlBuilder.append(" where ");
		hqlBuilder.append(" 	ibs.prioridade > :antiga ");
		hqlBuilder.append(" 	and ibs.prioridade <= :nova ");
		hqlBuilder.append(" 	and ibs.sprint.id = ( ");
		hqlBuilder.append(" 	select ");
		hqlBuilder.append(" 		ibsSub.sprint.id ");
		hqlBuilder.append(" 	from ");
		hqlBuilder.append(" 		ItemBacklogSprint ibsSub ");
		hqlBuilder.append(" 	where ");
		hqlBuilder.append(" 		ibsSub.id = :idItemBacklogSprint) ");

		executeUpdatePrioridade(idItemBacklogSprint, antigaPrioridade, novaPrioridade, hqlBuilder.toString());
		updateItem(idItemBacklogSprint, novaPrioridade);
	}

	public void repriorizarDeleteItem(Long idItemBacklogSprint) {
		var hqlBuilder = new StringBuilder();
		hqlBuilder.append(" UPDATE ItemBacklogSprint ibs ");
		hqlBuilder.append(" SET ibs.prioridade = ibs.prioridade - 1 ");
		hqlBuilder.append(" WHERE ibs.sprint.id = (");
		hqlBuilder.append("     SELECT ibsSub.sprint.id ");
		hqlBuilder.append("     FROM ItemBacklogSprint ibsSub ");
		hqlBuilder.append("     WHERE ibsSub.id = :idItemBacklogSprint ");
		hqlBuilder.append(" ) ");
		hqlBuilder.append(" AND ibs.prioridade > (");
		hqlBuilder.append("     SELECT ibsSub.prioridade ");
		hqlBuilder.append("     FROM ItemBacklogSprint ibsSub ");
		hqlBuilder.append("     WHERE ibsSub.id = :idItemBacklogSprint ");
		hqlBuilder.append(" )");

		var updatePrioridadeQuery = manager.createQuery(hqlBuilder.toString());
		updatePrioridadeQuery.setParameter("idItemBacklogSprint", idItemBacklogSprint);
		updatePrioridadeQuery.executeUpdate();
	}

	private void executeUpdatePrioridade(Long idItemBacklogSprint, Long antigaPrioridade, Long novaPrioridade,
			String hql) {
		var query = manager.createQuery(hql);
		query.setParameter("idItemBacklogSprint", idItemBacklogSprint);
		query.setParameter("antiga", antigaPrioridade);
		query.setParameter("nova", novaPrioridade);
		query.executeUpdate();
	}

	private void updateItem(Long idItemBacklogSprint, Long novaPrioridade) {
		var updatePrioridadeItem = " UPDATE ItemBacklogSprint ibs SET ibs.prioridade = :nova WHERE ibs.id = :idItem ";
		var updatePrioridadeQuery = manager.createQuery(updatePrioridadeItem);
		updatePrioridadeQuery.setParameter("idItem", idItemBacklogSprint);
		updatePrioridadeQuery.setParameter("nova", novaPrioridade);
		updatePrioridadeQuery.executeUpdate();
	}
}
