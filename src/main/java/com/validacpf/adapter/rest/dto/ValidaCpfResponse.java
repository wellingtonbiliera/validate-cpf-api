package com.validacpf.adapter.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Response da validação de CPF")
@lombok.Builder
@lombok.Getter
@lombok.AllArgsConstructor
public class ValidaCpfResponse {

	@Schema(description = "Indica se o CPF é válido", example = "true")
	private final Boolean valido;

	@Schema(description = "Mensagem descritiva do resultado", example = "CPF válido")
	private final String mensagem;

	@Schema(description = "CPF validado", example = "12345678901")
	private final String cpf;

	@Schema(description = "Data e hora da validação")
	private final LocalDateTime dataValidacao;

	@Schema(description = "Detalhes adicionais da validação (se disponível)")
	private final ValidaCpfDetalhesResponse detalhes;
}
