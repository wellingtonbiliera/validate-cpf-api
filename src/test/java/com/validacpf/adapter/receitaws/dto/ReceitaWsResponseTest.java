package com.validacpf.adapter.receitaws.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ReceitaWsResponseTest {

	private ReceitaWsResponse response;

	@BeforeEach
	void setUp() {
		response = new ReceitaWsResponse();
	}

	@Test
	void deveRetornarValidoQuandoStatusOk() {
		response.setStatus("OK");
		response.setErro(null);

		assertTrue(response.isValido());
	}

	@Test
	void deveRetornarInvalidoQuandoStatusNaoOk() {
		response.setStatus("ERROR");

		assertFalse(response.isValido());
	}

	@Test
	void deveRetornarInvalidoQuandoTemErros() {
		response.setStatus("OK");
		response.setErro(Arrays.asList("Erro 1", "Erro 2"));

		assertFalse(response.isValido());
	}

	@Test
	void deveConverterNascimentoString() {
		response.setNascimentoStr("01/01/1990");

		LocalDate nascimento = response.getNascimento();

		assertNotNull(nascimento);
		assertEquals(1990, nascimento.getYear());
		assertEquals(1, nascimento.getMonthValue());
		assertEquals(1, nascimento.getDayOfMonth());
	}

	@Test
	void deveRetornarNullQuandoNascimentoNull() {
		response.setNascimentoStr(null);
		assertNull(response.getNascimento());
	}

	@Test
	void deveRetornarNullQuandoNascimentoVazio() {
		response.setNascimentoStr("");
		assertNull(response.getNascimento());
	}

	@Test
	void deveRetornarNullQuandoFormatoInvalido() {
		response.setNascimentoStr("1990-01-01");
		assertNull(response.getNascimento());
	}

	@Test
	void deveRetornarInvalidoQuandoStatusNull() {
		response.setStatus(null);
		response.setErro(null);
		assertFalse(response.isValido());
	}

	@Test
	void deveRetornarValidoQuandoListaVazia() {
		response.setStatus("OK");
		response.setErro(Arrays.asList());
		assertTrue(response.isValido());
	}

	@Test
	void deveTestarEqualsEHashCode() {
		ReceitaWsResponse response1 = new ReceitaWsResponse();
		response1.setStatus("OK");
		response1.setCpf("12345678909");
		response1.setNome("João");
		
		ReceitaWsResponse response2 = new ReceitaWsResponse();
		response2.setStatus("OK");
		response2.setCpf("12345678909");
		response2.setNome("João");
		
		ReceitaWsResponse response3 = new ReceitaWsResponse();
		response3.setStatus("ERROR");
		response3.setCpf("98765432100");
		
		assertEquals(response1, response2);
		assertEquals(response1.hashCode(), response2.hashCode());
		assertNotEquals(response1, response3);
		assertNotEquals(response1, null);
	}

	@Test
	void deveTestarGettersESetters() {
		response.setStatus("OK");
		response.setNome("Maria Santos");
		response.setNascimentoStr("15/05/1985");
		response.setSituacao("Regular");
		response.setCpf("98765432100");

		assertEquals("OK", response.getStatus());
		assertEquals("Maria Santos", response.getNome());
		assertEquals("15/05/1985", response.getNascimentoStr());
		assertEquals("Regular", response.getSituacao());
		assertEquals("98765432100", response.getCpf());
	}

	@Test
	void deveTestarToString() {
		response.setStatus("OK");
		response.setCpf("12345678909");
		String toString = response.toString();
		assertNotNull(toString);
		assertTrue(toString.contains("ReceitaWsResponse"));
	}

	@Test
	void deveTestarCanEqual() {
		ReceitaWsResponse response1 = new ReceitaWsResponse();
		ReceitaWsResponse response2 = new ReceitaWsResponse();
		assertTrue(response1.canEqual(response2));
		assertFalse(response1.canEqual("not a ReceitaWsResponse"));
	}
}
