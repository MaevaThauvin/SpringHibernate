package movieapp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;




import java.awt.print.Printable;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jayway.jsonpath.JsonPath;

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

		//MockMvcResultHandlers
		//MockMvcResultMatchers
	}

}
