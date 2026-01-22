package com.validacpf.adapter.messaging;

import com.validacpf.domain.model.ValidacaoCpf;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

	public ValidaCpfEvent toEvent(final ValidacaoCpf validacao) {
		return ValidaCpfEvent.builder()
			.cpf(validacao.getCpf().getValor())
			.valido(validacao.getValido())
			.dataValidacao(validacao.getDataValidacao())
			.mensagem(validacao.getMensagem())
			.idValidacao(validacao.getId())
			.build();
	}
}
