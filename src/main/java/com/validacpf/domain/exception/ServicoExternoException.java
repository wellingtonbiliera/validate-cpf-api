package com.validacpf.domain.exception;

public class ServicoExternoException extends RuntimeException {

	public ServicoExternoException(String mensagem) {
		super(mensagem);
	}

	public ServicoExternoException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
