package movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import movieapp.entity.Artist;

@DataJpaTest
class ArtistRepositoryTest {
	
	@Autowired
	ArtistRepository artistRepository;
	
	@Autowired
	EntityManager entityManager;
	
	List<Artist> artists;
	List<Integer> ids;
	
	@BeforeEach
	void initData() {
		artists = List.of(
				new Artist("Steve McQueen", LocalDate.of(1930, 3, 24), LocalDate.of(1980, 11, 7)),
				new Artist("Steve McQueen", LocalDate.of(1969, 10, 9)),
				new Artist("Alfred Hitchcock"));
		artists.forEach(entityManager::persist);
		entityManager.flush();
		ids = artists.stream()
				//.map(a -> a.getId())
				.map(Artist::getId)
				.collect(Collectors.toList());
	}

	@Test
	void testFindAll() {
		//Read data from database (via spring jpa repository)
		var artistsFound = artistRepository.findAll();
		System.out.println(artists);
		System.out.println(ids);
		System.out.println(artistsFound);
		//TODO assert order first list and check if second list contain element of the first one
	}
	
	@Test 
	void testFindById() {
		var id = ids.get(0);
		var optArtistsFound = artistRepository.findById(id);
		System.out.println(artists);
		System.out.println(ids);
		System.out.println(optArtistsFound);
		assertTrue(optArtistsFound.isPresent());
		optArtistsFound.ifPresent(a -> assertEquals(id, a.getId(), "id artist"));
	}
	
	@Test 
	void testFindAllById() {
		var idSelection = List.of(ids.get(0), ids.get(2));
		var optArtistsFound = artistRepository.findAllById(idSelection);
		System.out.println("Artists "+artists);
		System.out.println("Ids "+ ids);
		System.out.println("Ids asked "+idSelection);
		System.out.println("Artists found :" +optArtistsFound);
	}
	
	@Test
	void testGetOne() {
		var id = ids.get(0);
		System.out.println(artists);
		System.out.println(ids);
		var artistsFound = artistRepository.getOne(id);
		System.out.println(artistsFound);
		//TODO assert
	}

}
