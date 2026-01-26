package com.validacpf.adapter.votacao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "votacao", url = "${votacao.url:http://localhost:8080}")
public interface VotacaoFeignClient {
	
	@GetMapping("/api/v1/pautas/{pautaId}/votos/{associadoId}")
	JaVotouResponse verificarJaVotou(@PathVariable Long pautaId, @PathVariable String associadoId);
}
