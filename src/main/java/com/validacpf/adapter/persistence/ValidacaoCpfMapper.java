package com.validacpf.adapter.persistence;

import com.validacpf.domain.model.ValidacaoCpf;
import com.validacpf.domain.valueobject.Cpf;
import com.validacpf.adapter.persistence.entity.ValidaCpfEntity;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoCpfMapper {

	public ValidaCpfEntity toEntity(final ValidacaoCpf validacao) {
		final var entity = new ValidaCpfEntity();
		
		if (validacao.getId() != null) {
			entity.setId(validacao.getId());
		}
		
		entity.setCpf(validacao.getCpf().getValor());
		entity.setValido(validacao.getValido());
		entity.setDataValidacao(validacao.getDataValidacao());
		entity.setMensagem(validacao.getMensagem());
		entity.setCodigoRetorno(validacao.getCodigoRetorno());
		entity.setNome(validacao.getNome());
		entity.setDataNascimento(validacao.getDataNascimento());
		entity.setSituacao(validacao.getSituacao());
		entity.setTimestampEvento(validacao.getTimestampEvento());
		
		return entity;
	}

	public ValidacaoCpf toDomain(final ValidaCpfEntity entity) {
		final var cpf = Cpf.criarSemValidacao(entity.getCpf());
		
		return ValidacaoCpf.builder()
			.id(entity.getId())
			.cpf(cpf)
			.valido(entity.getValido())
			.dataValidacao(entity.getDataValidacao())
			.mensagem(entity.getMensagem())
			.codigoRetorno(entity.getCodigoRetorno())
			.nome(entity.getNome())
			.dataNascimento(entity.getDataNascimento())
			.situacao(entity.getSituacao())
			.timestampEvento(entity.getTimestampEvento())
			.build();
	}
}
