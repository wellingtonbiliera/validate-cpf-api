package com.validacpf.adapter.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Response para verificação se CPF pode votar")
@Data
public class PodeVotarResponse {
	
	@Schema(description = "Status de habilitação para votar", 
		example = "ABLE_TO_VOTE", 
		allowableValues = {"ABLE_TO_VOTE", "UNABLE_TO_VOTE"})
	private String status;
}
