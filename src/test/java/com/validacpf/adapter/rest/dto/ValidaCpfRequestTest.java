package com.validacpf.adapter.rest.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidaCpfRequestTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void deveCriarRequestComCpfSemFormatacao() {
		ValidaCpfRequest request = new ValidaCpfRequest("12345678901");

		Set<ConstraintViolation<ValidaCpfRequest>> violations = validator.validate(request);

		assertTrue(violations.isEmpty());
		assertEquals("12345678901", request.getCpf());
	}

	@Test
	void deveCriarRequestComCpfFormatado() {
		ValidaCpfRequest request = new ValidaCpfRequest("123.456.789-01");

		Set<ConstraintViolation<ValidaCpfRequest>> violations = validator.validate(request);

		assertTrue(violations.isEmpty());
		assertEquals("123.456.789-01", request.getCpf());
	}

	@Test
	void deveValidarCpfVazio() {
		ValidaCpfRequest request = new ValidaCpfRequest("");

		Set<ConstraintViolation<ValidaCpfRequest>> violations = validator.validate(request);

		assertFalse(violations.isEmpty());
	}

	@Test
	void deveValidarCpfNull() {
		ValidaCpfRequest request = new ValidaCpfRequest(null);

		Set<ConstraintViolation<ValidaCpfRequest>> violations = validator.validate(request);

		assertFalse(violations.isEmpty());
	}

	@Test
	void deveValidarFormatoInvalido() {
		ValidaCpfRequest request = new ValidaCpfRequest("123");

		Set<ConstraintViolation<ValidaCpfRequest>> violations = validator.validate(request);

		assertFalse(violations.isEmpty());
	}

	@Test
	void deveTestarEqualsEHashCode() {
		ValidaCpfRequest request1 = new ValidaCpfRequest("12345678909");
		ValidaCpfRequest request2 = new ValidaCpfRequest("12345678909");
		ValidaCpfRequest request3 = new ValidaCpfRequest("98765432100");
		
		assertEquals(request1, request2);
		assertEquals(request1.hashCode(), request2.hashCode());
		assertNotEquals(request1, request3);
		assertNotEquals(request1, null);
	}

	@Test
	void deveTestarToString() {
		ValidaCpfRequest request = new ValidaCpfRequest("12345678909");
		String toString = request.toString();
		assertNotNull(toString);
		assertTrue(toString.contains("ValidaCpfRequest"));
	}

	@Test
	void deveCriarRequestComConstrutorPadrao() {
		ValidaCpfRequest request = new ValidaCpfRequest();
		assertNotNull(request);
	}

	@Test
	void deveTestarGetterESetter() {
		ValidaCpfRequest request = new ValidaCpfRequest();
		request.setCpf("98765432100");
		assertEquals("98765432100", request.getCpf());
	}

	@Test
	void deveValidarCpfComMenosDigitos() {
		ValidaCpfRequest request = new ValidaCpfRequest("1234567890");
		Set<ConstraintViolation<ValidaCpfRequest>> violations = validator.validate(request);
		assertFalse(violations.isEmpty());
	}

	@Test
	void deveTestarCanEqual() {
		ValidaCpfRequest request1 = new ValidaCpfRequest("12345678909");
		ValidaCpfRequest request2 = new ValidaCpfRequest("12345678909");
		assertTrue(request1.canEqual(request2));
		assertFalse(request1.canEqual("not a ValidaCpfRequest"));
	}
}
