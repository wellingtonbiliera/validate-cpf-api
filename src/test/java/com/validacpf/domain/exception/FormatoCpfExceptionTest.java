package com.validacpf.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormatoCpfExceptionTest {

	@Test
	void deveCriarExcecaoComMensagem() {
		FormatoCpfException ex = new FormatoCpfException("Formato inválido");
		
		assertEquals("Formato inválido", ex.getMessage());
	}

	@Test
	void deveSerRuntimeException() {
		FormatoCpfException ex = new FormatoCpfException("Teste");
		
		assertTrue(ex instanceof RuntimeException);
	}
}
