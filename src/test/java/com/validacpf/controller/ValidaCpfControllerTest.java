package com.validacpf.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.validacpf.adapter.rest.ValidaCpfController;
import com.validacpf.adapter.rest.dto.ValidaCpfRequest;
import com.validacpf.adapter.rest.dto.ValidaCpfResponse;
import com.validacpf.application.usecase.ValidarCpfUseCase;
import com.validacpf.domain.model.ValidacaoCpf;
import com.validacpf.domain.valueobject.Cpf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ValidaCpfController.class)
class ValidaCpfControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ValidarCpfUseCase validarCpfUseCase;

	@MockBean
	private com.validacpf.adapter.rest.mapper.ValidaCpfMapper mapper;

	@MockBean
	private com.validacpf.domain.port.ValidacaoCpfRepositoryPort repositoryPort;

	@Autowired
	private ObjectMapper objectMapper;

	private ValidacaoCpf validacaoValida;

	@BeforeEach
	void setUp() {
		final var cpf = Cpf.criarSemValidacao("12345678901");
		validacaoValida = ValidacaoCpf.builder()
			.cpf(cpf)
			.valido(true)
			.mensagem("CPF válido")
			.dataValidacao(LocalDateTime.now())
			.build();
	}

	@Test
	void deveValidarCPFComSucesso() throws Exception {
		final var request = new ValidaCpfRequest("12345678901");
		final var response = ValidaCpfResponse.builder()
			.cpf("123.456.789-01")
			.valido(true)
			.mensagem("CPF válido")
			.dataValidacao(LocalDateTime.now())
			.build();
		
		when(validarCpfUseCase.executar(any(String.class))).thenReturn(validacaoValida);
		when(mapper.toResponse(any(com.validacpf.domain.model.ValidacaoCpf.class))).thenReturn(response);

		mockMvc.perform(post("/api/validar-cpf")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.valido").value(true))
			.andExpect(jsonPath("$.cpf").value("123.456.789-01"))
			.andExpect(jsonPath("$.mensagem").value("CPF válido"));
	}

	@Test
	void deveRetornarErroQuandoCPFAusente() throws Exception {
		final var request = new ValidaCpfRequest("");

		mockMvc.perform(post("/api/validar-cpf")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest());
	}

	@Test
	void deveRetornarErroQuandoFormatoInvalido() throws Exception {
		final var request = new ValidaCpfRequest("123");

		mockMvc.perform(post("/api/validar-cpf")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest());
	}
}
