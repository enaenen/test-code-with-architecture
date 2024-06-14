package com.example.demo.user.controller.request;

import com.example.demo.user.domain.UserUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
	private final String nickname;
	private final String address;

	@Builder
	public UserUpdateRequest(
			@JsonProperty("nickname") String nickname,
			@JsonProperty("address") String address
	){
		this.nickname = nickname;
		this.address = address;
	}
	/*
	이것까지 작성할 수 있겠지만 too much 라서 생략
	public UserUpdate to() {
		...
	}
*/
}
