package com.validacpf.adapter.receitaws;

import com.validacpf.domain.exception.ServicoExternoException;
import com.validacpf.domain.model.DadosReceitaWs;
import com.validacpf.domain.port.ReceitaWsPort;
import com.validacpf.domain.valueobject.Cpf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReceitaWsAdapter implements ReceitaWsPort {

	private final ReceitaWsFeignClient feignClient;
	private final ReceitaWsMapper mapper;

	@Override
	public Optional<DadosReceitaWs> consultarCpf(final Cpf cpf) {
		log.debug("Consultando ReceitaWS para CPF: {}", cpf.getValor());
		
		try {
			final var response = feignClient.validarCPF(cpf.getValor());
			return mapper.toDadosReceitaWs(response);
		} catch (final feign.FeignException.NotFound e) {
			log.warn("CPF não encontrado no ReceitaWS: {}", cpf.getValor());
			return Optional.empty();
		} catch (final feign.FeignException e) {
			log.error("Erro ao consultar ReceitaWS - Status: {}, Mensagem: {}", e.status(), e.getMessage());
			throw new ServicoExternoException("Erro ao consultar ReceitaWS: " + e.getMessage(), e);
		} catch (final Exception e) {
			log.error("Erro inesperado ao consultar ReceitaWS", e);
			throw new ServicoExternoException("Erro inesperado ao consultar ReceitaWS", e);
		}
	}
}
