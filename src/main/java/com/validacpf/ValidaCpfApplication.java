package com.validacpf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ValidaCpfApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidaCpfApplication.class, args);
	}

}
