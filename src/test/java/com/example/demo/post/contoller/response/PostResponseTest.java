package com.example.demo.post.contoller.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.post.domain.Post;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class PostResponseTest {
	@Test
	void Post로_응답을_생성할_수_있다(){

		Post post = Post.builder()
				.id(1L)
				.content("helloworld")
				.writer(User.builder()
						.email("enaenen@naver.com")
						.nickname("enaenen")
						.address("Seoul")
						.status(UserStatus.ACTIVE)
						.certificationCode("AAA-BBB-CCC")
						.build())
				.build();

		PostResponse postResponse = PostResponse.from(post);

		assertThat(postResponse.getContent()).isEqualTo("helloworld");
		assertThat(postResponse.getWriter().getEmail()).isEqualTo("enaenen@naver.com");
		assertThat(postResponse.getWriter().getNickname()).isEqualTo("enaenen");
		assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);

	}

}