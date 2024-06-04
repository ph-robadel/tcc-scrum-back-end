package util;

import br.ufes.enums.DirecaoOrdenacaoEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseFilterSearch {

	private Integer tamanhoPagina;
	private Integer pagina;
	private String nomeCampoOrdenacao;
	private DirecaoOrdenacaoEnum direcaoOrdenacao;
}
