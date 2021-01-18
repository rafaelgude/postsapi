package com.rafaelgude.postsapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rafaelgude.postsapi.model.Post;
import com.rafaelgude.postsapi.repository.PostRepository;

@Service
public class PostService {
	
	@Autowired private PostRepository postRepository;

	public Page<Post> findAll(int page, int linesPerPage, String direction, String orderBy) {
		return postRepository.findAll(PageRequest.of(page, linesPerPage, Direction.valueOf(direction.toUpperCase()), orderBy));
	}
	
	public Optional<Post> findById(Long id) {
		return postRepository.findById(id);
	}
	
	public Post insert(Post post) {
		return postRepository.save(post);
	}
	
	public Post update(Post post) {
		return postRepository.save(post);
	}
	
	public void addUpvote(Post post) {
		post.setUpvotes(post.getUpvotes() + 1);
	}
}
