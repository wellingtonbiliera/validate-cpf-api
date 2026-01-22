package com.validacpf.adapter.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Detalhes adicionais da validação de CPF")
@lombok.Builder
@lombok.Getter
@lombok.AllArgsConstructor
public class ValidaCpfDetalhesResponse {

	@Schema(description = "Nome do portador do CPF")
	private final String nome;

	@Schema(description = "Data de nascimento")
	private final LocalDate dataNascimento;

	@Schema(description = "Situação do CPF")
	private final String situacao;
}
