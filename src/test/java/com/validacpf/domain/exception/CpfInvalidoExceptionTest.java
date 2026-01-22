package com.validacpf.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpfInvalidoExceptionTest {

	@Test
	void deveCriarExcecaoComMensagem() {
		CpfInvalidoException ex = new CpfInvalidoException("CPF inválido");
		
		assertEquals("CPF inválido", ex.getMessage());
	}

	@Test
	void deveSerRuntimeException() {
		CpfInvalidoException ex = new CpfInvalidoException("Teste");
		
		assertTrue(ex instanceof RuntimeException);
	}
}
