package com.example.demo.post.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.contoller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class PostCreateControllerTest {

	@Test
	void 사용자는_게시물을_작성할_수_있다() {

		//given
		TestContainer testContainer = TestContainer.builder()
				.uuidHolder(() -> "aaa-bbb-ccc")
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
						.lastLoginAt(100L)
						.build());

		PostCreate postCreate = PostCreate.builder()
				.content("helloworld")
				.writerId(1)
				.build();
		//when
		ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(
				postCreate);

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isNotNull();
		assertThat(result.getBody().getContent()).isEqualTo("helloworld");
		assertThat(result.getBody().getCreatedAt()).isEqualTo(1678530673958L);
		assertThat(result.getBody().getWriter().getNickname()).isEqualTo("enaenen");
//		assertThat(result.getBody().getModifiedAt()).isNull();
//		assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
//		assertThat(result.getBody().getWriter().getEmail()).isEqualTo("enaenen@naver.com");
//		assertThat(result.getBody().getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
	}
}