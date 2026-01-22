package com.validacpf.adapter.messaging;

import com.validacpf.domain.model.ValidacaoCpf;
import com.validacpf.domain.valueobject.Cpf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventPublisherAdapterTest {

	@Mock
	private ValidaCpfEventProducer eventProducer;

	@Mock
	private EventMapper mapper;

	@InjectMocks
	private EventPublisherAdapter adapter;

	private ValidacaoCpf validacao;

	@BeforeEach
	void setUp() {
		final var cpf = Cpf.criarSemValidacao("12345678909");
		validacao = ValidacaoCpf.builder()
			.id(1L)
			.cpf(cpf)
			.valido(true)
			.dataValidacao(LocalDateTime.now())
			.mensagem("CPF válido")
			.build();
	}

	@Test
	void devePublicarEvento() {
		final var event = ValidaCpfEvent.builder()
			.cpf("12345678909")
			.valido(true)
			.dataValidacao(LocalDateTime.now())
			.mensagem("CPF válido")
			.idValidacao(1L)
			.build();
		when(mapper.toEvent(validacao)).thenReturn(event);

		adapter.publicarEventoValidacao(validacao);

		verify(mapper).toEvent(validacao);
		verify(eventProducer).publicarEvento(event);
	}
}
