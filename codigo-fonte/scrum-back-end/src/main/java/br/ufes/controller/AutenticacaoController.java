package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.domain.usuario.AuthenticationDTO;
import br.ufes.domain.usuario.LoginResponseDTO;
import br.ufes.domain.usuario.Usuario;
import br.ufes.security.TokenService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("login")
public class AutenticacaoController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	@PostMapping
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Validated AuthenticationDTO data) {
		try {
			var usernamePassword = new UsernamePasswordAuthenticationToken(data.nomeUsuario(), data.senha());
			var auth = this.authenticationManager.authenticate(usernamePassword);
			var usuario = (Usuario) auth.getPrincipal();

			String token = tokenService.gerarToken(usuario);
			return ResponseEntity.ok(new LoginResponseDTO(token));
		} catch (InternalAuthenticationServiceException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.internalServerError().build();
		}

	}
}
