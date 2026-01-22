package com.validacpf.adapter.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ValidaCpfEventProducer {

	private final KafkaTemplate<String, ValidaCpfEvent> kafkaTemplate;
	private final String topicName;

	public ValidaCpfEventProducer(
		final KafkaTemplate<String, ValidaCpfEvent> kafkaTemplate,
		@Value("${kafka.topic.valida-cpf-events:valida-cpf-events}") final String topicName) {
		this.kafkaTemplate = kafkaTemplate;
		this.topicName = topicName;
	}

	public void publicarEvento(final ValidaCpfEvent event) {
		log.info("Publicando evento no Kafka - CPF: {}, Tópico: {}", event.getCpf(), topicName);
		
		final var future = kafkaTemplate.send(topicName, event.getCpf(), event);
		
		future.whenComplete((result, ex) -> {
			if (ex == null) {
				log.debug("Evento publicado com sucesso no offset: {}", result.getRecordMetadata().offset());
			} else {
				log.error("Erro ao publicar evento no Kafka para CPF: {}", event.getCpf(), ex);
			}
		});
	}
}
