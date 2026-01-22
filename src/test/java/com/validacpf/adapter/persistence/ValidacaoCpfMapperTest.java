package com.validacpf.adapter.persistence;

import com.validacpf.domain.model.ValidacaoCpf;
import com.validacpf.domain.valueobject.Cpf;
import com.validacpf.adapter.persistence.entity.ValidaCpfEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidacaoCpfMapperTest {

	private ValidacaoCpfMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ValidacaoCpfMapper();
	}

	@Test
	void deveConverterParaEntity() {
		Cpf cpf = Cpf.criarSemValidacao("12345678909");
		ValidacaoCpf validacao = ValidacaoCpf.builder()
			.id(1L)
			.cpf(cpf)
			.valido(true)
			.dataValidacao(LocalDateTime.now())
			.mensagem("CPF válido")
			.nome("João Silva")
			.dataNascimento(LocalDate.of(1990, 1, 1))
			.situacao("Regular")
			.build();

		ValidaCpfEntity entity = mapper.toEntity(validacao);

		assertEquals("12345678909", entity.getCpf());
		assertTrue(entity.getValido());
		assertEquals("CPF válido", entity.getMensagem());
		assertEquals("João Silva", entity.getNome());
	}

	@Test
	void deveConverterParaDomain() {
		ValidaCpfEntity entity = new ValidaCpfEntity();
		entity.setId(1L);
		entity.setCpf("12345678909");
		entity.setValido(true);
		entity.setDataValidacao(LocalDateTime.now());
		entity.setMensagem("CPF válido");

		ValidacaoCpf validacao = mapper.toDomain(entity);

		assertEquals("12345678909", validacao.getCpf().getValor());
		assertTrue(validacao.getValido());
		assertEquals("CPF válido", validacao.getMensagem());
	}
}
