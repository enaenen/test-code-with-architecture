package com.example.demo.post.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.common.service.ClockHolder;
import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PostServiceTest {

	private PostService postService;

	@BeforeEach
	void init() {
		FakePostRepository fakePostRepository = new FakePostRepository();
		FakeUserRepository fakeUserRepository = new FakeUserRepository();
		ClockHolder clockHolder = new TestClockHolder(0L);

		this.postService = PostService.builder()
				.userRepository(fakeUserRepository)
				.postRepository(fakePostRepository)
				.clockHolder(clockHolder)
				.build();

		User user1 = User.builder()
				.id(1L)
				.email("enaenen@naver.com")
				.nickname("enaenen")
				.address("Seoul")
				.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
				.status(UserStatus.ACTIVE)
				.lastLoginAt(0L)
				.build();

		User user2 = User.builder()
				.id(2L)
				.email("space@naver.com")
				.nickname("space")
				.address("Seoul")
				.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
				.status(UserStatus.PENDING)
				.lastLoginAt(0L)
				.build();

		fakeUserRepository.save(user1);
		fakeUserRepository.save(user2);
		fakePostRepository.save(
				Post.builder()
						.id(1L)
						.content("helloworld")
						.createdAt(1678530673958L)
						.modifiedAt(0L)
						.writer(user1)
						.build()
		);

	}


	@Test
	void getById는_게시물을_찾아올_수_있다() {
		// given
		// when
		Post result = postService.getById(1);

		// then
		assertThat(result.getContent()).isEqualTo("helloworld");
		assertThat(result.getWriter().getEmail()).isEqualTo("enaenen@naver.com");
	}


	@Test
	void postCreateDto_를_이용하여_게시물을_생성할_수_있다() {
		//given
		PostCreate postCreate = PostCreate.builder()
				.writerId(1)
				.content("foobar")
				.build();

		//when
		Post result = postService.create(postCreate);

		//then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getContent()).isEqualTo("foobar");
		assertThat(result.getCreatedAt()).isEqualTo(0);
	}


	@Test
	void postUpdateDto_를_이용하여_게시물을_수정할_수_있다() {
		//given
		PostUpdate postUpdate = PostUpdate.builder()
				.content("omg")
				.build();

		//when
		postService.update(1, postUpdate);

		//then
		Post post = postService.getById(1);
		assertThat(post.getId()).isNotNull();
		assertThat(post.getContent()).isEqualTo("omg");
		assertThat(post.getModifiedAt()).isEqualTo(0);
	}

}
