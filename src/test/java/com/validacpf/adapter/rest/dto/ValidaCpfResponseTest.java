package com.validacpf.adapter.rest.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidaCpfResponseTest {

	@Test
	void deveCriarComBuilder() {
		ValidaCpfResponse response = ValidaCpfResponse.builder()
			.valido(true)
			.mensagem("CPF válido")
			.cpf("123.456.789-09")
			.build();
		assertNotNull(response);
		assertTrue(response.getValido());
	}

	@Test
	void deveCriarComConstrutorCompleto() {
		final var agora = LocalDateTime.now();
		final var detalhes = ValidaCpfDetalhesResponse.builder()
			.nome("João")
			.build();
		
		final var response = ValidaCpfResponse.builder()
			.valido(true)
			.mensagem("CPF válido")
			.cpf("123.456.789-09")
			.dataValidacao(agora)
			.detalhes(detalhes)
			.build();

		assertTrue(response.getValido());
		assertEquals("CPF válido", response.getMensagem());
		assertEquals("123.456.789-09", response.getCpf());
		assertEquals(agora, response.getDataValidacao());
		assertNotNull(response.getDetalhes());
	}

	@Test
	void deveTestarGetters() {
		final var agora = LocalDateTime.now();
		
		final var response = ValidaCpfResponse.builder()
			.valido(true)
			.mensagem("Teste")
			.cpf("123.456.789-09")
			.dataValidacao(agora)
			.build();
		
		assertTrue(response.getValido());
		assertEquals("Teste", response.getMensagem());
		assertEquals("123.456.789-09", response.getCpf());
		assertEquals(agora, response.getDataValidacao());
	}
}
