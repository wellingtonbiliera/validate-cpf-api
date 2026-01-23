package com.validacpf.adapter.rest;

import com.validacpf.adapter.rest.dto.ValidaCpfRequest;
import com.validacpf.adapter.rest.dto.ValidaCpfResponse;
import com.validacpf.adapter.rest.mapper.ValidaCpfMapper;
import com.validacpf.application.usecase.ValidarCpfUseCase;
import com.validacpf.domain.port.ValidacaoCpfRepositoryPort;
import com.validacpf.domain.valueobject.Cpf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Validação de CPF", description = "API RESTful para validação de CPF")
@RequiredArgsConstructor
public class ValidaCpfController {

	private final ValidarCpfUseCase validarCpfUseCase;
	private final ValidaCpfMapper mapper;
	private final ValidacaoCpfRepositoryPort repositoryPort;

	@PostMapping("/validacoes-cpf")
	@Operation(
		summary = "Criar validação de CPF",
		description = "Cria uma nova validação de CPF através de validação local e consulta ao serviço ReceitaWS"
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "Validação criada com sucesso",
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
	public ResponseEntity<ValidaCpfResponse> criarValidacao(@Valid @RequestBody final ValidaCpfRequest request) {
		log.info("Requisição de criação de validação recebida para CPF: {}", request.getCpf());
		
		final var validacao = validarCpfUseCase.executar(request.getCpf());
		final var response = mapper.toResponse(validacao);
		
		return ResponseEntity.ok(response);
	}

	@GetMapping("/validacoes-cpf/{cpf}")
	@Operation(
		summary = "Consultar validação de CPF",
		description = "Consulta a última validação realizada para um CPF específico"
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "Validação encontrada",
			content = @Content(schema = @Schema(implementation = ValidaCpfResponse.class))
		),
		@ApiResponse(
			responseCode = "400",
			description = "Formato de CPF inválido",
			content = @Content(schema = @Schema(implementation = com.validacpf.adapter.rest.dto.ErrorResponse.class))
		),
		@ApiResponse(
			responseCode = "404",
			description = "Validação não encontrada para o CPF informado",
			content = @Content(schema = @Schema(implementation = com.validacpf.adapter.rest.dto.ErrorResponse.class))
		),
		@ApiResponse(
			responseCode = "500",
			description = "Erro interno do servidor",
			content = @Content(schema = @Schema(implementation = com.validacpf.adapter.rest.dto.ErrorResponse.class))
		)
	})
	public ResponseEntity<ValidaCpfResponse> consultarValidacao(
		@Parameter(description = "CPF a ser consultado (com ou sem formatação)", 
			example = "12345678901", 
			required = true)
		@PathVariable final String cpf) {
		
		log.info("Requisição de consulta de validação recebida para CPF: {}", cpf);
		
		final var cpfValueObject = Cpf.criar(cpf);
		final var validacao = repositoryPort.buscarPorCpf(cpfValueObject);
		
		if (validacao.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		final var response = mapper.toResponse(validacao.get());
		return ResponseEntity.ok(response);
	}

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
