package com.validacpf.integration;

import com.validacpf.adapter.persistence.ValidaCpfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Teste de integração REAL com Oracle Database
 * 
 * PRÉ-REQUISITOS:
 * 1. Docker Compose deve estar rodando: docker compose up -d
 * 2. Oracle deve estar pronto (aguardar 1-2 minutos após iniciar)
 * 3. Banco deve estar inicializado: docker exec -i valida-cpf-oracle sqlplus system/Oracle123@XE < scripts/init-database.sql
 * 
 * Para executar este teste, use o profile 'oracle-test':
 * ./gradlew test --tests OracleRealIntegrationTest -Dspring.profiles.active=oracle-test
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("oracle-test")
class OracleRealIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ValidaCpfRepository repository;

	@BeforeEach
	void limparBanco() {
		try {
			repository.deleteAll();
		} catch (final Exception e) {
			// Ignora se não conseguir limpar (pode ser que não exista ainda)
		}
	}

	@Test
	@Transactional
	void deveValidarCpfValidoESalvarNoOracleReal() throws Exception {
		final var cpfValido = "12345678909";
		final var requestJson = """
			{
				"cpf": "%s"
			}
			""".formatted(cpfValido);

		// Executar requisição na API
		final var resultado = mockMvc.perform(post("/api/validar-cpf")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cpf").exists())
			.andExpect(jsonPath("$.valido").exists())
			.andExpect(jsonPath("$.mensagem").exists())
			.andExpect(jsonPath("$.dataValidacao").exists())
			.andReturn();

		// Aguardar um pouco para garantir que a transação foi commitada
		Thread.sleep(500);

		// Verificar se foi salvo no Oracle REAL
		final var registros = repository.findAll();
		assertFalse(registros.isEmpty(), 
			"❌ ERRO: Nenhum registro encontrado no Oracle! Verifique se o banco está rodando e acessível.");

		final var registroSalvo = registros.stream()
			.filter(r -> {
				final var cpfSalvo = r.getCpf().replaceAll("[^0-9]", "");
				return cpfValido.equals(cpfSalvo);
			})
			.findFirst();

		assertTrue(registroSalvo.isPresent(), 
			"❌ ERRO: CPF não foi salvo no Oracle! CPF esperado: " + cpfValido);
		
		final var entity = registroSalvo.get();
		System.out.println("✅ Registro salvo no Oracle:");
		System.out.println("   ID: " + entity.getId());
		System.out.println("   CPF: " + entity.getCpf());
		System.out.println("   Válido: " + entity.getValido());
		System.out.println("   Mensagem: " + entity.getMensagem());
		System.out.println("   Data Validação: " + entity.getDataValidacao());

		assertNotNull(entity.getId(), "ID deveria estar preenchido");
		assertTrue(entity.getId() > 0, "ID deveria ser maior que zero");
		assertNotNull(entity.getDataValidacao(), "Data de validação deveria estar preenchida");
		assertNotNull(entity.getValido(), "Campo válido deveria estar preenchido");
		assertNotNull(entity.getMensagem(), "Mensagem deveria estar preenchida");
	}

	@Test
	@Transactional
	void deveValidarCpfFormatadoESalvarNoOracleReal() throws Exception {
		final var cpfFormatado = "123.456.789-09";
		final var cpfSemFormatacao = "12345678909";
		final var requestJson = """
			{
				"cpf": "%s"
			}
			""".formatted(cpfFormatado);

		// Executar requisição na API
		mockMvc.perform(post("/api/validar-cpf")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.valido").exists());

		Thread.sleep(500);

		// Verificar se foi salvo no Oracle REAL
		final var registros = repository.findAll();
		assertFalse(registros.isEmpty(), 
			"❌ ERRO: Nenhum registro encontrado no Oracle!");

		final var registroEncontrado = registros.stream()
			.anyMatch(r -> {
				final var cpfSalvo = r.getCpf().replaceAll("[^0-9]", "");
				return cpfSemFormatacao.equals(cpfSalvo);
			});

		assertTrue(registroEncontrado, 
			"❌ ERRO: CPF formatado não foi salvo corretamente no Oracle!");
	}

	@Test
	@Transactional
	void deveSalvarMultiplasValidacoesNoOracleReal() throws Exception {
		final var cpfs = new String[]{"12345678909", "98765432100"};

		for (final var cpf : cpfs) {
			final var requestJson = """
				{
					"cpf": "%s"
				}
				""".formatted(cpf);

			mockMvc.perform(post("/api/validar-cpf")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson))
				.andExpect(status().isOk());
			
			Thread.sleep(300);
		}

		Thread.sleep(500);

		// Verificar se todos foram salvos no Oracle REAL
		final var registros = repository.findAll();
		System.out.println("✅ Total de registros no Oracle: " + registros.size());
		
		assertTrue(registros.size() >= 2, 
			"❌ ERRO: Deveria ter pelo menos 2 registros no Oracle! Encontrados: " + registros.size());

		// Verificar que cada CPF foi salvo
		for (final var cpf : cpfs) {
			final var cpfSemFormatacao = cpf.replaceAll("[^0-9]", "");
			final var encontrado = registros.stream()
				.anyMatch(r -> {
					final var cpfSalvo = r.getCpf().replaceAll("[^0-9]", "");
					return cpfSemFormatacao.equals(cpfSalvo);
				});
			assertTrue(encontrado, 
				"❌ ERRO: CPF " + cpf + " não foi encontrado no Oracle!");
		}
	}

	@Test
	@Transactional
	void deveVerificarIntegridadeDadosNoOracleReal() throws Exception {
		final var cpf = "12345678909";
		final var requestJson = """
			{
				"cpf": "%s"
			}
			""".formatted(cpf);

		final var antes = LocalDateTime.now();

		// Executar requisição
		mockMvc.perform(post("/api/validar-cpf")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk());

		final var depois = LocalDateTime.now();
		Thread.sleep(500);

		// Verificar dados salvos no Oracle REAL
		final var registros = repository.findAll();
		assertFalse(registros.isEmpty(), 
			"❌ ERRO: Nenhum registro encontrado no Oracle!");

		final var registro = registros.stream()
			.filter(r -> {
				final var cpfSalvo = r.getCpf().replaceAll("[^0-9]", "");
				return cpf.equals(cpfSalvo);
			})
			.findFirst();

		assertTrue(registro.isPresent(), 
			"❌ ERRO: Registro não encontrado no Oracle!");

		final var entity = registro.get();
		
		// Validações detalhadas
		assertNotNull(entity.getId(), "❌ ID não deveria ser nulo");
		assertTrue(entity.getId() > 0, "❌ ID deveria ser maior que zero");
		assertNotNull(entity.getCpf(), "❌ CPF não deveria ser nulo");
		assertFalse(entity.getCpf().isEmpty(), "❌ CPF não deveria estar vazio");
		assertNotNull(entity.getValido(), "❌ Campo válido não deveria ser nulo");
		assertNotNull(entity.getDataValidacao(), "❌ Data de validação não deveria ser nula");
		assertTrue(entity.getDataValidacao().isAfter(antes.minusSeconds(2)), 
			"❌ Data de validação deveria ser recente");
		assertTrue(entity.getDataValidacao().isBefore(depois.plusSeconds(2)), 
			"❌ Data de validação deveria ser recente");
		assertNotNull(entity.getMensagem(), "❌ Mensagem não deveria ser nula");
		assertFalse(entity.getMensagem().isEmpty(), "❌ Mensagem não deveria estar vazia");

		System.out.println("✅ Todos os campos estão corretos no Oracle!");
		System.out.println("   ID: " + entity.getId());
		System.out.println("   CPF: " + entity.getCpf());
		System.out.println("   Válido: " + entity.getValido());
		System.out.println("   Data: " + entity.getDataValidacao());
		System.out.println("   Mensagem: " + entity.getMensagem());
	}
}
