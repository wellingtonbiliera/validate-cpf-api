package com.validacpf.adapter.rest.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidaCpfDetalhesResponseTest {

	@Test
	void deveCriarComBuilder() {
		ValidaCpfDetalhesResponse detalhes = ValidaCpfDetalhesResponse.builder()
			.nome("João")
			.build();
		assertNotNull(detalhes);
		assertEquals("João", detalhes.getNome());
	}

	@Test
	void deveCriarComConstrutorCompleto() {
		final var nascimento = LocalDate.of(1990, 1, 1);
		
		final var detalhes = ValidaCpfDetalhesResponse.builder()
			.nome("João Silva")
			.dataNascimento(nascimento)
			.situacao("Regular")
			.build();

		assertEquals("João Silva", detalhes.getNome());
		assertEquals(nascimento, detalhes.getDataNascimento());
		assertEquals("Regular", detalhes.getSituacao());
	}

	@Test
	void deveTestarGetters() {
		final var nascimento = LocalDate.of(1990, 1, 1);
		
		final var detalhes = ValidaCpfDetalhesResponse.builder()
			.nome("João")
			.dataNascimento(nascimento)
			.situacao("Regular")
			.build();
		
		assertEquals("João", detalhes.getNome());
		assertEquals(nascimento, detalhes.getDataNascimento());
		assertEquals("Regular", detalhes.getSituacao());
	}
}
