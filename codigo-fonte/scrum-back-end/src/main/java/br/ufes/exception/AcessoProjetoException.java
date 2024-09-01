package br.ufes.exception;

public class AcessoProjetoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AcessoProjetoException(String mensagem) {
		super(mensagem);
	}
}
