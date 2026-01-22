package com.validacpf.adapter.rest.mapper;

import com.validacpf.domain.model.ValidacaoCpf;
import com.validacpf.domain.valueobject.Cpf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidaCpfMapperTest {

	private ValidaCpfMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ValidaCpfMapper();
	}

	@Test
	void deveConverterValidacaoValidaComDetalhes() {
		final var cpf = Cpf.criarSemValidacao("12345678909");
		final var agora = LocalDateTime.now();
		final var validacao = ValidacaoCpf.builder()
			.cpf(cpf)
			.valido(true)
			.mensagem("CPF válido")
			.dataValidacao(agora)
			.nome("João Silva")
			.dataNascimento(LocalDate.of(1990, 1, 1))
			.situacao("Regular")
			.build();

		final var response = mapper.toResponse(validacao);

		assertEquals("123.456.789-09", response.getCpf());
		assertTrue(response.getValido());
		assertEquals("CPF válido", response.getMensagem());
		assertNotNull(response.getDetalhes());
		assertEquals("João Silva", response.getDetalhes().getNome());
	}

	@Test
	void deveConverterValidacaoInvalidaSemDetalhes() {
		final var cpf = Cpf.criarSemValidacao("12345678909");
		final var agora = LocalDateTime.now();
		final var validacao = ValidacaoCpf.builder()
			.cpf(cpf)
			.valido(false)
			.mensagem("CPF inválido")
			.dataValidacao(agora)
			.build();

		final var response = mapper.toResponse(validacao);

		assertFalse(response.getValido());
		assertNull(response.getDetalhes());
	}
}
