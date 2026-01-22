package com.validacpf.adapter.receitaws;

import com.validacpf.config.FeignConfig;
import com.validacpf.adapter.receitaws.dto.ReceitaWsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "receitaws", url = "${receitaws.url}", configuration = FeignConfig.class)
public interface ReceitaWsFeignClient {

	@GetMapping(value = "/v1/cpf/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	ReceitaWsResponse validarCPF(@PathVariable("cpf") final String cpf);
}
