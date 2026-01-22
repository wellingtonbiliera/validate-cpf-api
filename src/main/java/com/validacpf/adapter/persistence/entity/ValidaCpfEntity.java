package com.validacpf.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "VALIDA_CPF", indexes = {
	@Index(name = "IDX_VALIDA_CPF_DATA_VALIDACAO", columnList = "DATA_VALIDACAO"),
	@Index(name = "IDX_VALIDA_CPF_CPF", columnList = "CPF")
})
@SequenceGenerator(name = "VALIDA_CPF_SEQ", sequenceName = "VALIDA_CPF_SEQ", allocationSize = 1)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidaCpfEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VALIDA_CPF_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "CPF", nullable = false, length = 14)
	private String cpf;

	@Column(name = "VALIDO", nullable = false)
	private Boolean valido;

	@Column(name = "DATA_VALIDACAO", nullable = false)
	private LocalDateTime dataValidacao;

	@Column(name = "MENSAGEM", length = 500)
	private String mensagem;

	@Column(name = "CODIGO_RETORNO", length = 10)
	private String codigoRetorno;

	@Column(name = "NOME", length = 255)
	private String nome;

	@Column(name = "DATA_NASCIMENTO")
	private LocalDate dataNascimento;

	@Column(name = "SITUACAO", length = 50)
	private String situacao;

	@Column(name = "TIMESTAMP_EVENTO")
	private LocalDateTime timestampEvento;
}
