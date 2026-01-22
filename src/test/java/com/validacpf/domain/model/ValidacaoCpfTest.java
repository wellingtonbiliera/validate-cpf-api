package com.validacpf.domain.model;

import com.validacpf.domain.valueobject.Cpf;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidacaoCpfTest {

	@Test
	void deveCriarValidacaoValida() {
		Cpf cpf = Cpf.criarSemValidacao("12345678909");
		ValidacaoCpf validacao = ValidacaoCpf.criarValidacaoValida(
			cpf,
			"João Silva",
			LocalDate.of(1990, 1, 1),
			"Regular",
			"OK"
		);

		assertTrue(validacao.getValido());
		assertEquals("CPF válido", validacao.getMensagem());
		assertEquals("João Silva", validacao.getNome());
		assertNotNull(validacao.getDataValidacao());
	}

	@Test
	void deveCriarValidacaoInvalida() {
		Cpf cpf = Cpf.criarSemValidacao("12345678909");
		ValidacaoCpf validacao = ValidacaoCpf.criarValidacaoInvalida(cpf, "CPF inválido");

		assertFalse(validacao.getValido());
		assertEquals("CPF inválido", validacao.getMensagem());
	}

	@Test
	void deveCriarValidacaoInvalidaComErro() {
		Cpf cpf = Cpf.criarSemValidacao("12345678909");
		ValidacaoCpf validacao = ValidacaoCpf.criarValidacaoInvalidaComErro(cpf, "Erro ao consultar");

		assertFalse(validacao.getValido());
		assertTrue(validacao.getMensagem().contains("Erro ao consultar"));
	}
}
