package com.validacpf.application.usecase;

import com.validacpf.domain.exception.ServicoExternoException;
import com.validacpf.domain.model.DadosReceitaWs;
import com.validacpf.domain.model.ValidacaoCpf;
import com.validacpf.domain.port.EventPublisherPort;
import com.validacpf.domain.port.ReceitaWsPort;
import com.validacpf.domain.port.ValidacaoCpfRepositoryPort;
import com.validacpf.domain.valueobject.Cpf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidarCpfUseCaseTest {

	@Mock
	private ReceitaWsPort receitaWsPort;

	@Mock
	private ValidacaoCpfRepositoryPort repositoryPort;

	@Mock
	private EventPublisherPort eventPublisherPort;

	@InjectMocks
	private ValidarCpfUseCase useCase;

	private DadosReceitaWs dadosValidos;
	private ValidacaoCpf validacaoSalva;

	@BeforeEach
	void setUp() {
		dadosValidos = DadosReceitaWs.criarValido(
			"João Silva",
			LocalDate.of(1990, 1, 1),
			"Regular",
			"12345678909"
		);

		Cpf cpf = Cpf.criarSemValidacao("12345678909");
		validacaoSalva = ValidacaoCpf.builder()
			.id(1L)
			.cpf(cpf)
			.valido(true)
			.mensagem("CPF válido")
			.build();
	}

	@Test
	void deveValidarCpfComSucesso() {
		when(receitaWsPort.consultarCpf(any(Cpf.class)))
			.thenReturn(Optional.of(dadosValidos));
		when(repositoryPort.salvar(any(ValidacaoCpf.class)))
			.thenReturn(validacaoSalva);

		ValidacaoCpf resultado = useCase.executar("12345678909");

		assertNotNull(resultado);
		assertTrue(resultado.getValido());
		verify(repositoryPort).salvar(any(ValidacaoCpf.class));
		verify(eventPublisherPort).publicarEventoValidacao(any(ValidacaoCpf.class));
	}

	@Test
	void deveRetornarInvalidoQuandoReceitaWsInvalido() {
		DadosReceitaWs dadosInvalidos = DadosReceitaWs.criarInvalido("Erro");
		when(receitaWsPort.consultarCpf(any(Cpf.class)))
			.thenReturn(Optional.of(dadosInvalidos));

		Cpf cpf = Cpf.criarSemValidacao("12345678909");
		ValidacaoCpf validacaoInvalida = ValidacaoCpf.builder()
			.cpf(cpf)
			.valido(false)
			.mensagem("CPF inválido ou não encontrado")
			.build();

		when(repositoryPort.salvar(any(ValidacaoCpf.class)))
			.thenReturn(validacaoInvalida);

		ValidacaoCpf resultado = useCase.executar("12345678909");

		assertFalse(resultado.getValido());
	}

	@Test
	void deveRetornarInvalidoQuandoReceitaWsNaoEncontra() {
		when(receitaWsPort.consultarCpf(any(Cpf.class)))
			.thenReturn(Optional.empty());

		Cpf cpf = Cpf.criarSemValidacao("12345678909");
		ValidacaoCpf validacaoInvalida = ValidacaoCpf.builder()
			.cpf(cpf)
			.valido(false)
			.mensagem("CPF não encontrado no serviço externo")
			.build();

		when(repositoryPort.salvar(any(ValidacaoCpf.class)))
			.thenReturn(validacaoInvalida);

		ValidacaoCpf resultado = useCase.executar("12345678909");

		assertFalse(resultado.getValido());
	}

	@Test
	void deveLancarExcecaoQuandoCpfInvalido() {
		assertThrows(com.validacpf.domain.exception.CpfInvalidoException.class,
			() -> useCase.executar("11111111111"));
	}

	@Test
	void deveLancarExcecaoQuandoFormatoInvalido() {
		assertThrows(com.validacpf.domain.exception.FormatoCpfException.class,
			() -> useCase.executar("123"));
	}

	@Test
	void deveLancarExcecaoQuandoReceitaWsFalha() {
		when(receitaWsPort.consultarCpf(any(Cpf.class)))
			.thenThrow(new ServicoExternoException("Erro ao consultar"));

		assertThrows(ServicoExternoException.class,
			() -> useCase.executar("12345678909"));
	}

	@Test
	void naoDeveBloquearQuandoEventoFalha() {
		when(receitaWsPort.consultarCpf(any(Cpf.class)))
			.thenReturn(Optional.of(dadosValidos));
		when(repositoryPort.salvar(any(ValidacaoCpf.class)))
			.thenReturn(validacaoSalva);
		doThrow(new RuntimeException("Erro no evento"))
			.when(eventPublisherPort).publicarEventoValidacao(any(ValidacaoCpf.class));

		ValidacaoCpf resultado = useCase.executar("12345678909");

		assertNotNull(resultado);
	}
}
