package br.ufes.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.ufes.entity.Usuario;

@Service
public class TokenService {

	@Value("${api.security.token.secret}")
	private String secret;

	public String gerarToken(Usuario usuario) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token = JWT.create()
					.withIssuer("scrum-api")
					.withSubject(usuario.getNomeUsuario())
					.withExpiresAt(gerarInstantExpiracao())
					.sign(algorithm);
			return token;
		} catch (JWTCreationException ex) {
			throw new RuntimeException("Erro ao gerar token", ex);
		}
	}
	
	public String validarToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm)
					.withIssuer("scrum-api")
					.build()
					.verify(token)
					.getSubject();
		} catch (JWTVerificationException ex) {
			throw new RuntimeException("Erro ao verificar token", ex);
		}
	}

	private Instant gerarInstantExpiracao() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}

}
