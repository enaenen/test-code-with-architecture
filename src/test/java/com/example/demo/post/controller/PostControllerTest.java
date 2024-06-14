package com.example.demo.post.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.contoller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class PostControllerTest {

	@Test
	void 사용자는_게시물을_단건_조회_할_수_있다() {
		TestContainer testContainer = TestContainer.builder()
				.build();
		User user = User.builder()
				.id(1L)
				.email("enaenen@naver.com")
				.nickname("enaenen")
				.address("Seoul")
				.status(UserStatus.ACTIVE)
				.certificationCode("aaa-bbb-ccc")
				.lastLoginAt(100L)
				.build();
		testContainer.userRepository.save(
				user);
		testContainer.postRepository.save(
				Post.builder()
						.id(1L)
						.content("helloworld")
						.writer(user)
						.createdAt(100L)
						.build()
		);

		ResponseEntity<PostResponse> result = testContainer.postController.getPostById(1L);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getContent()).isEqualTo("helloworld");
		assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
		assertThat(result.getBody().getWriter().getNickname()).isEqualTo("enaenen");
//		assertThat(result.getBody().getId()).isNotNull();
//		assertThat(result.getBody().getModifiedAt()).isNull();
	}

	@Test
	void 사용자는_존재하지_않는_게시물을_조회할_경우_에러가_난다() {
		TestContainer testContainer = TestContainer.builder()
				.build();

		assertThatThrownBy(() -> testContainer.postController.getPostById(99L)).isInstanceOf(
				ResourceNotFoundException.class);

	}


	@Test
	void 사용자는_게시물을_수정할_수_있다() {
		TestContainer testContainer = TestContainer.builder()
				.uuidHolder(() -> "aaa-bbb-ccc")
				.clockHolder(() -> 1678530673958L)
				.build();
		User user = User.builder()
				.id(1L)
				.email("enaenen@naver.com")
				.nickname("enaenen")
				.address("Seoul")
				.status(UserStatus.ACTIVE)
				.certificationCode("aaa-bbb-ccc")
				.lastLoginAt(100L)
				.build();
		testContainer.userRepository.save(
				user);
		testContainer.postRepository.save(
				Post.builder()
						.id(1L)
						.content("helloworld")
						.writer(user)
						.createdAt(100L)
						.build()
		);
		PostUpdate postUpdate = PostUpdate.builder()
				.content("omg")
				.build();

		ResponseEntity<PostResponse> result = testContainer.postController.updatePost(1L, postUpdate);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(1L);
		assertThat(result.getBody().getContent()).isEqualTo("omg");
		assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
		assertThat(result.getBody().getModifiedAt()).isEqualTo(1678530673958L);
		assertThat(result.getBody().getWriter().getNickname()).isEqualTo("enaenen");
	}

}