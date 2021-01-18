package com.rafaelgude.postsapi.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.rafaelgude.postsapi.model.Post;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

}
