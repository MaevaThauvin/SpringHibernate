package movieapp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import movieapp.dto.ArtistSimple;
import movieapp.service.IArtistService;

@WebMvcTest(ArtistController.class) // controller to test with MockMvc client
class ArtistControllerTest {
	
	private final static String BASE_URI = "/api/artists";
	
	@Autowired
	MockMvc mockMvc; // client to perform http request to controller, equivalent to Postman
	
	@MockBean
	IArtistService artistService; // service layer mocked

	@Test
	void testGetIdAbsent() throws Exception {
		// 1. given
		int id = 0 ;
		given(artistService.getById(id))
			.willReturn(Optional.empty());
		// 2. when/then
		mockMvc.perform(
				MockMvcRequestBuilders.get(BASE_URI + "/"+id)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").doesNotExist());
		// check mock service has been called
		then(artistService).should().getById(eq(id));
		//MockMvcResultHandlers
		//MockMvcResultMatchers
	}
	
	@Test
	void testGetIdPresent() throws Exception {
		// 1. given
		int id = 1 ;
		String name = "Will Smith";
		LocalDate birthdate = LocalDate.of(1968,  9,  25);
		ArtistSimple artistSimpleDto = new ArtistSimple(id, name, birthdate);
		given(artistService.getById(id))
			.willReturn(Optional.of(artistSimpleDto));
		// 2. when/then
		mockMvc.perform(
					MockMvcRequestBuilders.get(BASE_URI + "/"+id) //provoke Get request , build GET HTTP request
					.accept(MediaType.APPLICATION_JSON)) // add header to my request saying I want JSON
				.andDo(print()) // intercept request to print what happend
				.andExpect(status().isOk()) // andExpect >> assertion / status : check status of the request
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)) // ask if content type is Json
				.andExpect(jsonPath("$.id").exists()) // $ (objet que l'on reçoit / $.id get the id attribute from the object used
				.andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.name").value(name))
				.andExpect(jsonPath("$.birthdate").value(birthdate.toString()));
		// check mock service has been called
		then(artistService).should().getById(eq(id));

		//MockMvcResultHandlers
		//MockMvcResultMatchers
	}
	
	@Test
	void testPostAddArtistSimple() throws Exception {
		// 1. given
		String name = "Will Smith";
		LocalDate birthdate = LocalDate.of(1968,  9,  25);
		String artistJsonIn =JsonProvider.artistJson(name, birthdate);
		// perfect response from mock service
		int id = 1;
		given(artistService.add(any())).willReturn(new ArtistSimple(id, name, birthdate));
		
		
		// 2. when/then
		mockMvc.perform(post(BASE_URI) //provoke Get request , build GET HTTP request
				.contentType(MediaType.APPLICATION_JSON)
				.content(artistJsonIn)	
				.accept(MediaType.APPLICATION_JSON)) // add header to my request saying I want JSON
				.andDo(print()) // intercept request to print what happend
				.andExpect(status().isOk()) // andExpect >> assertion / status : check status of the request
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)) // ask if content type is Json
				.andExpect(jsonPath("$.id").exists()) // $ (objet que l'on reçoit / $.id get the id attribute from the object used
				.andExpect(jsonPath("$.id").value(id ))
				.andExpect(jsonPath("$.name").value(name))
				.andExpect(jsonPath("$.birthdate").value(birthdate.toString()));
		
		// check mock service has been called
		then(artistService).should().add(any());
	}
	
	@Test
	void testGetGetByName() throws Exception {
		// 1. given
		String name = "Steve McQueen";
		int id =1;
		
		given(artistService.getByName(eq(name))).willReturn(Set.of(
				new ArtistSimple(id, name, LocalDate.of(1930, 03, 24)),
				new ArtistSimple(id, name, LocalDate.of(1930, 03, 24))
				));
		

		// 2. when / then
		mockMvc.perform(get(BASE_URI+"/byName").param("name", name))
			.andDo(print())
			.andExpect(status().isOk()) // andExpect >> assertion / status : check status of the request
			.andExpect(content().contentType(MediaType.APPLICATION_JSON)) 
			//.andExpect(jsonPath("$.name").value(name))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$", Matchers.hasSize(2)))
			.andExpect(jsonPath("$[*].name", Matchers.everyItem(Matchers.is(name))));
		
		then(artistService).should().getByName(eq(name));
	}

}
