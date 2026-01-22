package com.validacpf.adapter.rest.mapper;

import com.validacpf.adapter.rest.dto.ValidaCpfDetalhesResponse;
import com.validacpf.adapter.rest.dto.ValidaCpfResponse;
import com.validacpf.domain.model.ValidacaoCpf;
import org.springframework.stereotype.Component;

@Component
public class ValidaCpfMapper {

	public ValidaCpfResponse toResponse(final ValidacaoCpf validacao) {
		final var detalhes = validacao.getValido() && validacao.getNome() != null
			? criarDetalhes(validacao)
			: null;

		return ValidaCpfResponse.builder()
			.cpf(validacao.getCpf().getValorFormatado())
			.valido(validacao.getValido())
			.mensagem(validacao.getMensagem())
			.dataValidacao(validacao.getDataValidacao())
			.detalhes(detalhes)
			.build();
	}

	private ValidaCpfDetalhesResponse criarDetalhes(final ValidacaoCpf validacao) {
		return ValidaCpfDetalhesResponse.builder()
			.nome(validacao.getNome())
			.dataNascimento(validacao.getDataNascimento())
			.situacao(validacao.getSituacao())
			.build();
	}
}
