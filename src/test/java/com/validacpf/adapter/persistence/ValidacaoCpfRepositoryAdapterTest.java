package com.validacpf.adapter.persistence;

import com.validacpf.domain.model.ValidacaoCpf;
import com.validacpf.domain.valueobject.Cpf;
import com.validacpf.adapter.persistence.entity.ValidaCpfEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidacaoCpfRepositoryAdapterTest {

	@Mock
	private ValidaCpfRepository repository;

	@Mock
	private ValidacaoCpfMapper mapper;

	@InjectMocks
	private ValidacaoCpfRepositoryAdapter adapter;

	private ValidacaoCpf validacao;
	private ValidaCpfEntity entity;

	@BeforeEach
	void setUp() {
		Cpf cpf = Cpf.criarSemValidacao("12345678909");
		validacao = ValidacaoCpf.builder()
			.cpf(cpf)
			.valido(true)
			.dataValidacao(LocalDateTime.now())
			.mensagem("CPF válido")
			.build();

		entity = new ValidaCpfEntity();
		entity.setId(1L);
		entity.setCpf("12345678909");
		entity.setValido(true);
	}

	@Test
	void deveSalvarValidacao() {
		when(mapper.toEntity(validacao)).thenReturn(entity);
		when(repository.save(entity)).thenReturn(entity);
		when(mapper.toDomain(entity)).thenReturn(validacao);

		ValidacaoCpf resultado = adapter.salvar(validacao);

		assertNotNull(resultado);
		verify(repository).save(entity);
	}

	@Test
	void deveBuscarPorCpf() {
		Cpf cpf = Cpf.criarSemValidacao("12345678909");
		when(repository.findByCpf("12345678909")).thenReturn(Optional.of(entity));
		when(mapper.toDomain(entity)).thenReturn(validacao);

		Optional<ValidacaoCpf> resultado = adapter.buscarPorCpf(cpf);

		assertTrue(resultado.isPresent());
		verify(repository).findByCpf("12345678909");
	}

	@Test
	void deveRetornarEmptyQuandoNaoEncontra() {
		Cpf cpf = Cpf.criarSemValidacao("12345678909");
		when(repository.findByCpf("12345678909")).thenReturn(Optional.empty());

		Optional<ValidacaoCpf> resultado = adapter.buscarPorCpf(cpf);

		assertFalse(resultado.isPresent());
	}
}
