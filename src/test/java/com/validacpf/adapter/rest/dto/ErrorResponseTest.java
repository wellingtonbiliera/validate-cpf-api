package com.validacpf.adapter.rest.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

	@Test
	void deveCriarComConstrutorPadrao() {
		ErrorResponse response = new ErrorResponse();
		
		assertNull(response.getTimestamp());
		assertNull(response.getStatus());
		assertNull(response.getErro());
		assertNull(response.getMensagem());
		assertNull(response.getCaminho());
		assertNull(response.getDetalhes());
	}

	@Test
	void deveCriarComConstrutorCompleto() {
		LocalDateTime timestamp = LocalDateTime.now();
		List<String> detalhes = Arrays.asList("Erro 1", "Erro 2");
		
		ErrorResponse response = new ErrorResponse(
			timestamp, 400, "BAD_REQUEST", "Mensagem de erro", "/api/validar-cpf", detalhes
		);
		
		assertEquals(timestamp, response.getTimestamp());
		assertEquals(400, response.getStatus());
		assertEquals("BAD_REQUEST", response.getErro());
		assertEquals("Mensagem de erro", response.getMensagem());
		assertEquals("/api/validar-cpf", response.getCaminho());
		assertEquals(detalhes, response.getDetalhes());
	}

	@Test
	void deveCriarComBuilder() {
		LocalDateTime timestamp = LocalDateTime.now();
		List<String> detalhes = Arrays.asList("Detalhe 1");
		
		ErrorResponse response = ErrorResponse.builder()
			.timestamp(timestamp)
			.status(422)
			.erro("UNPROCESSABLE_ENTITY")
			.mensagem("CPF inválido")
			.caminho("/api/validar-cpf")
			.detalhes(detalhes)
			.build();
		
		assertEquals(timestamp, response.getTimestamp());
		assertEquals(422, response.getStatus());
		assertEquals("UNPROCESSABLE_ENTITY", response.getErro());
		assertEquals("CPF inválido", response.getMensagem());
		assertEquals("/api/validar-cpf", response.getCaminho());
		assertEquals(detalhes, response.getDetalhes());
	}

	@Test
	void deveTestarBuilderComValoresParciais() {
		ErrorResponse response = ErrorResponse.builder()
			.status(500)
			.erro("INTERNAL_ERROR")
			.build();
		
		assertEquals(500, response.getStatus());
		assertEquals("INTERNAL_ERROR", response.getErro());
		assertNull(response.getTimestamp());
		assertNull(response.getMensagem());
		assertNull(response.getCaminho());
		assertNull(response.getDetalhes());
	}


	@Test
	void deveTestarGettersESetters() {
		ErrorResponse response = new ErrorResponse();
		LocalDateTime timestamp = LocalDateTime.now();
		List<String> detalhes = Arrays.asList("Erro de validação");
		
		response.setTimestamp(timestamp);
		response.setStatus(500);
		response.setErro("INTERNAL_SERVER_ERROR");
		response.setMensagem("Erro interno");
		response.setCaminho("/api/validar-cpf");
		response.setDetalhes(detalhes);
		
		assertEquals(timestamp, response.getTimestamp());
		assertEquals(500, response.getStatus());
		assertEquals("INTERNAL_SERVER_ERROR", response.getErro());
		assertEquals("Erro interno", response.getMensagem());
		assertEquals("/api/validar-cpf", response.getCaminho());
		assertEquals(detalhes, response.getDetalhes());
	}

	@Test
	void deveTestarEqualsEHashCode() {
		LocalDateTime timestamp = LocalDateTime.now();
		ErrorResponse response1 = new ErrorResponse(timestamp, 400, "BAD_REQUEST", "Erro", "/api", null);
		ErrorResponse response2 = new ErrorResponse(timestamp, 400, "BAD_REQUEST", "Erro", "/api", null);
		ErrorResponse response3 = new ErrorResponse(timestamp, 500, "BAD_REQUEST", "Erro", "/api", null);
		ErrorResponse response4 = new ErrorResponse(timestamp, 400, "INTERNAL_ERROR", "Erro", "/api", null);
		
		assertEquals(response1, response2);
		assertEquals(response1.hashCode(), response2.hashCode());
		assertNotEquals(response1, response3);
		assertNotEquals(response1, response4);
		assertNotEquals(response1, null);
	}

	@Test
	void deveTestarEqualsComCamposNull() {
		ErrorResponse response1 = new ErrorResponse(null, null, null, null, null, null);
		ErrorResponse response2 = new ErrorResponse(null, null, null, null, null, null);
		ErrorResponse response3 = new ErrorResponse(LocalDateTime.now(), null, null, null, null, null);
		
		assertEquals(response1, response2);
		assertNotEquals(response1, response3);
	}

	@Test
	void deveTestarToString() {
		ErrorResponse response = new ErrorResponse();
		response.setStatus(400);
		response.setErro("BAD_REQUEST");
		String toString = response.toString();
		assertNotNull(toString);
		assertTrue(toString.contains("ErrorResponse"));
	}

	@Test
	void deveTestarEqualsComDiferentesValores() {
		LocalDateTime timestamp = LocalDateTime.now();
		ErrorResponse response1 = new ErrorResponse(timestamp, 400, "BAD_REQUEST", "Mensagem 1", "/api", null);
		ErrorResponse response2 = new ErrorResponse(timestamp, 400, "BAD_REQUEST", "Mensagem 1", "/api", null);
		ErrorResponse response3 = new ErrorResponse(timestamp, 400, "BAD_REQUEST", "Mensagem 2", "/api", null);
		
		assertEquals(response1, response2);
		assertNotEquals(response1, response3);
	}

	@Test
	void deveTestarEqualsComDetalhes() {
		LocalDateTime timestamp = LocalDateTime.now();
		List<String> detalhes1 = Arrays.asList("Erro 1");
		List<String> detalhes2 = Arrays.asList("Erro 1");
		List<String> detalhes3 = Arrays.asList("Erro 2");
		ErrorResponse response1 = new ErrorResponse(timestamp, 400, "BAD_REQUEST", "Erro", "/api", detalhes1);
		ErrorResponse response2 = new ErrorResponse(timestamp, 400, "BAD_REQUEST", "Erro", "/api", detalhes2);
		ErrorResponse response3 = new ErrorResponse(timestamp, 400, "BAD_REQUEST", "Erro", "/api", detalhes3);
		
		assertEquals(response1, response2);
		assertNotEquals(response1, response3);
	}

	@Test
	void deveTestarEqualsComCaminho() {
		LocalDateTime timestamp = LocalDateTime.now();
		ErrorResponse response1 = new ErrorResponse(timestamp, 400, "BAD_REQUEST", "Erro", "/api1", null);
		ErrorResponse response2 = new ErrorResponse(timestamp, 400, "BAD_REQUEST", "Erro", "/api1", null);
		ErrorResponse response3 = new ErrorResponse(timestamp, 400, "BAD_REQUEST", "Erro", "/api2", null);
		
		assertEquals(response1, response2);
		assertNotEquals(response1, response3);
	}
}
