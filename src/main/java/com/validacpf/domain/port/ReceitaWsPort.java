package com.validacpf.domain.port;

import com.validacpf.domain.valueobject.Cpf;
import com.validacpf.domain.model.DadosReceitaWs;

import java.util.Optional;

public interface ReceitaWsPort {
	Optional<DadosReceitaWs> consultarCpf(Cpf cpf);
}
