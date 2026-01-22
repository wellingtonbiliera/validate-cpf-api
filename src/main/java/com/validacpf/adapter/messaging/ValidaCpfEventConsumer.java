package com.validacpf.adapter.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ValidaCpfEventConsumer {

	@KafkaListener(
		topics = "${kafka.topic.valida-cpf-events:valida-cpf-events}",
		groupId = "valida-cpf-consumer-group",
		containerFactory = "kafkaListenerContainerFactory"
	)
	public void consumirEvento(
		@Payload final ValidaCpfEvent event,
		@Header(KafkaHeaders.RECEIVED_TOPIC) final String topic,
		@Header(KafkaHeaders.RECEIVED_PARTITION) final int partition,
		@Header(KafkaHeaders.OFFSET) final long offset) {
		
		log.info("Evento recebido - Tópico: {}, Partição: {}, Offset: {}, CPF: {}, Válido: {}",
			topic, partition, offset, event.getCpf(), event.getValido());
		
		try {
			log.debug("Evento processado com sucesso para CPF: {}", event.getCpf());
			
		} catch (final Exception e) {
			log.error("Erro ao processar evento para CPF: {}", event.getCpf(), e);
			throw e;
		}
	}
}
