package com.validacpf.adapter.receitaws;

import com.validacpf.domain.model.DadosReceitaWs;
import com.validacpf.adapter.receitaws.dto.ReceitaWsResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReceitaWsMapper {

	public Optional<DadosReceitaWs> toDadosReceitaWs(final ReceitaWsResponse response) {
		if (response == null) {
			return Optional.empty();
		}

		if (response.isValido()) {
			return Optional.of(DadosReceitaWs.criarValido(
				response.getNome(),
				response.getNascimento(),
				response.getSituacao(),
				response.getCpf()
			));
		}

		final var mensagemErro = response.getErro() != null 
			? String.join(", ", response.getErro()) 
			: "Erro desconhecido";

		return Optional.of(DadosReceitaWs.criarInvalido(mensagemErro));
	}
}
