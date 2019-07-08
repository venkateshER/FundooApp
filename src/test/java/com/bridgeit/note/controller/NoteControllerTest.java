package com.bridgeit.note.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bridgeit.note.dto.NoteDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class NoteControllerTest {

	private MockMvc mvc;
	
	 @Autowired 
	 private WebApplicationContext wac;
	
	 @Before
	    public void setup() {
	        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	    }
	 
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	@Test
	public void testCreate() {
		try {
			mvc.perform( MockMvcRequestBuilders
				      .post("/note/create")
				      .content(asJsonString(new NoteDto("note1","abcde"))).header("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2ZW5rYXRlc2giOjI0NH0.Aj3zuX-ImwRH4yfgUIcxPf7rm6lLHh3AMcyfZIUmfek")
				      .contentType(MediaType.APPLICATION_JSON)
				      .accept(MediaType.APPLICATION_JSON)).andDo(print())
				      .andExpect(status().isOk()).andReturn();
		} catch (Exception e) {
			e.printStackTrace();
		}
			}
		
	@Test
	public void testDelete() {
		
		try {
			mvc.perform( MockMvcRequestBuilders
				      .delete("/note/delete").param("id", "585").header("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2ZW5rYXRlc2giOjI1OH0.ti3cFtAhWXLiWAEzqaJIVcp56b5nEdgkvxW-jolwjzM")
//				      .content(asJsonString(new NoteDto("note1","abcde"))).header("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2ZW5rYXRlc2giOjI0NH0.Aj3zuX-ImwRH4yfgUIcxPf7rm6lLHh3AMcyfZIUmfek")
				      .contentType(MediaType.APPLICATION_JSON)
				      .accept(MediaType.APPLICATION_JSON)).andDo(print())
				      .andExpect(status().isOk()).andReturn();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Test
	public void testUpdate() {
		
		try {
			mvc.perform( MockMvcRequestBuilders
				      .put("/note/update")
				      .content(asJsonString(new NoteDto("note3","abcde"))).param("id", "584").header("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2ZW5rYXRlc2giOjI1OH0.ti3cFtAhWXLiWAEzqaJIVcp56b5nEdgkvxW-jolwjzM")
				      .contentType(MediaType.APPLICATION_JSON)
				      .accept(MediaType.APPLICATION_JSON)).andDo(print())
				      .andExpect(status().isOk()).andReturn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testgetAllNotes() {
		
		try {
			mvc.perform( MockMvcRequestBuilders
				      .get("/note/getAllNotes").header("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2ZW5rYXRlc2giOjI1OH0.ti3cFtAhWXLiWAEzqaJIVcp56b5nEdgkvxW-jolwjzM")
//				      .content(asJsonString(new NoteDto("note3","abcde"))).param("id", "584").header("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2ZW5rYXRlc2giOjI1OH0.ti3cFtAhWXLiWAEzqaJIVcp56b5nEdgkvxW-jolwjzM")
				      .contentType(MediaType.APPLICATION_JSON)
				      .accept(MediaType.APPLICATION_JSON)).andDo(print())
				      .andExpect(status().isOk()).andReturn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
