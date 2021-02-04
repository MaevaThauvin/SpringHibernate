package movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

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
		//Creation of Artists
		var clint = new Artist("Clint Eastwood", LocalDate.of(1930, 5, 31));
		var todd = new Artist("Todd Philipps", LocalDate.of(1970, 12, 20));
		var morgan = new Artist("Mogran Freeman", LocalDate.of(1939, 6, 1));
		var bradley = new Artist("Bradley Cooper");
		var zach = new Artist("Zach Galifianakis");
		
		//Persistence of Artists
		Stream.of(clint, todd, morgan, bradley, zach)
			.forEach(entityManager::persist); // insert x5
		
		//Movies with director and actors
		var movieUnforgiven = new Movie("Unforgiven", 1992, 130);
		var movieGranTorino = new Movie("Gran Torino", 2008, 116);
		var movieInvictus = new Movie("Invictus", 2009, null);
		
		List<Movie> moviesClint = List.of(movieUnforgiven, movieGranTorino, movieInvictus);
		moviesClint.forEach(m->m.setDirector(clint));
		movieH = new Movie("The Hangover", 2009, null);
		movieH.setDirector(todd);
		movieA = new Movie("Alien", 1979, null);	
		
		
		
		movieUnforgiven.setActors(List.of(clint, morgan));
		movieGranTorino.setActors(List.of(clint));
		movieInvictus.setActors(List.of(morgan));
		movieH.setActors(List.of(bradley, zach));
		
		//Persistence of movies
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
		assertNotNull(optMovie.get().getDirector(), "director present");
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
		optMovie.ifPresent(m->assertNull(m.getDirector(), "no director"));
	}
	
	@Test
	void testFindByDirector() {
		String name = "Clint Eastwood";
		List<Movie> clintMovies = movieRepository.findByDirectorName(name);
//		assertAll(artists
//				//.map(a->{System.out.println(a);return a;})
//				.filter(a->{System.out.println(a);return true;})
//				.map(a->()->assertTrue(a.getName().toLowerCase().endsWith(name))));
		System.out.println(clintMovies);
		assertEquals(3, clintMovies.size());
		assertAll(
		clintMovies.stream().map(Movie::getDirector)
			.map(Artist::getName)
			.map(n -> () -> assertEquals(name, n, "director name")));
		
	}

}
