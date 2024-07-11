package br.ufes.controller.handler;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.auth0.jwt.exceptions.TokenExpiredException;

import exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	private ResponseEntity<String> exceptionHandler(Exception exception) {
		return retornarErroNaoTratado(exception);
	}

	@ExceptionHandler(RuntimeException.class)
	private ResponseEntity<String> runtimeExceptionHandler(RuntimeException exception) {
		return retornarErroNaoTratado(exception);
	}

	@ExceptionHandler(BusinessException.class)
	private ResponseEntity<String> businessExceptionHandler(BusinessException exception) {
		return ResponseEntity.badRequest().body(exception.getMessage());
	}

	@ExceptionHandler(InternalAuthenticationServiceException.class)
	private ResponseEntity<String> internalAuthenticationServiceExceptionHandler(
			InternalAuthenticationServiceException exception) {
		return ResponseEntity.badRequest().body("Não foi possível se autenticar com usuário e senha informado!");
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<String> tokenExpiredExceptionHandler(TokenExpiredException exception) {
		return ResponseEntity.badRequest().body("O token de autenticação expirou!");
	}

	private ResponseEntity<String> retornarErroNaoTratado(Exception exception) {
		UUID identificadorError = UUID.randomUUID();
		String mensagemError = "Ocorreu um erro inesperado, entre em contato com o Suporte. Código do erro: "
				+ identificadorError;

		log.error(mensagemError);
		exception.printStackTrace();
		return ResponseEntity.internalServerError().body(mensagemError);
	}
}
