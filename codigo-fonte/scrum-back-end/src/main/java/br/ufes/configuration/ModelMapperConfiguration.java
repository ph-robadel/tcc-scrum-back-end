package br.ufes.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

		@Bean
		ModelMapper getModel() {
			return new ModelMapper();
		}
		
}
