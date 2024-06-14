package com.example.demo.user.controller.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class UserResponseTest {

	@Test
	public void User으로_응답을_생성할_수_있다(){
		User user = User.builder()
				.id(1L)
				.email("enaenen@naver.com")
				.nickname("enaenen")
				.address("Seoul")
				.status(UserStatus.ACTIVE)
				.certificationCode("AAA-BBB-CCC")
				.lastLoginAt(100L)
				.build();

		UserResponse userResponse = UserResponse.from(user);

		assertThat(userResponse.getId()).isEqualTo(1);
		assertThat(userResponse.getEmail()).isEqualTo("enaenen@naver.com");
		assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
	}

}