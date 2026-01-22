package com.validacpf.adapter.receitaws;

import com.validacpf.domain.model.DadosReceitaWs;
import com.validacpf.adapter.receitaws.dto.ReceitaWsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ReceitaWsMapperTest {

	private ReceitaWsMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ReceitaWsMapper();
	}

	@Test
	void deveConverterRespostaValida() {
		ReceitaWsResponse response = new ReceitaWsResponse();
		response.setStatus("OK");
		response.setNome("João Silva");
		response.setNascimentoStr("01/01/1990");
		response.setSituacao("Regular");
		response.setCpf("12345678909");

		Optional<DadosReceitaWs> resultado = mapper.toDadosReceitaWs(response);

		assertTrue(resultado.isPresent());
		assertTrue(resultado.get().isValido());
		assertEquals("João Silva", resultado.get().getNome());
	}

	@Test
	void deveConverterRespostaInvalida() {
		ReceitaWsResponse response = new ReceitaWsResponse();
		response.setStatus("ERROR");
		response.setErro(java.util.Arrays.asList("CPF não encontrado"));

		Optional<DadosReceitaWs> resultado = mapper.toDadosReceitaWs(response);

		assertTrue(resultado.isPresent());
		assertFalse(resultado.get().isValido());
	}

	@Test
	void deveRetornarEmptyQuandoResponseNull() {
		Optional<DadosReceitaWs> resultado = mapper.toDadosReceitaWs(null);

		assertFalse(resultado.isPresent());
	}
}
