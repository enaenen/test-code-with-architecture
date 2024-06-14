package com.example.demo.user.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;

class UserCreateControllerTest {

	@Test
	void 사용자는_회원_가입을_할_수_있고_회원가입된_사용자는_PENDING_상태이다() {
		//given
		UserCreate userCreate = UserCreate.builder()
				.nickname("enaenen")
				.email("enaenen@naver.com")
				.address("Pangyo")
				.build();
		TestContainer testContainer = TestContainer.builder()
				.uuidHolder(() -> "aaa-bbb-ccc")
				.build();

		//when
		ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(
				userCreate);

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isNotNull();
		assertThat(result.getBody().getEmail()).isEqualTo("enaenen@naver.com");
		assertThat(result.getBody().getNickname()).isEqualTo("enaenen");
		assertThat(result.getBody().getLastLoginAt()).isNull();
		assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
		assertThat(testContainer.userRepository.getById(1).getCertificationCode()).isEqualTo("aaa-bbb-ccc");

	}
}