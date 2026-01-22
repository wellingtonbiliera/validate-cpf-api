package com.validacpf.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServicoExternoExceptionTest {

	@Test
	void deveCriarExcecaoComMensagem() {
		ServicoExternoException ex = new ServicoExternoException("Erro no serviço");
		
		assertEquals("Erro no serviço", ex.getMessage());
	}

	@Test
	void deveCriarExcecaoComMensagemECausa() {
		RuntimeException causa = new RuntimeException("Erro original");
		ServicoExternoException ex = new ServicoExternoException("Erro no serviço", causa);
		
		assertEquals("Erro no serviço", ex.getMessage());
		assertEquals(causa, ex.getCause());
	}

	@Test
	void deveSerRuntimeException() {
		ServicoExternoException ex = new ServicoExternoException("Teste");
		
		assertTrue(ex instanceof RuntimeException);
	}
}
