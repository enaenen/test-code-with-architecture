package com.example.demo.post.contoller;

import com.example.demo.post.contoller.port.PostService;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.contoller.response.PostResponse;
import com.example.demo.post.service.PostServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시물(posts)")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Builder
public class PostCreateController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<PostResponse> createPost(@RequestBody PostCreate postCreate) {
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(PostResponse.from(postService.create(postCreate)));
	}
}