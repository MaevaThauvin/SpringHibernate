package modelmapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import movieapp.dto.MovieSimple;
import movieapp.entity.Movie;

class ModelMapperTest {
	static ModelMapper modelMapper;
	
	 @BeforeAll
	 static void initModelMapper() {
		 modelMapper = new ModelMapper();
	 }
	 
	
	@Test
	void testEntityToDto() {
		//entity
		Movie movieEntity = new Movie("Blade Runner", 1982, 117);
		movieEntity.setId(1);
		//convert to Dto
		MovieSimple movieDto = modelMapper.map(movieEntity, MovieSimple.class);
		//is it ok
		System.out.println(movieDto);
		assertEquals(movieEntity.getTitle(), movieDto.getTitle());
		assertEquals(movieEntity.getId(), movieDto.getId());
		assertEquals(movieEntity.getYear(), movieDto.getYear());
	}
	
	@Test
	void testDtoToEntity() {
		//DTO
		MovieSimple movieDto = new MovieSimple();
		movieDto.setTitle("Blade Runner");
		movieDto.setYear(1982);
		//convert to Entity
		Movie movieEntity = modelMapper.map(movieDto, Movie.class);
		//is it ok
		System.out.println(movieEntity);
		assertEquals(movieDto.getTitle(), movieEntity.getTitle());
		assertEquals(movieDto.getId(), movieEntity.getId());
		assertEquals(movieDto.getYear(), movieEntity.getYear());
		assertNull(movieEntity.getDirector());
		assertTrue(movieEntity.getActors().isEmpty());
	}
	
	@Test
	void testDtoIntoEntity() {
		//DTO
		MovieSimple movieDto = new MovieSimple();
		movieDto.setId(1);
		movieDto.setTitle("Blade Runner (Director's cut)");
		movieDto.setYear(1970);
		//Entity 
		Movie movieEntity = new Movie("Blade Runner", 1982, 117);
		movieEntity.setId(1);
		//update entity with dto
		modelMapper.map(movieDto,  movieEntity);
		//is it ok
		System.out.println(movieEntity);
		assertEquals(movieDto.getTitle(), movieEntity.getTitle());
		assertEquals(movieDto.getId(), movieEntity.getId());
		assertEquals(movieDto.getYear(), movieEntity.getYear());
		assertNull(movieEntity.getDirector());
		assertEquals(117, movieEntity.getDuration()); // property not in dto
	}
	
	@Test
	void testEntitiesToDtos() {
		//entities
		Stream<Movie> entitySource = Stream.of(
				new Movie("The Man Who Knew Too Much", 1934, null),
				new Movie("The invisible Man", 2020, null),
				new Movie("Wonder Woman 1984", 2020, null),
				new Movie("Men In Black", 1997, null));
		//Convert to Dtos
//		List<MovieSimple> res = entitySource.map(me -> modelMapper.map(me, MovieSimple.class))
//		.collect(Collectors.toList());
		var res = entitySource.map(me -> modelMapper.map(me, MovieSimple.class))
				.collect(Collectors.toCollection(
						// ArrayList::new
						// HashSet::new
						() -> new TreeSet<MovieSimple>(Comparator.comparing(MovieSimple::getTitle))
						));				
		System.out.println(res);
		
	}

}
