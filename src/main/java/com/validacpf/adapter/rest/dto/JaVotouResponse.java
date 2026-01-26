package com.validacpf.adapter.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Response para verificação se CPF já votou na pauta")
@Data
public class JaVotouResponse {
	
	@Schema(description = "Indica se o CPF já votou na pauta", example = "true")
	private Boolean jaVotou;
}
