package br.ufes.controller.handler;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import exception.BusinessException;

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
	private ResponseEntity<BadRequestHandlerDTO> businessExceptionHandler(BusinessException exception) {
		var handlerDTO = new BadRequestHandlerDTO(exception.getErros());
		return ResponseEntity.badRequest().body(handlerDTO);
	}
	
	
	private ResponseEntity<String> retornarErroNaoTratado(Exception exception) {
		UUID identificadorError = UUID.randomUUID();
		String mensagemError = "Ocorreu um erro inesperado, entre em contato com o Suporte. Código do erro: "
				+ identificadorError;

		System.out.println(mensagemError);
		exception.printStackTrace();
		return ResponseEntity.internalServerError().body(mensagemError);
	}
}
