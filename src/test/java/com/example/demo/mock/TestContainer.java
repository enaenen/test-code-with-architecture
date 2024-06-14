package com.example.demo.mock;

import com.example.demo.common.service.ClockHolder;
import com.example.demo.common.service.UuidHolder;
import com.example.demo.post.contoller.PostController;
import com.example.demo.post.contoller.PostCreateController;
import com.example.demo.post.contoller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.UserService;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

	public final MailSender mailSender;
	public final UserRepository userRepository;
	public final PostRepository postRepository;
	public final PostService postService;
	public final CertificationService certificationService;
	public final UserController userController;
	public final UserCreateController userCreateController;
	public final PostController postController;
	public final PostCreateController postCreateController;


	@Builder
	public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
		this.userRepository = new FakeUserRepository();
		this.postRepository = new FakePostRepository();
		this.mailSender = new FakeMailSender();
		this.postService = PostServiceImpl.builder()
				.postRepository(this.postRepository)
				.userRepository(this.userRepository)
				.clockHolder(clockHolder)
				.build();
		this.certificationService = new CertificationService(this.mailSender);
		UserService userService = UserServiceImpl.builder()
				.uuidHolder(uuidHolder)
				.clockHolder(clockHolder)
				.userRepository(this.userRepository)
				.certificationService(this.certificationService)
				.build();
		this.userController = UserController.builder()
				.userService(userService)
				.build();
		this.userCreateController = UserCreateController.builder()
				.userService(userService)
				.build();
		this.postController = PostController.builder()
				.postService(this.postService)
				.build();
		this.postCreateController = PostCreateController.builder().postService(this.postService)
				.build();
	}
}

