package com.validacpf.adapter.rest;

import com.validacpf.adapter.rest.dto.ValidaCpfRequest;
import com.validacpf.adapter.rest.dto.ValidaCpfResponse;
import com.validacpf.adapter.rest.mapper.ValidaCpfMapper;
import com.validacpf.application.usecase.ValidarCpfUseCase;
import com.validacpf.domain.port.ValidacaoCpfRepositoryPort;
import com.validacpf.domain.valueobject.Cpf;
import com.validacpf.adapter.userinfo.UserInfoAdapter;
import com.validacpf.adapter.votacao.VotacaoFeignClient;
import com.validacpf.adapter.rest.dto.PodeVotarResponse;
import com.validacpf.adapter.rest.dto.JaVotouResponse;
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

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Validação de CPF")
@RequiredArgsConstructor
public class ValidaCpfController {

	private final ValidarCpfUseCase validarCpfUseCase;
	private final ValidaCpfMapper mapper;
	private final ValidacaoCpfRepositoryPort repositoryPort;
	private final UserInfoAdapter userInfoAdapter;
	private final VotacaoFeignClient votacaoFeignClient;

	@PostMapping("/validacoes-cpf")
	@Operation(summary = "Criar validação de CPF")
	public ResponseEntity<ValidaCpfResponse> criarValidacao(@Valid @RequestBody final ValidaCpfRequest request) {
		final var validacao = validarCpfUseCase.executar(request.getCpf());
		return ResponseEntity.ok(mapper.toResponse(validacao));
	}

	@GetMapping("/validacoes-cpf/{cpf}")
	@Operation(summary = "Consultar validação de CPF")
	public ResponseEntity<ValidaCpfResponse> consultarValidacao(@PathVariable final String cpf) {
		final var cpfValueObject = Cpf.criar(cpf);
		final var validacao = repositoryPort.buscarPorCpf(cpfValueObject);
		
		if (validacao.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(mapper.toResponse(validacao.get()));
	}

	@GetMapping("/validacoes-cpf/{cpf}/pode-votar")
	@Operation(summary = "Verificar se CPF pode votar")
	public ResponseEntity<PodeVotarResponse> podeVotar(@PathVariable final String cpf) {
		final var cpfValueObject = Cpf.criar(cpf);
		final var podeVotar = userInfoAdapter.podeVotar(cpfValueObject.getValor());
		final var response = new PodeVotarResponse();
		response.setStatus(podeVotar ? "ABLE_TO_VOTE" : "UNABLE_TO_VOTE");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/validacoes-cpf/{cpf}/ja-votou/{pautaId}")
	@Operation(summary = "Verificar se CPF já votou na pauta")
	public ResponseEntity<JaVotouResponse> jaVotou(
		@PathVariable final String cpf,
		@PathVariable final Long pautaId) {
		final var cpfValueObject = Cpf.criar(cpf);
		final var validacao = repositoryPort.buscarPorCpf(cpfValueObject);
		final var associadoId = validacao.map(v -> v.getCpf().getValor())
			.orElse(cpfValueObject.getValor());
		
		try {
			final var responseVotacao = votacaoFeignClient.verificarJaVotou(pautaId, associadoId);
			final var response = new JaVotouResponse();
			response.setJaVotou(responseVotacao != null && Boolean.TRUE.equals(responseVotacao.getJaVotou()));
			return ResponseEntity.ok(response);
		} catch (final Exception e) {
			final var response = new JaVotouResponse();
			response.setJaVotou(false);
			return ResponseEntity.ok(response);
		}
	}
	
	@PostMapping("/validar-cpf")
	@Operation(summary = "Validar CPF")
	public ResponseEntity<ValidaCpfResponse> validarCpf(@Valid @RequestBody final ValidaCpfRequest request) {
		final var validacao = validarCpfUseCase.executar(request.getCpf());
		return ResponseEntity.ok(mapper.toResponse(validacao));
	}
}

