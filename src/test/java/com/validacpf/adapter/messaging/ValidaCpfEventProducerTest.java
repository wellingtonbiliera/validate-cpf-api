package com.validacpf.adapter.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidaCpfEventProducerTest {

	@Mock
	private KafkaTemplate<String, ValidaCpfEvent> kafkaTemplate;

	@InjectMocks
	private ValidaCpfEventProducer producer;

	private ValidaCpfEvent event;

	@BeforeEach
	void setUp() {
		producer = new ValidaCpfEventProducer(kafkaTemplate, "valida-cpf-events");
		event = ValidaCpfEvent.builder()
			.cpf("12345678909")
			.valido(true)
			.dataValidacao(LocalDateTime.now())
			.mensagem("CPF válido")
			.idValidacao(1L)
			.build();
	}

	@Test
	void devePublicarEventoComSucesso() {
		SendResult<String, ValidaCpfEvent> sendResult = mock(SendResult.class);
		CompletableFuture<SendResult<String, ValidaCpfEvent>> future = CompletableFuture.completedFuture(sendResult);
		
		when(kafkaTemplate.send(eq("valida-cpf-events"), eq("12345678909"), any(ValidaCpfEvent.class)))
			.thenReturn(future);

		producer.publicarEvento(event);

		verify(kafkaTemplate).send("valida-cpf-events", "12345678909", event);
	}

	@Test
	void deveTratarErroAoPublicar() {
		CompletableFuture<SendResult<String, ValidaCpfEvent>> future = new CompletableFuture<>();
		future.completeExceptionally(new RuntimeException("Erro no Kafka"));
		
		when(kafkaTemplate.send(eq("valida-cpf-events"), eq("12345678909"), any(ValidaCpfEvent.class)))
			.thenReturn(future);

		producer.publicarEvento(event);

		verify(kafkaTemplate).send("valida-cpf-events", "12345678909", event);
	}

	@Test
	void deveTestarWhenCompleteComSucesso() throws Exception {
		SendResult<String, ValidaCpfEvent> sendResult = mock(SendResult.class);
		org.apache.kafka.clients.producer.RecordMetadata metadata = mock(org.apache.kafka.clients.producer.RecordMetadata.class);
		when(metadata.offset()).thenReturn(123L);
		when(sendResult.getRecordMetadata()).thenReturn(metadata);
		
		CompletableFuture<SendResult<String, ValidaCpfEvent>> future = new CompletableFuture<>();
		
		when(kafkaTemplate.send(eq("valida-cpf-events"), eq("12345678909"), any(ValidaCpfEvent.class)))
			.thenReturn(future);

		producer.publicarEvento(event);
		
		future.complete(sendResult);
		Thread.sleep(100);
		
		verify(kafkaTemplate).send("valida-cpf-events", "12345678909", event);
	}
}
