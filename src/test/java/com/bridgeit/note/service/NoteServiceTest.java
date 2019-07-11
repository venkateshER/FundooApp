package com.bridgeit.note.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.bridgeit.configuration.ApplicationConfig;
import com.bridgeit.note.dto.NoteDto;
import com.bridgeit.note.model.Note;
import com.bridgeit.note.repository.NoteRepositoryInterface;
import com.bridgeit.user.model.User;
import com.bridgeit.user.repository.UserRepositoryInterface;
import com.bridgeit.utility.Response;
import com.bridgeit.utility.ResponseUtil;
import com.bridgeit.utility.TokenUtil;
import com.bridgeit.utility.Utility;
import com.google.common.base.Optional;
@SpringBootTest
@RunWith(SpringRunner.class)
class NoteServiceTest {
	
	@Before
    public void setUp() throws Exception {

         MockitoAnnotations.initMocks(this);
    }
	
	@Mock
	ModelMapper modelMapper;
	
	@Mock
	Utility util;
	
	@Mock
	Response response1;
	
	@Mock
	TokenUtil token;
	
	@Mock
	NoteRepositoryInterface noteRepository;
	
	@Mock
	Note note;
	
	@Mock
	ApplicationConfig applic;
	
	@Mock
	ResponseUtil respoUtil;
	
	@MockBean
	UserRepositoryInterface userRepository;
//	
	@Mock
	NoteServiceInterface noteServiceInter;

	@InjectMocks
	NoteService noteService;
	
	@Test
	void testCreate() {
		
		NoteDto noteDto=new NoteDto("note1", "abcdefg");
//		Optional<User> user=
		String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2ZW5rYXRlc2giOjI1OH0.ti3cFtAhWXLiWAEzqaJIVcp56b5nEdgkvxW-jolwjzM";
		Response response=new Response(200,"created");
//		when(userRepository.findById((Mockito.any())).thenReturn();
		assertEquals(response.getStatusCode(),noteService.create(noteDto, token).getStatusCode());
		
	}
//
//	@Test
//	void testDelete() {
//		
//	}
//
//	@Test
//	void testUpdate() {
//		
//	}

}
