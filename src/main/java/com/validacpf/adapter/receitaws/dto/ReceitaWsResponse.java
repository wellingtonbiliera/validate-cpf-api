package com.validacpf.adapter.receitaws.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceitaWsResponse {

	private String status;
	
	private String nome;
	
	@JsonProperty("nascimento")
	private String nascimentoStr;
	
	private String situacao;
	
	private String cpf;
	
	private List<String> erro;

	@JsonIgnore
	public LocalDate getNascimento() {
		if (nascimentoStr == null || nascimentoStr.isEmpty()) {
			return null;
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			return LocalDate.parse(nascimentoStr, formatter);
		} catch (Exception e) {
			return null;
		}
	}

	public boolean isValido() {
		return "OK".equals(status) && (erro == null || erro.isEmpty());
	}
}
