package com.example.demo.mock;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FakePostRepository implements PostRepository {

	private Long autoGeneratedId = 0L;
	private final List<Post> data = new ArrayList<>();

	@Override
	public Optional<Post> findById(long id) {
		return data.stream().filter(item -> item.getId().equals(id)).findAny();
	}

	@Override
	public Post save(Post post) {
		if (post.getId() == null || post.getId() == 0) {
			Post newPost = Post.builder()
					.id(++autoGeneratedId)
					.content(post.getContent())
					.createdAt(post.getCreatedAt())
					.modifiedAt(post.getModifiedAt())
					.writer(post.getWriter())
					.build();
			data.add(post);
			return newPost;
		} else {
			data.removeIf(item -> Objects.equals(item.getId(), post.getId()));
			data.add(post);
			return post;
		}
	}
}
