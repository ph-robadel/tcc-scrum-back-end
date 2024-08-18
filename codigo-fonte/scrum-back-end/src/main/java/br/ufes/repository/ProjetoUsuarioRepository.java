package br.ufes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.filter.ProjetoUsuarioFilterDTO;
import br.ufes.entity.ProjetoUsuario;

@Repository
public interface ProjetoUsuarioRepository extends JpaRepository<ProjetoUsuario, Long> {

	ProjetoUsuario getByIdProjetoAndIdUsuario(Long idProjeto, Long idUsuario);

	List<UsuarioResponseDTO> search(ProjetoUsuarioFilterDTO filterDTO);

	Long searchCount(ProjetoUsuarioFilterDTO filterDTO);

}
