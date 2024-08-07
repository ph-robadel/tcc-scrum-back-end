package br.ufes.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.filter.UsuarioFilterDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class UsuarioRepositoryImpl {

	@PersistenceContext
	private EntityManager manager;

	public List<UsuarioResponseDTO> search(UsuarioFilterDTO usuarioFilterDTO) {

		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT new br.ufes.dto.UsuarioResponseDTO (");
		sqlBuider.append("   u.id, ");
		sqlBuider.append("   u.nomeCompleto, ");
		sqlBuider.append("   u.nomeUsuario, ");
		sqlBuider.append("   u.email, ");
		sqlBuider.append("   u.ativo, ");
		sqlBuider.append("   u.perfil");
		sqlBuider.append(" ) ");

		searchFrom(usuarioFilterDTO, sqlBuider, params);

		if (!ObjectUtils.isEmpty(usuarioFilterDTO.getFieldSort())
				&& !ObjectUtils.isEmpty(usuarioFilterDTO.getSortOrder())) {
			sqlBuider.append(
					" ORDER BY " + usuarioFilterDTO.getFieldSort() + " " + usuarioFilterDTO.getSortOrder().name());
		}

		var query = manager.createQuery(sqlBuider.toString(), UsuarioResponseDTO.class);
		params.forEach(query::setParameter);

		query.setFirstResult(usuarioFilterDTO.getPage() * usuarioFilterDTO.getSize());
		query.setMaxResults(usuarioFilterDTO.getSize());
		return query.getResultList();
	}

	public Long searchCount(UsuarioFilterDTO usuarioFilterDTO) {

		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT count(1) ");
		searchFrom(usuarioFilterDTO, sqlBuider, params);

		var query = manager.createQuery(sqlBuider.toString(), Long.class);
		params.forEach(query::setParameter);

		var result = query.getResultList();
		return !ObjectUtils.isEmpty(result) ? result.get(0) : 0l;
	}

	private void searchFrom(UsuarioFilterDTO usuarioFilterDTO, StringBuilder sqlBuider,
			HashMap<String, Object> params) {
		sqlBuider.append(" FROM ");
		sqlBuider.append("   Usuario u ");
		sqlBuider.append(" WHERE 1=1 ");

		if (!ObjectUtils.isEmpty(usuarioFilterDTO.getNome())) {
			sqlBuider.append(" and u.nomeCompleto ilike :nome ");
			params.put("nome", "%" + usuarioFilterDTO.getNome() + "%");
		}

		if (Boolean.TRUE.equals(usuarioFilterDTO.getApenasAtivo())) {
			sqlBuider.append(" and u.ativo = true");
		}

		if (!ObjectUtils.isEmpty(usuarioFilterDTO.getPerfil())) {
			sqlBuider.append(" and u.perfil = :perfil");
			params.put("perfil", usuarioFilterDTO.getPerfil());
		}
	}

}
