package br.ufes.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfiguration {

	@Bean
	OpenAPI customOpenAPI() {
		ApiResponse badRequest = new ApiResponse().description("Dados da requisição inconsistentes");
		ApiResponse forbidden = new ApiResponse().description("Token inválido ou expirado");
		ApiResponse internalServerError = new ApiResponse().description("Erro interno no servidor");
		
		Components components = new Components();
		components.addResponses("badRequest", badRequest);
		components.addResponses("forbidden", forbidden);
		components.addResponses("internalServerError", internalServerError);
		components.addSecuritySchemes("token",
				new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));

		return new OpenAPI().components(components);

	}

}
