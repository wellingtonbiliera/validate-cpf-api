package com.validacpf.domain.port;

import com.validacpf.domain.model.ValidacaoCpf;

public interface EventPublisherPort {
	void publicarEventoValidacao(ValidacaoCpf validacao);
}
