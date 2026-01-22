package com.validacpf.domain.model;

import com.validacpf.domain.valueobject.Cpf;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ValidacaoCpf {

	private Long id;
	private final Cpf cpf;
	private final Boolean valido;
	private final LocalDateTime dataValidacao;
	private final String mensagem;
	private final String codigoRetorno;
	private final String nome;
	private final LocalDate dataNascimento;
	private final String situacao;
	private final LocalDateTime timestampEvento;

	public static ValidacaoCpf criarValidacaoValida(
		final Cpf cpf,
		final String nome,
		final LocalDate dataNascimento,
		final String situacao,
		final String codigoRetorno) {
		
		final var agora = LocalDateTime.now();
		
		return ValidacaoCpf.builder()
			.cpf(cpf)
			.valido(true)
			.dataValidacao(agora)
			.mensagem("CPF válido")
			.nome(nome)
			.dataNascimento(dataNascimento)
			.situacao(situacao)
			.codigoRetorno(codigoRetorno)
			.timestampEvento(agora)
			.build();
	}

	public static ValidacaoCpf criarValidacaoInvalida(
		final Cpf cpf,
		final String motivo) {
		
		final var agora = LocalDateTime.now();
		
		return ValidacaoCpf.builder()
			.cpf(cpf)
			.valido(false)
			.dataValidacao(agora)
			.mensagem(motivo)
			.timestampEvento(agora)
			.build();
	}

	public static ValidacaoCpf criarValidacaoInvalidaComErro(
		final Cpf cpf,
		final String erro) {
		
		final var mensagem = "CPF inválido ou não encontrado: " + erro;
		return criarValidacaoInvalida(cpf, mensagem);
	}
}
