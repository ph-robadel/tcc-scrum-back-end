package br.ufes.dto.filter;

import java.util.HashMap;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufes.enums.CategoriaItemProjetoEnum;
import br.ufes.enums.SituacaoItemProjetoEnum;
import br.ufes.util.BaseFilterSearch;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemBacklogProjetoFilterDTO extends BaseFilterSearch {

	private String titulo;

	@Getter(AccessLevel.NONE)
	private String codigo;

	private SituacaoItemProjetoEnum situacao;

	private CategoriaItemProjetoEnum categoria;

	private Long idAutor;

	@JsonIgnore
	private Long idProjeto;

	public ItemBacklogProjetoFilterDTO(String titulo, String codigo, String situacao, String categoria, Long idAutor) {

		this.titulo = titulo;
		this.codigo = codigo;
		this.situacao = SituacaoItemProjetoEnum.fromString(situacao);
		this.categoria = CategoriaItemProjetoEnum.fromString(categoria);
		this.idAutor = idAutor;
		if (!ObjectUtils.isEmpty(codigo)) {
			setCategoriaBySiglaCodigo(codigo);
		}
	}

	@Override
	public void setFieldSort(String nomeCampo) {
		var mapFieldSort = new HashMap<String, String>();
		var campoFormatado = nomeCampo.toLowerCase().trim();

		mapFieldSort.put("id", "id");
		mapFieldSort.put("titulo", "titulo");
		mapFieldSort.put("situacao", "situacao");
		mapFieldSort.put("categoria", "categoria");
		mapFieldSort.put("prioridade", "prioridade");
		mapFieldSort.put("autor", "autor");
		mapFieldSort.put("idUsuario", "autor");

		this.fieldSort = mapFieldSort.getOrDefault(campoFormatado, null);
	}

	public Long getCodigo() {
		if(ObjectUtils.isEmpty(codigo)) {
			return null;
		}
		var codigoSemSigla = codigo.replaceAll("\\D", "");
		if(ObjectUtils.isEmpty(codigoSemSigla)) {
			return null;
		}
		
		return Long.parseLong(codigoSemSigla);
	}

	public void setCategoriaBySiglaCodigo(String codigo) {
		if (ObjectUtils.isEmpty(codigo)) {
			return;
		}
		var sigla = codigo.replaceAll("[^\\p{L}]", "");
		this.categoria = CategoriaItemProjetoEnum.fromSigla(sigla);
	}

}
