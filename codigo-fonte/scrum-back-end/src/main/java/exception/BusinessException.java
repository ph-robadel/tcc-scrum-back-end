package exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private List<String> erros = new ArrayList<>();

	public BusinessException(String mensagem) {
		super(mensagem);
		this.erros.add(mensagem);
	}

	public BusinessException(List<String> mensagens) {
		super(obterMensagemFormatada(mensagens));
		this.erros.addAll(mensagens);
	}

	private static String obterMensagemFormatada(List<String> mensagens) {
		return mensagens != null ? mensagens.stream().collect(Collectors.joining("; ")) : "";
	}

	public List<String> getErros() {
		return this.erros;
	}

}
