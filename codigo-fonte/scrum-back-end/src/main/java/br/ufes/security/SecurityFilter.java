package br.ufes.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;

import br.ufes.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			var token = recoverToken(request);
			if (token != null) {
				var nomeUsuario = tokenService.validarToken(token);
				var usuario = usuarioRepository.findByNomeUsuario(nomeUsuario);
				if (!Boolean.TRUE.equals(usuario.getAtivo())) {
					throw new JWTVerificationException("Usuário inativo");
				}
				var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			filterChain.doFilter(request, response);
		} catch (JWTVerificationException exception) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(exception.getLocalizedMessage());
		}

	}

	private String recoverToken(HttpServletRequest request) {
		var authHeader = request.getHeader("Authorization");
		if (authHeader == null) {
			return null;
		}

		return authHeader.replace("Bearer ", "");
	}

}
