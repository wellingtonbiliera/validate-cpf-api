package com.validacpf.adapter.rest.exception;

import com.validacpf.domain.exception.CpfInvalidoException;
import com.validacpf.domain.exception.FormatoCpfException;
import com.validacpf.domain.exception.ServicoExternoException;
import com.validacpf.adapter.rest.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

	@Mock
	private HttpServletRequest request;

	@InjectMocks
	private GlobalExceptionHandler handler;

	@BeforeEach
	void setUp() {
		when(request.getRequestURI()).thenReturn("/api/validar-cpf");
	}

	@Test
	void deveTratarFormatoCpfException() {
		FormatoCpfException ex = new FormatoCpfException("CPF inválido");

		ResponseEntity<ErrorResponse> response = handler.handleFormatoCpfException(ex, request);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("CPF inválido", response.getBody().getMensagem());
	}

	@Test
	void deveTratarCpfInvalidoException() {
		CpfInvalidoException ex = new CpfInvalidoException("CPF inválido");

		ResponseEntity<ErrorResponse> response = handler.handleCpfInvalidoException(ex, request);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

	@Test
	void deveTratarServicoExternoException() {
		ServicoExternoException ex = new ServicoExternoException("Serviço indisponível");

		ResponseEntity<ErrorResponse> response = handler.handleServicoExternoException(ex, request);

		assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
	}

	@Test
	void deveTratarMethodArgumentNotValidException() {
		MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
		BindingResult bindingResult = mock(BindingResult.class);
		FieldError fieldError = new FieldError("request", "cpf", "CPF é obrigatório");

		when(ex.getBindingResult()).thenReturn(bindingResult);
		when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

		ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentNotValidException(ex, request);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody().getDetalhes());
	}

	@Test
	void deveTratarExceptionGenerica() {
		Exception ex = new RuntimeException("Erro interno");

		ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex, request);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
}
