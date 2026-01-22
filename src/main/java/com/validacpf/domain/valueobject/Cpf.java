package com.validacpf.domain.valueobject;

import com.validacpf.domain.exception.CpfInvalidoException;
import com.validacpf.domain.exception.FormatoCpfException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode
public class Cpf {

	private static final int[] PESO_DIGITO_1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
	private static final int[] PESO_DIGITO_2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
	private static final int TAMANHO_CPF = 11;

	private final String valor;
	private final String valorFormatado;

	private Cpf(final String valor, final boolean validar) {
		this.valor = removerFormatacao(Objects.requireNonNull(valor, "CPF não pode ser nulo"));
		if (validar) {
			validar();
		}
		this.valorFormatado = formatar();
	}

	public static Cpf criar(final String valor) {
		return new Cpf(valor, true);
	}

	public static Cpf criarSemValidacao(final String valor) {
		return new Cpf(valor, false);
	}

	private void validar() {
		validarFormato();
		validarDigitosRepetidos();
		validarDigitosVerificadores();
	}

	private void validarFormato() {
		if (valor.length() != TAMANHO_CPF) {
			throw new FormatoCpfException("CPF deve conter 11 dígitos numéricos");
		}
	}

	private void validarDigitosRepetidos() {
		final var primeiroDigito = valor.charAt(0);
		final var todosIguais = valor.chars()
			.allMatch(digito -> digito == primeiroDigito);
		
		if (todosIguais) {
			throw new CpfInvalidoException("CPF inválido: dígitos repetidos");
		}
	}

	private void validarDigitosVerificadores() {
		final var digito1 = calcularDigitoVerificador(valor, PESO_DIGITO_1);
		if (digito1 != Character.getNumericValue(valor.charAt(9))) {
			throw new CpfInvalidoException("CPF inválido: dígitos verificadores incorretos");
		}

		final var digito2 = calcularDigitoVerificador(valor, PESO_DIGITO_2);
		if (digito2 != Character.getNumericValue(valor.charAt(10))) {
			throw new CpfInvalidoException("CPF inválido: dígitos verificadores incorretos");
		}
	}

	private int calcularDigitoVerificador(final String cpf, final int[] peso) {
		var soma = 0;
		for (var i = 0; i < peso.length; i++) {
			soma += Character.getNumericValue(cpf.charAt(i)) * peso[i];
		}
		final var resto = soma % 11;
		return resto < 2 ? 0 : 11 - resto;
	}

	private String removerFormatacao(final String cpf) {
		return cpf.replaceAll("[^0-9]", "");
	}

	private String formatar() {
		if (valor.length() != TAMANHO_CPF) {
			return valor;
		}
		return String.format("%s.%s.%s-%s",
			valor.substring(0, 3),
			valor.substring(3, 6),
			valor.substring(6, 9),
			valor.substring(9, 11));
	}

	@Override
	public String toString() {
		return valorFormatado;
	}
}
