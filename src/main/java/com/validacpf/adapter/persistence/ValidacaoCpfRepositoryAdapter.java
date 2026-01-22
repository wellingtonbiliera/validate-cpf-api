package com.validacpf.adapter.persistence;

import com.validacpf.domain.model.ValidacaoCpf;
import com.validacpf.domain.port.ValidacaoCpfRepositoryPort;
import com.validacpf.domain.valueobject.Cpf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidacaoCpfRepositoryAdapter implements ValidacaoCpfRepositoryPort {

	private final ValidaCpfRepository repository;
	private final ValidacaoCpfMapper mapper;

	@Override
	public ValidacaoCpf salvar(final ValidacaoCpf validacao) {
		log.debug("Salvando validação de CPF: {}", validacao.getCpf().getValor());
		
		final var entity = mapper.toEntity(validacao);
		final var entitySalva = repository.save(entity);
		
		log.debug("Validação salva com ID: {}", entitySalva.getId());
		return mapper.toDomain(entitySalva);
	}

	@Override
	public Optional<ValidacaoCpf> buscarPorCpf(final Cpf cpf) {
		log.debug("Buscando validação por CPF: {}", cpf.getValor());
		
		return repository.findByCpf(cpf.getValor())
			.map(mapper::toDomain);
	}
}
