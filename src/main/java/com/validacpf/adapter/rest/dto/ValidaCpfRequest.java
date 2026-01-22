package com.validacpf.adapter.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Request para validação de CPF")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidaCpfRequest {

	@NotBlank(message = "CPF é obrigatório")
	@Pattern(regexp = "^\\d{11}$|^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", 
		message = "CPF deve ter 11 dígitos ou estar no formato XXX.XXX.XXX-XX")
	@Schema(description = "CPF a ser validado (com ou sem formatação)", 
		example = "12345678901", 
		requiredMode = Schema.RequiredMode.REQUIRED)
	private String cpf;
}
