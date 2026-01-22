package com.validacpf.domain.valueobject;

import com.validacpf.domain.exception.CpfInvalidoException;
import com.validacpf.domain.exception.FormatoCpfException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpfTest {

	@Test
	void deveCriarCpfComFormatacao() {
		Cpf cpf = Cpf.criar("123.456.789-09");
		assertEquals("12345678909", cpf.getValor());
		assertEquals("123.456.789-09", cpf.getValorFormatado());
	}

	@Test
	void deveCriarCpfSemFormatacao() {
		Cpf cpf = Cpf.criar("12345678909");
		assertEquals("12345678909", cpf.getValor());
		assertEquals("123.456.789-09", cpf.getValorFormatado());
	}

	@Test
	void deveLancarExcecaoFormatoInvalido() {
		assertThrows(FormatoCpfException.class, () -> Cpf.criar("1234567890"));
		assertThrows(FormatoCpfException.class, () -> Cpf.criar("123456789012"));
	}

	@Test
	void deveLancarExcecaoDigitosRepetidos() {
		assertThrows(CpfInvalidoException.class, () -> Cpf.criar("11111111111"));
		assertThrows(CpfInvalidoException.class, () -> Cpf.criar("00000000000"));
	}

	@Test
	void deveLancarExcecaoDigitosVerificadoresInvalidos() {
		assertThrows(CpfInvalidoException.class, () -> Cpf.criar("12345678901"));
	}

	@Test
	void deveLancarExcecaoCpfNulo() {
		assertThrows(NullPointerException.class, () -> Cpf.criar(null));
	}

	@Test
	void deveCriarCpfSemValidacao() {
		Cpf cpf = Cpf.criarSemValidacao("12345678901");
		assertEquals("12345678901", cpf.getValor());
	}

	@Test
	void deveRetornarToStringFormatado() {
		Cpf cpf = Cpf.criarSemValidacao("12345678901");
		assertEquals("123.456.789-01", cpf.toString());
	}

	@Test
	void deveCriarCpfComDiferentesFormatos() {
		// Com pontos e traço
		Cpf cpf1 = Cpf.criar("111.444.777-35");
		assertEquals("11144477735", cpf1.getValor());
		assertEquals("111.444.777-35", cpf1.getValorFormatado());
		
		// Com espaços e outros caracteres
		Cpf cpf2 = Cpf.criarSemValidacao("111 444 777 35");
		assertEquals("11144477735", cpf2.getValor());
	}

	@Test
	void deveValidarCpfComRestoMaiorOuIgualA2() {
		// CPF válido: 11144477735
		Cpf cpf = Cpf.criar("111.444.777-35");
		assertEquals("11144477735", cpf.getValor());
	}

	@Test
	void deveValidarCpfComRestoMenorQue2() {
		// CPF válido: 12345678909 (dígito verificador 0)
		Cpf cpf = Cpf.criar("123.456.789-09");
		assertEquals("12345678909", cpf.getValor());
	}

	@Test
	void deveLancarExcecaoSegundoDigitoVerificadorInvalido() {
		// CPF com primeiro dígito correto mas segundo incorreto
		assertThrows(CpfInvalidoException.class, () -> Cpf.criar("12345678900"));
	}

	@Test
	void deveFormatarCpfSemValidacao() {
		Cpf cpf = Cpf.criarSemValidacao("98765432100");
		assertEquals("987.654.321-00", cpf.getValorFormatado());
		assertEquals("987.654.321-00", cpf.toString());
	}

	@Test
	void deveRemoverFormatacaoComVariosCaracteres() {
		Cpf cpf = Cpf.criarSemValidacao("123-456.789/09");
		assertEquals("12345678909", cpf.getValor());
	}

	@Test
	void deveTestarEqualsEHashCode() {
		Cpf cpf1 = Cpf.criar("123.456.789-09");
		Cpf cpf2 = Cpf.criar("12345678909");
		Cpf cpf3 = Cpf.criar("987.654.321-00");
		
		assertEquals(cpf1, cpf2);
		assertEquals(cpf1.hashCode(), cpf2.hashCode());
		assertNotEquals(cpf1, cpf3);
	}

	@Test
	void deveTestarGetters() {
		Cpf cpf = Cpf.criar("123.456.789-09");
		
		assertEquals("12345678909", cpf.getValor());
		assertEquals("123.456.789-09", cpf.getValorFormatado());
	}
}
