package com.validacpf.adapter.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Response de erro padrão")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

	@Schema(description = "Timestamp do erro")
	private LocalDateTime timestamp;

	@Schema(description = "Código HTTP do erro", example = "400")
	private Integer status;

	@Schema(description = "Tipo do erro", example = "BAD_REQUEST")
	private String erro;

	@Schema(description = "Mensagem de erro", example = "CPF inválido")
	private String mensagem;

	@Schema(description = "Caminho da requisição", example = "/api/validar-cpf")
	private String caminho;

	@Schema(description = "Detalhes adicionais de validação")
	private List<String> detalhes;
}
