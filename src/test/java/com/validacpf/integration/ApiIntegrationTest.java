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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ValidaCpfRepository repository;

	@BeforeEach
	void limparBanco() {
		repository.deleteAll();
	}

	@Test
	@Transactional
	void deveValidarCpfESalvarNoOracle() throws Exception {
		final var cpfValido = "12345678909";
		final var requestJson = """
			{
				"cpf": "%s"
			}
			""".formatted(cpfValido);

		// Executar requisição na API
		mockMvc.perform(post("/api/validar-cpf")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cpf").exists())
			.andExpect(jsonPath("$.valido").exists())
			.andExpect(jsonPath("$.mensagem").exists())
			.andExpect(jsonPath("$.dataValidacao").exists());

		// Verificar se foi salvo no Oracle
		final var registros = repository.findAll();
		assertFalse(registros.isEmpty(), "Deveria ter pelo menos um registro salvo no Oracle");

		final var registroSalvo = registros.stream()
			.filter(r -> cpfValido.equals(r.getCpf()) || cpfValido.replaceAll("[^0-9]", "").equals(r.getCpf().replaceAll("[^0-9]", "")))
			.findFirst();

		assertTrue(registroSalvo.isPresent(), "Registro deveria estar salvo no Oracle");
		
		final var entity = registroSalvo.get();
		assertNotNull(entity.getId(), "ID deveria estar preenchido");
		assertNotNull(entity.getDataValidacao(), "Data de validação deveria estar preenchida");
		assertNotNull(entity.getValido(), "Campo válido deveria estar preenchido");
		assertNotNull(entity.getMensagem(), "Mensagem deveria estar preenchida");
	}

	@Test
	@Transactional
	void deveValidarCpfFormatadoESalvarNoOracle() throws Exception {
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

		// Verificar se foi salvo no Oracle (pode estar com ou sem formatação)
		final var registros = repository.findAll();
		assertFalse(registros.isEmpty(), "Deveria ter registro salvo no Oracle");

		final var registroEncontrado = registros.stream()
			.anyMatch(r -> {
				final var cpfSalvo = r.getCpf().replaceAll("[^0-9]", "");
				return cpfSemFormatacao.equals(cpfSalvo);
			});

		assertTrue(registroEncontrado, "CPF deveria estar salvo no Oracle (com ou sem formatação)");
	}

	@Test
	@Transactional
	void deveRejeitarCpfInvalidoENaoSalvar() throws Exception {
		final var cpfInvalido = "11111111111";
		final var requestJson = """
			{
				"cpf": "%s"
			}
			""".formatted(cpfInvalido);

		// Executar requisição na API
		mockMvc.perform(post("/api/validar-cpf")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isUnprocessableEntity());

		// Verificar que NÃO foi salvo no Oracle (CPF inválido não passa da validação)
		final var registros = repository.findAll();
		assertTrue(registros.isEmpty(), "CPF inválido não deveria ser salvo no Oracle");
	}

	@Test
	@Transactional
	void deveSalvarMultiplasValidacoesNoOracle() throws Exception {
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
		}

		// Verificar se todos foram salvos no Oracle
		final var registros = repository.findAll();
		assertTrue(registros.size() >= 2, "Deveria ter pelo menos 2 registros salvos no Oracle");

		// Verificar que cada CPF foi salvo
		for (final var cpf : cpfs) {
			final var cpfSemFormatacao = cpf.replaceAll("[^0-9]", "");
			final var encontrado = registros.stream()
				.anyMatch(r -> {
					final var cpfSalvo = r.getCpf().replaceAll("[^0-9]", "");
					return cpfSemFormatacao.equals(cpfSalvo);
				});
			assertTrue(encontrado, "CPF " + cpf + " deveria estar salvo no Oracle");
		}
	}

	@Test
	@Transactional
	void deveVerificarCamposSalvosNoOracle() throws Exception {
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

		// Verificar dados salvos
		final var registro = repository.findAll().stream()
			.filter(r -> cpf.equals(r.getCpf()) || cpf.equals(r.getCpf().replaceAll("[^0-9]", "")))
			.findFirst();

		assertTrue(registro.isPresent(), "Registro deveria estar no Oracle");

		final var entity = registro.get();
		assertNotNull(entity.getId(), "ID não deveria ser nulo");
		assertTrue(entity.getId() > 0, "ID deveria ser maior que zero");
		assertNotNull(entity.getCpf(), "CPF não deveria ser nulo");
		assertNotNull(entity.getValido(), "Campo válido não deveria ser nulo");
		assertNotNull(entity.getDataValidacao(), "Data de validação não deveria ser nula");
		assertTrue(entity.getDataValidacao().isAfter(antes.minusSeconds(1)), 
			"Data de validação deveria ser recente");
		assertTrue(entity.getDataValidacao().isBefore(depois.plusSeconds(1)), 
			"Data de validação deveria ser recente");
		assertNotNull(entity.getMensagem(), "Mensagem não deveria ser nula");
		assertFalse(entity.getMensagem().isEmpty(), "Mensagem não deveria estar vazia");
	}
}
