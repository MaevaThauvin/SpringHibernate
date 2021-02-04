package movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import movieapp.entity.Artist;
import movieapp.entity.Movie;

@DataJpaTest
class MovieRepositoryDirectorFindTest {
	
	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	MovieRepository movieRepository;
	
	Movie movieH, movieA;
	
	
	@BeforeEach
	void initData() {
		var clint = new Artist("Client Eastwood", LocalDate.of(1930, 5, 31));
		entityManager.persist(clint);
		var todd = new Artist("Todd Philipps", LocalDate.of(1970, 12, 20));
		entityManager.persist(todd);
		
		List<Movie> moviesClint = List.of( 
				new Movie("Invictus", 2009, null),
				new Movie("Unforgiven", 1992, 130),
				new Movie("Gran Torino", 2008, 116));
		moviesClint.forEach(m->m.setDirector(clint));
		movieH = new Movie("The Hangover", 2009, null);
		movieH.setDirector(todd);
		movieA = new Movie("Alien", 1979, null);	
		moviesClint.forEach(entityManager::persist);//insert x3
		entityManager.persist(movieH); //insert
		entityManager.persist(movieA);  //insert
		entityManager.flush(); //synchro
		entityManager.clear(); //vider le cache
		
	}
		

	@Test
	void testFindMovieWithExistingDirector() {
		int idMovie = movieH.getId();
		var optMovie = movieRepository.findById(idMovie);
		assertTrue(optMovie.isPresent());
		assertNotNull(optMovie.get().getDirector());
		//Autre forme équivalente
//		assertTrue(optMovie.isPresent());
//		optMovie.ifPresent(m->assertNotNull(m.getDirector()));
	}
	
	@Test
	void testFindMovieWitNoDirector() {
		int idMovie = movieA.getId();
		var optMovie = movieRepository.findById(idMovie);
//		assertTrue(optMovie.isPresent());
//		assertNull(optMovie.get().getDirector());
		//Autre forme équivalente
		assertTrue(optMovie.isPresent());
		optMovie.ifPresent(m->assertNull(m.getDirector()));
	}

}
