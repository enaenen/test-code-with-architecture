package com.example.demo.user.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import com.example.demo.user.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class UserControllerTest {

	@Test
	void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달받을_수_있다() {
		//given
		TestContainer testContainer = TestContainer.builder()
				.build();
		testContainer.userRepository.save(
				User.builder()
						.id(1L)
						.email("enaenen@naver.com")
						.nickname("enaenen")
						.address("Seoul")
						.status(UserStatus.ACTIVE)
						.certificationCode("aaa-bbb-ccc")
						.lastLoginAt(100L)
						.build()
		);

		//when
		ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1L);

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(1L);
		assertThat(result.getBody().getEmail()).isEqualTo("enaenen@naver.com");
		assertThat(result.getBody().getNickname()).isEqualTo("enaenen");
		assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
		assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);

	}

	@Test
	void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() {
		//given
		TestContainer testContainer = TestContainer.builder()
				.build();
		//when
		//then
		assertThatThrownBy(
				() -> testContainer.userController.getUserById(1L)).isInstanceOf(
				ResourceNotFoundException.class);
	}

	@Test
	void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() {
		//given
		TestContainer testContainer = TestContainer.builder()
				.build();
		testContainer.userRepository.save(
				User.builder()
						.id(1L)
						.email("enaenen@naver.com")
						.nickname("enaenen")
						.address("Seoul")
						.status(UserStatus.PENDING)
						.certificationCode("aaa-bbb-ccc")
						.build()
		);

		//when
		ResponseEntity<Void> result = testContainer.userController.verifyEmail(1L, "aaa-bbb-ccc");

		//then

		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
		assertThat(testContainer.userRepository.getById(1).getStatus()).isEqualTo(
				UserStatus.ACTIVE);
	}

	@Test
	void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() {
		//given
		TestContainer testContainer = TestContainer.builder()
				.build();
		testContainer.userRepository.save(
				User.builder()
						.id(1L)
						.email("enaenen@naver.com")
						.nickname("enaenen")
						.address("Seoul")
						.status(UserStatus.PENDING)
						.certificationCode("aaa-bbb-ccc")
						.build()
		);

		//when
		assertThatThrownBy(
				() -> testContainer.userController.verifyEmail(1L, "wrong")).isInstanceOf(
				CertificationCodeNotMatchedException.class);
		//then

	}

	@Test
	void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() {
		//given
		TestContainer testContainer = TestContainer.builder()
				.clockHolder(() -> 1678530673958L)
				.build();
		testContainer.userRepository.save(
				User.builder()
						.id(1L)
						.email("enaenen@naver.com")
						.nickname("enaenen")
						.address("Seoul")
						.status(UserStatus.ACTIVE)
						.certificationCode("aaa-bbb-ccc")
						.build()
		);

		//when
		ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo(
				"enaenen@naver.com");

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(1L);
		assertThat(result.getBody().getEmail()).isEqualTo("enaenen@naver.com");
		assertThat(result.getBody().getNickname()).isEqualTo("enaenen");
		assertThat(result.getBody().getLastLoginAt()).isEqualTo(1678530673958L);
		assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
		assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	void 사용자는_내_정보를_수정할_수_있다() {
		//given
		TestContainer testContainer = TestContainer.builder()
				.build();
		testContainer.userRepository.save(
				User.builder()
						.id(1L)
						.email("enaenen@naver.com")
						.nickname("enaenen")
						.address("Seoul")
						.status(UserStatus.ACTIVE)
						.certificationCode("aaa-bbb-ccc")
						.lastLoginAt(100L)
						.build()
		);

		//when
		ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo(
				"enaenen@naver.com", UserUpdate.builder()
						.address("Pangyo")
						.nickname("enaenen-2")
						.build());

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(1L);
		assertThat(result.getBody().getEmail()).isEqualTo("enaenen@naver.com");
		assertThat(result.getBody().getNickname()).isEqualTo("enaenen-2");
		assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
		assertThat(result.getBody().getAddress()).isEqualTo("Pangyo");
		assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

}