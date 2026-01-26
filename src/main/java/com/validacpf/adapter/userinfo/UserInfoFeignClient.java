package com.validacpf.adapter.userinfo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-info", url = "https://user-info.herokuapp.com")
public interface UserInfoFeignClient {
	
	@GetMapping("/users/{cpf}")
	UserInfoResponse verificarUsuario(@org.springframework.web.bind.annotation.PathVariable String cpf);
}
