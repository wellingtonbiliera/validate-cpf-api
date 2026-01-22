package com.validacpf.adapter.receitaws;

import com.validacpf.domain.exception.ServicoExternoException;
import com.validacpf.domain.model.DadosReceitaWs;
import com.validacpf.domain.valueobject.Cpf;
import com.validacpf.adapter.receitaws.dto.ReceitaWsResponse;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceitaWsAdapterTest {

	@Mock
	private ReceitaWsFeignClient feignClient;

	@Mock
	private ReceitaWsMapper mapper;

	@InjectMocks
	private ReceitaWsAdapter adapter;

	private Cpf cpf;
	private ReceitaWsResponse response;

	@BeforeEach
	void setUp() {
		cpf = Cpf.criarSemValidacao("12345678909");
		response = new ReceitaWsResponse();
		response.setStatus("OK");
		response.setNome("João Silva");
	}

	@Test
	void deveConsultarCpfComSucesso() {
		when(feignClient.validarCPF(anyString())).thenReturn(response);
		when(mapper.toDadosReceitaWs(response))
			.thenReturn(Optional.of(DadosReceitaWs.criarValido("João", null, "Regular", "12345678909")));

		Optional<DadosReceitaWs> resultado = adapter.consultarCpf(cpf);

		assertTrue(resultado.isPresent());
		verify(feignClient).validarCPF("12345678909");
	}

	@Test
	void deveRetornarEmptyQuandoNaoEncontrado() {
		when(feignClient.validarCPF(anyString()))
			.thenThrow(mock(FeignException.NotFound.class));

		Optional<DadosReceitaWs> resultado = adapter.consultarCpf(cpf);

		assertFalse(resultado.isPresent());
	}

	@Test
	void deveLancarExcecaoQuandoFeignException() {
		FeignException feignException = mock(FeignException.class);
		when(feignException.status()).thenReturn(500);
		when(feignException.getMessage()).thenReturn("Erro interno");
		when(feignClient.validarCPF(anyString())).thenThrow(feignException);

		assertThrows(ServicoExternoException.class, () -> adapter.consultarCpf(cpf));
	}

	@Test
	void deveLancarExcecaoQuandoErroInesperado() {
		when(feignClient.validarCPF(anyString()))
			.thenThrow(new RuntimeException("Erro inesperado"));

		assertThrows(ServicoExternoException.class, () -> adapter.consultarCpf(cpf));
	}
}
