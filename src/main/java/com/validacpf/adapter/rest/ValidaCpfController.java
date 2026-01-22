package com.validacpf.adapter.rest;

import com.validacpf.adapter.rest.dto.ValidaCpfRequest;
import com.validacpf.adapter.rest.dto.ValidaCpfResponse;
import com.validacpf.adapter.rest.mapper.ValidaCpfMapper;
import com.validacpf.application.usecase.ValidarCpfUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "Validação de CPF", description = "API para validação de CPF")
@RequiredArgsConstructor
public class ValidaCpfController {

	private final ValidarCpfUseCase validarCpfUseCase;
	private final ValidaCpfMapper mapper;

	@PostMapping("/validar-cpf")
	@Operation(
		summary = "Validar CPF",
		description = "Valida um CPF através de validação local e consulta ao serviço ReceitaWS"
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "Validação realizada com sucesso",
			content = @Content(schema = @Schema(implementation = ValidaCpfResponse.class))
		),
		@ApiResponse(
			responseCode = "400",
			description = "Requisição inválida (formato ou campo ausente)",
			content = @Content(schema = @Schema(implementation = com.validacpf.adapter.rest.dto.ErrorResponse.class))
		),
		@ApiResponse(
			responseCode = "422",
			description = "CPF inválido (validação)",
			content = @Content(schema = @Schema(implementation = com.validacpf.adapter.rest.dto.ErrorResponse.class))
		),
		@ApiResponse(
			responseCode = "503",
			description = "Serviço externo indisponível",
			content = @Content(schema = @Schema(implementation = com.validacpf.adapter.rest.dto.ErrorResponse.class))
		),
		@ApiResponse(
			responseCode = "500",
			description = "Erro interno do servidor",
			content = @Content(schema = @Schema(implementation = com.validacpf.adapter.rest.dto.ErrorResponse.class))
		)
	})
	public ResponseEntity<ValidaCpfResponse> validarCpf(@Valid @RequestBody final ValidaCpfRequest request) {
		log.info("Requisição de validação recebida para CPF: {}", request.getCpf());
		
		final var validacao = validarCpfUseCase.executar(request.getCpf());
		final var response = mapper.toResponse(validacao);
		
		return ResponseEntity.ok(response);
	}
}
