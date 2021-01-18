package com.rafaelgude.postsapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import com.rafaelgude.postsapi.model.Post;
import com.rafaelgude.postsapi.repository.PostRepository;
import com.rafaelgude.postsapi.service.PostService;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

	@Mock
	private PostRepository postRepository;
	
	@InjectMocks
	private PostService postService;
	
	@Test
	public void shouldReturnAllPosts() {
		var posts = new PageImpl<Post>(List.of(new Post()));
		var pageRequest = PageRequest.of(0, 24, Direction.valueOf("ASC"), "description");
		
		when(postRepository.findAll(pageRequest)).thenReturn(posts);
		
		var expectedPosts = postService.findAll(0, 24, "ASC", "description");
		
		assertEquals(posts, expectedPosts);
		verify(postRepository).findAll(pageRequest);
	}
	
	@Test
	public void whenFindById_thenReturnOptionalPost() {
		var post = new Post(1L, "Unit Test");
		
		when(postRepository.findById(1L)).thenReturn(Optional.of(post));
		
		var found = postService.findById(1L);
		assertThat(found).isNotEmpty();
		assertEquals(found.get(), post);
	}
	
	@Test
	public void whenFindByInvalidId_thenReturnEmptyOptional() {
		when(postRepository.findById(0L)).thenReturn(Optional.empty());
		assertEquals(postService.findById(0L), Optional.empty());
	}
	
	@Test
	public void whenInsertPost_thenPersistData() {
		var post = new Post();
		post.setDescription("Unit Test");
		
		when(postRepository.save(post)).thenReturn(post);
		
		var inserted = postService.insert(post);
		
		assertThat(inserted).isNotNull();
		assertEquals(inserted, post);
		verify(postRepository).save(post);
	}
	
	@Test
	public void whenUpdatePost_thenPersistNewData() {
		var post = new Post(1L, "Unit Test");
		
		when(postRepository.save(post)).thenReturn(post);
		var updated = postService.update(post);
		
		assertThat(updated).isNotNull();
		verify(postRepository).save(post);
	}
	
	@Test
	public void shouldAddUpvote() {
		var post = new Post(1L, "Unit Test");
		var oldUpvotes = post.getUpvotes();
		
		postService.addUpvote(post);
		assertEquals(oldUpvotes + 1, post.getUpvotes());
	}
	
	
}
