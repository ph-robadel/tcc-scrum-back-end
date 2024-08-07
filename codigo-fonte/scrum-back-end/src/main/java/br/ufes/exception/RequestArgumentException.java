package br.ufes.exception;

public class RequestArgumentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RequestArgumentException(String mensagem) {
		super(mensagem);
	}

}
