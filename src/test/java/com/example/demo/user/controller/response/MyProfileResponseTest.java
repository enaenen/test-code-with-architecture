package com.example.demo.user.controller.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class MyProfileResponseTest {
	@Test
	public void User으로_응답을_생성할_수_있다(){

		User user = User.builder()
				.id(1L)
				.email("enaenen@naver.com")
				.nickname("enaenen")
				.address("Seoul")
				.status(UserStatus.ACTIVE)
				.lastLoginAt(100L)
				.certificationCode("AAA-BBB-CCC")
				.build();

		MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

		assertThat(myProfileResponse.getId()).isEqualTo(1);
		assertThat(myProfileResponse.getEmail()).isEqualTo("enaenen@naver.com");
		assertThat(myProfileResponse.getNickname()).isEqualTo("enaenen");
		assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
		assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);

	}

}