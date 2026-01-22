package com.validacpf.adapter.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidaCpfEventConsumerTest {

	@InjectMocks
	private ValidaCpfEventConsumer consumer;

	private ValidaCpfEvent event;

	@BeforeEach
	void setUp() {
		consumer = new ValidaCpfEventConsumer();
		event = ValidaCpfEvent.builder()
			.cpf("12345678909")
			.valido(true)
			.dataValidacao(LocalDateTime.now())
			.mensagem("CPF válido")
			.idValidacao(1L)
			.build();
	}

	@Test
	void deveConsumirEventoComSucesso() {
		assertDoesNotThrow(() -> 
			consumer.consumirEvento(event, "valida-cpf-events", 0, 1L)
		);
	}

	@Test
	void deveConsumirEventoComDiferentesParticoesEOffsets() {
		assertDoesNotThrow(() -> 
			consumer.consumirEvento(event, "valida-cpf-events", 1, 100L)
		);
		
		assertDoesNotThrow(() -> 
			consumer.consumirEvento(event, "valida-cpf-events", 2, 200L)
		);
	}

	@Test
	void deveConsumirEventoComCpfInvalido() {
		final var eventInvalido = ValidaCpfEvent.builder()
			.cpf("12345678909")
			.valido(false)
			.dataValidacao(LocalDateTime.now())
			.mensagem("CPF inválido")
			.idValidacao(1L)
			.build();
		
		assertDoesNotThrow(() -> 
			consumer.consumirEvento(eventInvalido, "valida-cpf-events", 0, 1L)
		);
	}

	@Test
	void deveConsumirEventoSemMensagem() {
		final var eventSemMensagem = ValidaCpfEvent.builder()
			.cpf("12345678909")
			.valido(true)
			.dataValidacao(LocalDateTime.now())
			.mensagem(null)
			.idValidacao(1L)
			.build();
		
		assertDoesNotThrow(() -> 
			consumer.consumirEvento(eventSemMensagem, "valida-cpf-events", 0, 1L)
		);
	}

	@Test
	void deveConsumirEventoSemIdValidacao() {
		final var eventSemId = ValidaCpfEvent.builder()
			.cpf("12345678909")
			.valido(true)
			.dataValidacao(LocalDateTime.now())
			.mensagem("CPF válido")
			.idValidacao(null)
			.build();
		
		assertDoesNotThrow(() -> 
			consumer.consumirEvento(eventSemId, "valida-cpf-events", 0, 1L)
		);
	}
}
