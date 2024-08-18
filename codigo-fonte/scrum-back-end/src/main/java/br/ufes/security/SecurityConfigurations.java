package br.ufes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
	
	@Autowired
	private SecurityFilter securityFilter;
	
	private static final String[] SWAGGER_PATHS = {"/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**", "/webjars/swagger-ui/**"};
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(SWAGGER_PATHS).permitAll()
						.requestMatchers(HttpMethod.POST, "/login").permitAll()
						
						.requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
						.requestMatchers(HttpMethod.GET, "/usuarios").authenticated()
						.requestMatchers(HttpMethod.GET, "/usuarios/{idUsuario}").authenticated()
						.requestMatchers(HttpMethod.PUT, "/usuarios/{idUsuario}").hasAuthority("ADMINISTRADOR")
						.requestMatchers(HttpMethod.POST, "/usuarios/{idUsuario}/inativar").hasAuthority("ADMINISTRADOR")
						.requestMatchers(HttpMethod.POST, "/usuarios/{idUsuario}/reativar").hasAuthority("ADMINISTRADOR")
						.requestMatchers(HttpMethod.PATCH, "/usuarios/{idUsuario}/atualizar-senha").hasAuthority("ADMINISTRADOR")
						.requestMatchers(HttpMethod.POST, "/usuarios/inativar").authenticated()
						.requestMatchers(HttpMethod.GET, "/usuarios/me").authenticated()
						.requestMatchers(HttpMethod.PUT, "/usuarios/me").authenticated()
						.requestMatchers(HttpMethod.PATCH, "/usuarios/atualizar-senha").authenticated()
					
						.requestMatchers(HttpMethod.POST, "/projetos").hasAnyAuthority("PRODUCT_OWNER", "SCRUM_MASTER")
						.requestMatchers(HttpMethod.PUT, "/projetos/{idProjeto}").hasAnyAuthority("PRODUCT_OWNER", "SCRUM_MASTER")
						.requestMatchers(HttpMethod.GET, "/projetos/search").authenticated()
						.requestMatchers(HttpMethod.GET, "/projetos/{idProjeto}").authenticated()
						.requestMatchers(HttpMethod.POST, "/projetos/{idProjeto}/inativar").hasAnyAuthority("PRODUCT_OWNER")
						.requestMatchers(HttpMethod.POST, "/projetos/{idProjeto}/ativar").hasAnyAuthority("PRODUCT_OWNER")
						.requestMatchers(HttpMethod.GET, "/projetos/{idProjeto}/usuarios/search").authenticated()
						.requestMatchers(HttpMethod.POST, "/projetos/{idProjeto}/usuarios/{idUsuario}").hasAnyAuthority("PRODUCT_OWNER", "SCRUM_MASTER")
						.requestMatchers(HttpMethod.DELETE, "/projetos/{idProjeto}/usuarios/{idUsuario}").hasAnyAuthority("PRODUCT_OWNER", "SCRUM_MASTER")
						.requestMatchers(HttpMethod.POST, "/projetos/{idProjeto}/item-backlog-projeto").hasAnyAuthority("PRODUCT_OWNER", "SCRUM_MASTER", "DEV_TEAM")
						.requestMatchers(HttpMethod.POST, "/projetos/{idProjeto}/item-backlog-projeto/search").authenticated()
						.requestMatchers(HttpMethod.POST, "/projetos/{idProjeto}/sprints").hasAnyAuthority("PRODUCT_OWNER", "SCRUM_MASTER")
						.requestMatchers(HttpMethod.POST, "/projetos/{idProjeto}/sprints/search").authenticated()
						
						.requestMatchers(HttpMethod.GET, "/item-backlog-projeto/{idItemBacklogProjeto}").authenticated()
						.requestMatchers(HttpMethod.PUT, "/item-backlog-projeto/{idItemBacklogProjeto}").hasAnyAuthority("PRODUCT_OWNER", "SCRUM_MASTER", "DEV_TEAM")
						.requestMatchers(HttpMethod.DELETE, "/item-backlog-projeto/{idItemBacklogProjeto}").hasAnyAuthority("PRODUCT_OWNER", "SCRUM_MASTER", "DEV_TEAM")
						
						.requestMatchers(HttpMethod.GET, "/sprints/{idSprint}").authenticated()
						.requestMatchers(HttpMethod.PUT, "/sprints/{idSprint}").hasAnyAuthority("PRODUCT_OWNER", "SCRUM_MASTER")
						.requestMatchers(HttpMethod.DELETE, "/sprints/{idSprint}").hasAnyAuthority("PRODUCT_OWNER", "SCRUM_MASTER")
						.requestMatchers(HttpMethod.POST, "/sprints/{idSprint}/item-backlog-projeto/{idItemBacklogProjeto}/item-backlog-sprint").hasAnyAuthority("PRODUCT_OWNER", "SCRUM_MASTER")
						.requestMatchers(HttpMethod.POST, "/sprints/{idSprint}/item-backlog-sprint/search").authenticated()
						
						.requestMatchers(HttpMethod.GET, "/item-backlog-sprint/{idItemBacklogSprint}").authenticated()
						.requestMatchers(HttpMethod.PUT, "/item-backlog-sprint/{idItemBacklogSprint}").hasAnyAuthority("PRODUCT_OWNER", "SCRUM_MASTER", "DEV_TEAM")
						.requestMatchers(HttpMethod.DELETE, "/item-backlog-sprint/{idItemBacklogSprint}").hasAnyAuthority("PRODUCT_OWNER", "SCRUM_MASTER", "DEV_TEAM")
						.requestMatchers(HttpMethod.POST, "/item-backlog-sprint/{idItemBacklogSprint}/aprovar-inclusao").hasAuthority("PRODUCT_OWNER")
						.requestMatchers(HttpMethod.POST, "/item-backlog-sprint/{idItemBacklogSprint}/recusar-inclusao").hasAuthority("PRODUCT_OWNER")
						.requestMatchers(HttpMethod.POST, "/item-backlog-sprint/{idItemBacklogSprint}/aprovar-remocao").hasAuthority("PRODUCT_OWNER")
						.requestMatchers(HttpMethod.POST, "/item-backlog-sprint/{idItemBacklogSprint}/recusar-remocao").hasAuthority("PRODUCT_OWNER")
						
						.anyRequest().authenticated()
				)
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
