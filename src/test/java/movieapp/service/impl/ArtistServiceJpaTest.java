package movieapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.any;


import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import movieapp.dto.ArtistSimple;
import movieapp.entity.Artist;
import movieapp.persistence.ArtistRepository;
import movieapp.service.IArtistService;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ArtistServiceJpaTest {
	
	
	ModelMapper modelMapper;
	
	// layer to mock
	// @Mock : pure mockito
	@MockBean // mock with spring IOC
	ArtistRepository artistRepository;
	
	// layer to test using layer mocked
	// @InjectMocks : pure mockito
	@Autowired
	IArtistService artistService;

	@Test
	void testGetByIdPresent() {
		// given
		int id = 1;		
		String name = "Will Smith";
		LocalDate birthdate = LocalDate.of(1968, 9, 25);
		
		// perfect answer from mock
		Artist artistEntity = new Artist(name, birthdate);
		artistEntity.setId(id);
		given(artistRepository.findById(id)).willReturn(Optional.of(artistEntity));
		
		// when
		Optional<ArtistSimple> optArtistSimpleDto = artistService.getById(id);
		System.out.println(optArtistSimpleDto);
		
		// then
		then(artistRepository).should().findById(eq(id));
		assertTrue(optArtistSimpleDto.isPresent());
		optArtistSimpleDto.ifPresent(
				artistSimpleDto -> assertAll(
						() -> assertEquals(id, artistSimpleDto.getId()),
						() -> assertEquals(name, artistSimpleDto.getName()),
						() -> assertEquals(birthdate, artistSimpleDto.getBirthdate())));
		assertEquals(id, optArtistSimpleDto.get().getId());
	}
	
	@Test
	void testGetByIdAbsent() {
		// given
		int id = 1;		
		//perfect answer
		given(artistRepository.findById(id)).willReturn(Optional.empty());
		// when
		Optional<ArtistSimple> optArtistSimpleDto = artistService.getById(id);
		// then
		// check mock has been call
		then(artistRepository).should().findById(eq(id));
		assertFalse(optArtistSimpleDto.isPresent());
		
	}
	
	@Test
	void testAddArtistSimple() {
		// given
		// DTO to add 
		String name = "Will Smith";
		LocalDate birthdate = LocalDate.of(1968, 9, 25);
		ArtistSimple artistDtoIn = new ArtistSimple(null, name, birthdate);
		
		// Entity response from Mock Repository
		int id = 1;
		Artist artistEntity = new Artist(name, birthdate); 
		artistEntity.setId(id);
		given(artistRepository.save(any())).willReturn(artistEntity);

		// when
		ArtistSimple artistSimpleDtoOut = artistService.add(artistDtoIn);		
		
		// then
		then(artistRepository).should().save(any()); // ask if we invoke the add method at least once 
		assertNotNull(artistSimpleDtoOut.getId()); // from repo response
		assertEquals(id, artistSimpleDtoOut.getId());
		assertEquals(name, artistSimpleDtoOut.getName());
		assertEquals(birthdate, artistSimpleDtoOut.getBirthdate());
		
		
	}

}
