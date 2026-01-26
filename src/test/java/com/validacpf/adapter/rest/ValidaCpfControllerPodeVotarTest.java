package com.validacpf.adapter.rest;

import com.validacpf.adapter.userinfo.UserInfoAdapter;
import com.validacpf.domain.valueobject.Cpf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ValidaCpfController.class)
@DisplayName("ValidaCpfController - Pode Votar")
class ValidaCpfControllerPodeVotarTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserInfoAdapter userInfoAdapter;
	
	@MockBean
	private com.validacpf.application.usecase.ValidarCpfUseCase validarCpfUseCase;
	
	@MockBean
	private com.validacpf.domain.port.ValidacaoCpfRepositoryPort repositoryPort;
	
	@MockBean
	private com.validacpf.adapter.rest.mapper.ValidaCpfMapper mapper;
	
	@MockBean
	private com.validacpf.adapter.votacao.VotacaoFeignClient votacaoFeignClient;
	
	@Test
	@DisplayName("Deve retornar ABLE_TO_VOTE quando CPF pode votar")
	void deveRetornarAbleToVote() throws Exception {
		when(userInfoAdapter.podeVotar(anyString())).thenReturn(true);
		
		mockMvc.perform(get("/api/validacoes-cpf/12345678901/pode-votar"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("ABLE_TO_VOTE"));
	}
	
	@Test
	@DisplayName("Deve retornar UNABLE_TO_VOTE quando CPF não pode votar")
	void deveRetornarUnableToVote() throws Exception {
		when(userInfoAdapter.podeVotar(anyString())).thenReturn(false);
		
		mockMvc.perform(get("/api/validacoes-cpf/12345678901/pode-votar"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("UNABLE_TO_VOTE"));
	}
}
