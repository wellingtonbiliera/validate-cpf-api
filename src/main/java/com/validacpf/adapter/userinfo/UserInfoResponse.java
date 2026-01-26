package com.validacpf.adapter.userinfo;

import lombok.Data;

@Data
public class UserInfoResponse {
	private String status; // ABLE_TO_VOTE ou UNABLE_TO_VOTE
}
