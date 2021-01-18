package com.rafaelgude.postsapi;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class PostControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeAll
	public void setup() throws JSONException, Exception {
		mockMvc.perform(post("/posts")
						.content(new JSONObject().put("description", "First Post").put("upvotes", 0).toString())
						.contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isCreated());
	}
	
	@Test
	public void testGetAllPosts() throws Exception {
		mockMvc.perform(get("/posts"))
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.content", hasSize(1)));
	}
	
	@Test
	public void testCreatePost() throws JSONException, Exception {
		mockMvc.perform(post("/posts")
						.content(new JSONObject().put("description", "Testing Controller").toString())
						.contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isCreated());
		
		mockMvc.perform(get("/posts"))
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.content", hasSize(2)));
	}
	
	@Test
	public void givenNoDescription_whenCreatePost_whenReturnBadRequest() throws JSONException, Exception {
		mockMvc.perform(post("/posts")
						.content(new JSONObject().put("upvotes", 10).toString())
						.contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testAddUpvote() throws Exception {
		mockMvc.perform(patch("/posts/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isNoContent());
		
		mockMvc.perform(get("/posts"))
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.content[0].upvotes", is(1)));
	}
	
	@Test
	public void givenInvalidId_whenAddUpvote_thenReturnNotFound() throws Exception {
		mockMvc.perform(patch("/posts/{id}", 999L)
						.contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isNotFound());
	}
	
}
