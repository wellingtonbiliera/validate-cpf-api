package com.validacpf.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DadosReceitaWsTest {

	@Test
	void deveCriarDadosValidos() {
		DadosReceitaWs dados = DadosReceitaWs.criarValido(
			"João Silva",
			LocalDate.of(1990, 1, 1),
			"Regular",
			"12345678909"
		);

		assertTrue(dados.isValido());
		assertEquals("OK", dados.getStatus());
		assertEquals("João Silva", dados.getNome());
		assertEquals("Regular", dados.getSituacao());
	}

	@Test
	void deveCriarDadosInvalidos() {
		DadosReceitaWs dados = DadosReceitaWs.criarInvalido("Erro ao consultar");

		assertFalse(dados.isValido());
		assertEquals("ERROR", dados.getStatus());
	}
}
