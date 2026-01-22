package com.validacpf.adapter.rest.exception;

import com.validacpf.domain.exception.CpfInvalidoException;
import com.validacpf.domain.exception.FormatoCpfException;
import com.validacpf.domain.exception.ServicoExternoException;
import com.validacpf.adapter.rest.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(FormatoCpfException.class)
	public ResponseEntity<ErrorResponse> handleFormatoCpfException(
		final FormatoCpfException ex,
		final jakarta.servlet.http.HttpServletRequest request) {
		
		log.warn("FormatoCpfException: {}", ex.getMessage());
		return criarErrorResponse(
			HttpStatus.BAD_REQUEST,
			"BAD_REQUEST",
			ex.getMessage(),
			request.getRequestURI()
		);
	}

	@ExceptionHandler(CpfInvalidoException.class)
	public ResponseEntity<ErrorResponse> handleCpfInvalidoException(
		final CpfInvalidoException ex,
		final jakarta.servlet.http.HttpServletRequest request) {
		
		log.warn("CpfInvalidoException: {}", ex.getMessage());
		return criarErrorResponse(
			HttpStatus.UNPROCESSABLE_ENTITY,
			"UNPROCESSABLE_ENTITY",
			ex.getMessage(),
			request.getRequestURI()
		);
	}

	@ExceptionHandler(ServicoExternoException.class)
	public ResponseEntity<ErrorResponse> handleServicoExternoException(
		final ServicoExternoException ex,
		final jakarta.servlet.http.HttpServletRequest request) {
		
		log.error("ServicoExternoException: {}", ex.getMessage(), ex);
		return criarErrorResponse(
			HttpStatus.SERVICE_UNAVAILABLE,
			"SERVICE_UNAVAILABLE",
			ex.getMessage(),
			request.getRequestURI()
		);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		final MethodArgumentNotValidException ex,
		final jakarta.servlet.http.HttpServletRequest request) {
		
		log.warn("MethodArgumentNotValidException: {}", ex.getMessage());
		
		final var detalhes = extrairDetalhesValidacao(ex);
		
		final var error = ErrorResponse.builder()
			.timestamp(LocalDateTime.now())
			.status(HttpStatus.BAD_REQUEST.value())
			.erro("BAD_REQUEST")
			.mensagem("Erro de validação nos dados de entrada")
			.caminho(request.getRequestURI())
			.detalhes(detalhes)
			.build();
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(
		final Exception ex,
		final jakarta.servlet.http.HttpServletRequest request) {
		
		log.error("Erro não tratado: {}", ex.getMessage(), ex);
		return criarErrorResponse(
			HttpStatus.INTERNAL_SERVER_ERROR,
			"INTERNAL_SERVER_ERROR",
			"Erro interno do servidor",
			request.getRequestURI()
		);
	}

	private ResponseEntity<ErrorResponse> criarErrorResponse(
		final HttpStatus status,
		final String erro,
		final String mensagem,
		final String caminho) {
		
		final var error = ErrorResponse.builder()
			.timestamp(LocalDateTime.now())
			.status(status.value())
			.erro(erro)
			.mensagem(mensagem)
			.caminho(caminho)
			.build();
		
		return ResponseEntity.status(status).body(error);
	}

	private List<String> extrairDetalhesValidacao(final MethodArgumentNotValidException ex) {
		final var detalhes = new ArrayList<String>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			final var fieldName = ((FieldError) error).getField();
			final var errorMessage = error.getDefaultMessage();
			detalhes.add(fieldName + ": " + errorMessage);
		});
		return detalhes;
	}
}
