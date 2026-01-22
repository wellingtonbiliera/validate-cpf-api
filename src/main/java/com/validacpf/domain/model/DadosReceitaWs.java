package com.validacpf.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DadosReceitaWs {

	private final String status;
	private final String nome;
	private final LocalDate dataNascimento;
	private final String situacao;
	private final String cpf;
	private final boolean valido;

	public static DadosReceitaWs criarValido(
		final String nome,
		final LocalDate dataNascimento,
		final String situacao,
		final String cpf) {
		
		return DadosReceitaWs.builder()
			.status("OK")
			.nome(nome)
			.dataNascimento(dataNascimento)
			.situacao(situacao)
			.cpf(cpf)
			.valido(true)
			.build();
	}

	public static DadosReceitaWs criarInvalido(final String erro) {
		return DadosReceitaWs.builder()
			.status("ERROR")
			.valido(false)
			.build();
	}
}
