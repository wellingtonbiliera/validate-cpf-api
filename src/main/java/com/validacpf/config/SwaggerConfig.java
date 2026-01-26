package com.validacpf.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		final var info = new Info()
			.title("Validação de CPF API")
			.version("1.0.0")
			.description("API para validação de CPF");

		final var servers = List.of(
			new Server().url("http://localhost:8080").description("Local")
		);

		return new OpenAPI()
			.info(info)
			.servers(servers);
	}
}
