package com.rafaelgude.postsapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rafaelgude.postsapi.exception.ObjectNotFoundException;
import com.rafaelgude.postsapi.model.Post;
import com.rafaelgude.postsapi.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {
	
	@Autowired PostService postService;

	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Page<Post> findAll(@RequestParam(value="page", defaultValue="0") int page, 
					          @RequestParam(value="linesPerPage", defaultValue="24") int linesPerPage, 
					          @RequestParam(value="direction", defaultValue="ASC") String direction,
					          @RequestParam(value="orderBy", defaultValue="description") String orderBy) {
		return postService.findAll(page, linesPerPage, direction, orderBy);
	}
	
	@PostMapping
	public ResponseEntity<?> insert(@RequestBody Post post) {
		postService.insert(post);
		return ResponseEntity.created(null).build();
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> upvote(@PathVariable("id") long id) {
		postService.findById(id).ifPresentOrElse(post -> {
			postService.addUpvote(post);
			postService.update(post);
		}, () -> {
			throw new ObjectNotFoundException("Post não encontrado.");
		});
		
		return ResponseEntity.noContent().build();
	}
	
}
