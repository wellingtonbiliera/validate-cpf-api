package com.validacpf.adapter.userinfo;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoAdapter {
	
	private final UserInfoFeignClient feignClient;
	
	public boolean podeVotar(final String cpf) {
		log.debug("Verificando se CPF {} pode votar no serviço externo", cpf);
		try {
			final var response = feignClient.verificarUsuario(cpf);
			return response != null && "ABLE_TO_VOTE".equals(response.getStatus());
		} catch (final FeignException e) {
			if (e.status() == 404) {
				log.warn("CPF não encontrado no serviço externo: {}", cpf);
				return false;
			}
			throw e;
		} catch (final Exception e) {
			log.error("Erro ao verificar se CPF pode votar: {}", e.getMessage());
			return false;
		}
	}
}
