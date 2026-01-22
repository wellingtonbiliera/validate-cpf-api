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
			// ignora
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

		mockMvc.perform(post("/api/validar-cpf")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cpf").exists())
			.andExpect(jsonPath("$.valido").exists())
			.andExpect(jsonPath("$.mensagem").exists())
			.andExpect(jsonPath("$.dataValidacao").exists());

		Thread.sleep(500);

		final var registros = repository.findAll();
		assertFalse(registros.isEmpty());

		final var registroSalvo = registros.stream()
			.filter(r -> {
				final var cpfSalvo = r.getCpf().replaceAll("[^0-9]", "");
				return cpfValido.equals(cpfSalvo);
			})
			.findFirst();

		assertTrue(registroSalvo.isPresent());
		
		final var entity = registroSalvo.get();
		assertNotNull(entity.getId());
		assertTrue(entity.getId() > 0);
		assertNotNull(entity.getDataValidacao());
		assertNotNull(entity.getValido());
		assertNotNull(entity.getMensagem());
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

		mockMvc.perform(post("/api/validar-cpf")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.valido").exists());

		Thread.sleep(500);

		final var registros = repository.findAll();
		assertFalse(registros.isEmpty());

		final var registroEncontrado = registros.stream()
			.anyMatch(r -> {
				final var cpfSalvo = r.getCpf().replaceAll("[^0-9]", "");
				return cpfSemFormatacao.equals(cpfSalvo);
			});

		assertTrue(registroEncontrado);
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

		final var registros = repository.findAll();
		assertTrue(registros.size() >= 2);

		for (final var cpf : cpfs) {
			final var cpfSemFormatacao = cpf.replaceAll("[^0-9]", "");
			final var encontrado = registros.stream()
				.anyMatch(r -> {
					final var cpfSalvo = r.getCpf().replaceAll("[^0-9]", "");
					return cpfSemFormatacao.equals(cpfSalvo);
				});
			assertTrue(encontrado);
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

		mockMvc.perform(post("/api/validar-cpf")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk());

		final var depois = LocalDateTime.now();
		Thread.sleep(500);

		final var registros = repository.findAll();
		assertFalse(registros.isEmpty());

		final var registro = registros.stream()
			.filter(r -> {
				final var cpfSalvo = r.getCpf().replaceAll("[^0-9]", "");
				return cpf.equals(cpfSalvo);
			})
			.findFirst();

		assertTrue(registro.isPresent());

		final var entity = registro.get();
		assertNotNull(entity.getId());
		assertTrue(entity.getId() > 0);
		assertNotNull(entity.getCpf());
		assertFalse(entity.getCpf().isEmpty());
		assertNotNull(entity.getValido());
		assertNotNull(entity.getDataValidacao());
		assertTrue(entity.getDataValidacao().isAfter(antes.minusSeconds(2)));
		assertTrue(entity.getDataValidacao().isBefore(depois.plusSeconds(2)));
		assertNotNull(entity.getMensagem());
		assertFalse(entity.getMensagem().isEmpty());
	}
}
