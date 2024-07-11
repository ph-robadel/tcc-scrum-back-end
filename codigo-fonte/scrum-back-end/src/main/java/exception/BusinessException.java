package exception;

import java.util.List;
import java.util.stream.Collectors;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BusinessException(String mensagem) {
		super(mensagem);
	}

	public BusinessException(List<String> mensagens) {
		super(obterMensagemFormatada(mensagens));
	}

	private static String obterMensagemFormatada(List<String> mensagens) {
		return mensagens != null ? mensagens.stream().collect(Collectors.joining("; ")) : "";
	}

}
