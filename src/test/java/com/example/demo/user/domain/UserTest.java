package com.example.demo.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import org.junit.jupiter.api.Test;

public class UserTest {

	@Test
	public void User는_UserCreate_객체로_생성할_수_있다() {
		//given
		UserCreate userCreate = UserCreate.builder()
				.email("enaenen@naver.com")
				.nickname("enaenen")
				.address("Seoul")
				.build();
		//when
		User user = User.from(userCreate, new TestUuidHolder("AAA-BBB-CCC"));
		//then
		assertThat(user.getId()).isNull();
		assertThat(user.getEmail()).isEqualTo("enaenen@naver.com");
		assertThat(user.getNickname()).isEqualTo("enaenen");
		assertThat(user.getAddress()).isEqualTo("Seoul");
		assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
		assertThat(user.getCertificationCode()).isEqualTo("AAA-BBB-CCC");
	}

	@Test
	public void User는_UserUpdate_객체로_데이터를_업데이트_할_수_있다() {
		//given
		User user = User.builder()
				.id(1L)
				.email("enaenen@naver.com")
				.nickname("enaenen")
				.address("Seoul")
				.status(UserStatus.ACTIVE)
				.certificationCode("AAA-BBB-CCC")
				.lastLoginAt(100L)
				.build();

		UserUpdate userUpdate = UserUpdate.builder()
				.nickname("changed")
				.address("bundang")
				.build();
		//when
		user = user.update(userUpdate);

		//then
		assertThat(user.getId()).isEqualTo(1L);
		assertThat(user.getEmail()).isEqualTo("enaenen@naver.com");
		assertThat(user.getNickname()).isEqualTo("changed");
		assertThat(user.getAddress()).isEqualTo("bundang");
		assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(user.getCertificationCode()).isEqualTo("AAA-BBB-CCC");
		assertThat(user.getLastLoginAt()).isEqualTo(100L);
	}

	@Test
	public void User는_로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
		//given
		User user = User.builder()
				.id(1L)
				.email("enaenen@naver.com")
				.nickname("enaenen")
				.address("Seoul")
				.status(UserStatus.ACTIVE)
				.certificationCode("AAA-BBB-CCC")
				.lastLoginAt(100L)
				.build();
		//when
		user = user.login(new TestClockHolder(1678530673958L));

		//then
		assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
	}

	@Test
	public void User는_유효환_인증_코드로_계정을_활성화_할_수_있다() {
		//given
		User user = User.builder()
				.id(1L)
				.email("enaenen@naver.com")
				.nickname("enaenen")
				.address("Seoul")
				.status(UserStatus.PENDING)
				.certificationCode("AAA-BBB-CCC")
				.lastLoginAt(100L)
				.build();

		//when
		user = user.certificate("AAA-BBB-CCC");

		//then
		assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	public void User는_잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다() {
		//given
		User user = User.builder()
				.id(1L)
				.email("enaenen@naver.com")
				.nickname("enaenen")
				.address("Seoul")
				.status(UserStatus.PENDING)
				.certificationCode("AAA-BBB-CCC")
				.lastLoginAt(100L)
				.build();
		//when
		//then
		assertThatThrownBy(() -> user.certificate("wrong code")).isInstanceOf(
				CertificationCodeNotMatchedException.class);
	}


}
