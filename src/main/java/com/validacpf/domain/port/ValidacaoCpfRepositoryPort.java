package com.validacpf.domain.port;

import com.validacpf.domain.model.ValidacaoCpf;
import com.validacpf.domain.valueobject.Cpf;

import java.util.Optional;

public interface ValidacaoCpfRepositoryPort {
	ValidacaoCpf salvar(ValidacaoCpf validacao);
	Optional<ValidacaoCpf> buscarPorCpf(Cpf cpf);
}
