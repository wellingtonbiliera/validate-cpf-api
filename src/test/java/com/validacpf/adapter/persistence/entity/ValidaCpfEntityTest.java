package com.validacpf.adapter.persistence.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidaCpfEntityTest {

	@Test
	void deveCriarEntidadeComConstrutorPadrao() {
		ValidaCpfEntity entity = new ValidaCpfEntity();
		assertNotNull(entity);
	}

	@Test
	void deveCriarEntidadeComConstrutorCompleto() {
		LocalDateTime agora = LocalDateTime.now();
		LocalDate nascimento = LocalDate.of(1990, 1, 1);
		
		ValidaCpfEntity entity = new ValidaCpfEntity(
			1L, "12345678909", true, agora, "CPF válido",
			"OK", "João Silva", nascimento, "Regular", agora
		);

		assertEquals(1L, entity.getId());
		assertEquals("12345678909", entity.getCpf());
		assertTrue(entity.getValido());
		assertEquals("CPF válido", entity.getMensagem());
	}

	@Test
	void deveTestarGettersESetters() {
		ValidaCpfEntity entity = new ValidaCpfEntity();
		LocalDateTime agora = LocalDateTime.now();
		LocalDate nascimento = LocalDate.of(1990, 1, 1);
		
		entity.setId(1L);
		entity.setCpf("12345678909");
		entity.setValido(true);
		entity.setDataValidacao(agora);
		entity.setMensagem("CPF válido");
		entity.setCodigoRetorno("OK");
		entity.setNome("João Silva");
		entity.setDataNascimento(nascimento);
		entity.setSituacao("Regular");
		entity.setTimestampEvento(agora);
		
		assertEquals(1L, entity.getId());
		assertEquals("12345678909", entity.getCpf());
		assertTrue(entity.getValido());
		assertEquals(agora, entity.getDataValidacao());
		assertEquals("CPF válido", entity.getMensagem());
		assertEquals("OK", entity.getCodigoRetorno());
		assertEquals("João Silva", entity.getNome());
		assertEquals(nascimento, entity.getDataNascimento());
		assertEquals("Regular", entity.getSituacao());
		assertEquals(agora, entity.getTimestampEvento());
	}

	@Test
	void deveTestarEqualsEHashCode() {
		LocalDateTime agora = LocalDateTime.now();
		ValidaCpfEntity entity1 = new ValidaCpfEntity(1L, "12345678909", true, agora, "Válido", null, null, null, null, agora);
		ValidaCpfEntity entity2 = new ValidaCpfEntity(1L, "12345678909", true, agora, "Válido", null, null, null, null, agora);
		ValidaCpfEntity entity3 = new ValidaCpfEntity(2L, "98765432100", false, agora, "Inválido", null, null, null, null, agora);
		ValidaCpfEntity entity4 = new ValidaCpfEntity(1L, "11111111111", true, agora, "Válido", null, null, null, null, agora);
		
		assertEquals(entity1, entity2);
		assertEquals(entity1.hashCode(), entity2.hashCode());
		assertNotEquals(entity1, entity3);
		assertNotEquals(entity1, entity4);
		assertNotEquals(entity1, null);
	}

	@Test
	void deveTestarToString() {
		ValidaCpfEntity entity = new ValidaCpfEntity();
		entity.setId(1L);
		entity.setCpf("12345678909");
		
		String toString = entity.toString();
		assertNotNull(toString);
		assertTrue(toString.contains("ValidaCpfEntity"));
	}

	@Test
	void deveTestarEqualsComCamposNull() {
		LocalDateTime agora = LocalDateTime.now();
		ValidaCpfEntity entity1 = new ValidaCpfEntity(1L, "12345678909", true, agora, null, null, null, null, null, null);
		ValidaCpfEntity entity2 = new ValidaCpfEntity(1L, "12345678909", true, agora, null, null, null, null, null, null);
		ValidaCpfEntity entity3 = new ValidaCpfEntity(1L, "12345678909", true, agora, "Mensagem", null, null, null, null, null);
		
		assertEquals(entity1, entity2);
		assertNotEquals(entity1, entity3);
	}

	@Test
	void deveTestarEqualsComDiferentesValores() {
		LocalDateTime agora = LocalDateTime.now();
		ValidaCpfEntity entity1 = new ValidaCpfEntity(1L, "12345678909", true, agora, "Válido", "OK", null, null, null, agora);
		ValidaCpfEntity entity2 = new ValidaCpfEntity(1L, "12345678909", true, agora, "Válido", "OK", null, null, null, agora);
		ValidaCpfEntity entity3 = new ValidaCpfEntity(1L, "12345678909", true, agora, "Válido", "ERROR", null, null, null, agora);
		
		assertEquals(entity1, entity2);
		assertNotEquals(entity1, entity3);
	}

	@Test
	void deveTestarEqualsComDiferentesDatas() {
		LocalDateTime agora1 = LocalDateTime.of(2024, 1, 1, 10, 0);
		LocalDateTime agora2 = LocalDateTime.of(2024, 1, 1, 10, 0);
		LocalDateTime agora3 = LocalDateTime.of(2024, 1, 1, 11, 0);
		ValidaCpfEntity entity1 = new ValidaCpfEntity(1L, "12345678909", true, agora1, "Válido", null, null, null, null, agora1);
		ValidaCpfEntity entity2 = new ValidaCpfEntity(1L, "12345678909", true, agora2, "Válido", null, null, null, null, agora2);
		ValidaCpfEntity entity3 = new ValidaCpfEntity(1L, "12345678909", true, agora3, "Válido", null, null, null, null, agora3);
		
		assertEquals(entity1, entity2);
		assertNotEquals(entity1, entity3);
	}
}
