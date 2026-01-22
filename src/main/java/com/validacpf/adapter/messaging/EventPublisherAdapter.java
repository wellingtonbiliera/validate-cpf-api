package com.validacpf.adapter.messaging;

import com.validacpf.domain.model.ValidacaoCpf;
import com.validacpf.domain.port.EventPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisherAdapter implements EventPublisherPort {

	private final ValidaCpfEventProducer eventProducer;
	private final EventMapper mapper;

	@Override
	public void publicarEventoValidacao(final ValidacaoCpf validacao) {
		final var event = mapper.toEvent(validacao);
		eventProducer.publicarEvento(event);
	}
}
