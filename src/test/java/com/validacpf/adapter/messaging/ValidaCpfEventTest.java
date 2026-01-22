package com.validacpf.adapter.messaging;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidaCpfEventTest {

	@Test
	void deveCriarEventoComBuilder() {
		LocalDateTime agora = LocalDateTime.now();
		ValidaCpfEvent event = ValidaCpfEvent.builder()
			.cpf("12345678909")
			.valido(true)
			.dataValidacao(agora)
			.mensagem("CPF válido")
			.idValidacao(1L)
			.build();

		assertEquals("12345678909", event.getCpf());
		assertTrue(event.getValido());
		assertEquals(agora, event.getDataValidacao());
		assertEquals("CPF válido", event.getMensagem());
		assertEquals(1L, event.getIdValidacao());
	}

	@Test
	void deveCriarEventoComConstrutor() {
		LocalDateTime agora = LocalDateTime.now();
		ValidaCpfEvent event = new ValidaCpfEvent("98765432100", false, agora, "CPF inválido", 2L);

		assertEquals("98765432100", event.getCpf());
		assertFalse(event.getValido());
		assertEquals(agora, event.getDataValidacao());
		assertEquals("CPF inválido", event.getMensagem());
		assertEquals(2L, event.getIdValidacao());
	}

	@Test
	void deveTestarEqualsEHashCode() {
		LocalDateTime agora = LocalDateTime.now();
		ValidaCpfEvent event1 = new ValidaCpfEvent("12345678909", true, agora, "Válido", 1L);
		ValidaCpfEvent event2 = new ValidaCpfEvent("12345678909", true, agora, "Válido", 1L);
		ValidaCpfEvent event3 = new ValidaCpfEvent("98765432100", false, agora, "Inválido", 2L);

		assertEquals(event1, event2);
		assertEquals(event1.hashCode(), event2.hashCode());
		assertNotEquals(event1, event3);
		assertNotEquals(event1, null);
	}

	@Test
	void deveTestarGettersESetters() {
		ValidaCpfEvent event = new ValidaCpfEvent();
		LocalDateTime agora = LocalDateTime.now();

		event.setCpf("11122233344");
		event.setValido(false);
		event.setDataValidacao(agora);
		event.setMensagem("Teste");
		event.setIdValidacao(99L);

		assertEquals("11122233344", event.getCpf());
		assertFalse(event.getValido());
		assertEquals(agora, event.getDataValidacao());
		assertEquals("Teste", event.getMensagem());
		assertEquals(99L, event.getIdValidacao());
	}
}
