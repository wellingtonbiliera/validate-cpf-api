package com.validacpf.adapter.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@lombok.Builder
public class ValidaCpfEvent {

	private String cpf;
	private Boolean valido;
	private LocalDateTime dataValidacao;
	private String mensagem;
	private Long idValidacao;
}
