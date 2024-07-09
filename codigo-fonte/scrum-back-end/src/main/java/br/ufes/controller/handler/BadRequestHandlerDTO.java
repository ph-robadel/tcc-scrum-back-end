package br.ufes.controller.handler;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BadRequestHandlerDTO {
	private List<String> erros;
}
