package com.validacpf.adapter.messaging;

import com.validacpf.domain.model.ValidacaoCpf;
import com.validacpf.domain.valueobject.Cpf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

	private EventMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new EventMapper();
	}

	@Test
	void deveConverterParaEvent() {
		Cpf cpf = Cpf.criarSemValidacao("12345678909");
		LocalDateTime agora = LocalDateTime.now();
		ValidacaoCpf validacao = ValidacaoCpf.builder()
			.id(1L)
			.cpf(cpf)
			.valido(true)
			.dataValidacao(agora)
			.mensagem("CPF válido")
			.build();

		ValidaCpfEvent event = mapper.toEvent(validacao);

		assertEquals("12345678909", event.getCpf());
		assertTrue(event.getValido());
		assertEquals(agora, event.getDataValidacao());
		assertEquals("CPF válido", event.getMensagem());
		assertEquals(1L, event.getIdValidacao());
	}
}
