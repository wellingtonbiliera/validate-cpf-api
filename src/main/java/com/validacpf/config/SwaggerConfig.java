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
			.title("Valida CPF API")
			.version("1.0.0")
			.description("API REST para validação de CPF com integração à ReceitaWS, " +
				"persistência em Oracle Database e mensageria com Apache Kafka.")
			.contact(new Contact()
				.name("Valida CPF")
				.email("contato@validacpf.com"))
			.license(new License()
				.name("Apache 2.0")
				.url("https://www.apache.org/licenses/LICENSE-2.0.html"));

		final var servers = List.of(
			new Server().url("http://localhost:8080").description("Servidor de Desenvolvimento"),
			new Server().url("https://api.validacpf.com").description("Servidor de Produção")
		);

		return new OpenAPI()
			.info(info)
			.servers(servers);
	}
}
