package com.example.demo.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.common.service.ClockHolder;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class PostTest {

	@Test
	public void PostCreate으로_게시물을_만들_수_있다() {
		//given
		PostCreate postCreate = PostCreate.builder()
				.writerId(1)
				.content("helloworld")
				.build();
		User writer = User.builder()
				.email("enaenen@naver.com")
				.nickname("enaenen")
				.address("Seoul")
				.status(UserStatus.ACTIVE)
				.certificationCode("AAA-BBB-CCC")
				.build();
		ClockHolder clockHolder = new TestClockHolder(0L);

		//when
		Post post = Post.from(writer, postCreate, clockHolder);

		//then
		assertThat(post.getContent()).isEqualTo("helloworld");
		assertThat(post.getWriter().getEmail()).isEqualTo("enaenen@naver.com");
		assertThat(post.getWriter().getNickname()).isEqualTo("enaenen");
		assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
		assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(post.getWriter().getCertificationCode()).isEqualTo("AAA-BBB-CCC");
		assertThat(post.getCreatedAt()).isEqualTo(0L);
	}

	@Test
	public void PostUpdate로_게시물을_수정할_수_있다() {
		//given
		PostUpdate postUpdate = PostUpdate.builder()
				.content("omg")
				.build();

		User writer = User.builder()
				.email("enaenen@naver.com")
				.nickname("enaenen")
				.address("Seoul")
				.status(UserStatus.ACTIVE)
				.certificationCode("AAA-BBB-CCC")
				.build();
		Post post = Post.builder()
				.id(1L)
				.content("helloworld")
				.writer(writer)
				.build();

		//when
		post = post.update(postUpdate, new TestClockHolder(1678530673958L));

		//then
		assertThat(post.getContent()).isEqualTo("omg");
		assertThat(post.getModifiedAt()).isEqualTo(1678530673958L);
	}
}