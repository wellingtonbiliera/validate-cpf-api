package com.validacpf.domain.exception;

public class CpfInvalidoException extends RuntimeException {

	public CpfInvalidoException(String mensagem) {
		super(mensagem);
	}
}
