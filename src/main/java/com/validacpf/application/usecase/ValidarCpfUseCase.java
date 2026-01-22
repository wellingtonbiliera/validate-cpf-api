package com.validacpf.application.usecase;

import com.validacpf.domain.exception.ServicoExternoException;
import com.validacpf.domain.model.DadosReceitaWs;
import com.validacpf.domain.model.ValidacaoCpf;
import com.validacpf.domain.port.EventPublisherPort;
import com.validacpf.domain.port.ReceitaWsPort;
import com.validacpf.domain.port.ValidacaoCpfRepositoryPort;
import com.validacpf.domain.valueobject.Cpf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidarCpfUseCase {

	private final ReceitaWsPort receitaWsPort;
	private final ValidacaoCpfRepositoryPort repositoryPort;
	private final EventPublisherPort eventPublisherPort;

	@Transactional
	public ValidacaoCpf executar(final String cpfInput) {
		log.info("Iniciando validação de CPF: {}", cpfInput);

		final var cpf = Cpf.criar(cpfInput);
		final var validacao = processarValidacao(cpf);

		final var validacaoSalva = repositoryPort.salvar(validacao);
		publicarEventoAssincrono(validacaoSalva);

		log.info("Validação concluída - CPF: {}, Válido: {}", cpf, validacaoSalva.getValido());
		return validacaoSalva;
	}

	private ValidacaoCpf processarValidacao(final Cpf cpf) {
		final var dadosReceitaWs = consultarReceitaWs(cpf);

		return dadosReceitaWs
			.filter(DadosReceitaWs::isValido)
			.map(dados -> criarValidacaoValida(cpf, dados))
			.orElseGet(() -> criarValidacaoInvalida(cpf, dadosReceitaWs));
	}

	private Optional<DadosReceitaWs> consultarReceitaWs(final Cpf cpf) {
		try {
			return receitaWsPort.consultarCpf(cpf);
		} catch (final ServicoExternoException e) {
			log.error("Erro ao consultar ReceitaWS: {}", e.getMessage());
			throw e;
		} catch (final Exception e) {
			log.error("Erro inesperado ao consultar ReceitaWS", e);
			throw new ServicoExternoException("Erro ao consultar serviço externo", e);
		}
	}

	private ValidacaoCpf criarValidacaoValida(final Cpf cpf, final DadosReceitaWs dados) {
		return ValidacaoCpf.criarValidacaoValida(
			cpf,
			dados.getNome(),
			dados.getDataNascimento(),
			dados.getSituacao(),
			dados.getStatus()
		);
	}

	private ValidacaoCpf criarValidacaoInvalida(final Cpf cpf, final Optional<DadosReceitaWs> dadosReceitaWs) {
		final var motivo = dadosReceitaWs
			.map(dados -> "CPF inválido ou não encontrado")
			.orElse("CPF não encontrado no serviço externo");

		return ValidacaoCpf.criarValidacaoInvalida(cpf, motivo);
	}

	private void publicarEventoAssincrono(final ValidacaoCpf validacao) {
		try {
			eventPublisherPort.publicarEventoValidacao(validacao);
		} catch (final Exception e) {
			log.error("Erro ao publicar evento de validação para CPF: {}", validacao.getCpf(), e);
		}
	}
}
