package br.ufes.exception;

public class AcessoUsuarioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AcessoUsuarioException(String mensagem) {
		super(mensagem);
	}
}
