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
	void testFindMovieWithActors() {
		int idMovie = movieH.getId();
//		select
//	        movie0_.id as id1_0_0_,
//	        movie0_.id_director as id_direc5_0_0_,
//	        movie0_.duration as duration2_0_0_,
//	        movie0_.title as title3_0_0_,
//	        movie0_.year as year4_0_0_,
//	        artist1_.id as id1_2_1_,
//	        artist1_.birthdate as birthdat2_2_1_,
//	        artist1_.deathdate as deathdat3_2_1_,
//	        artist1_.name as name4_2_1_ 
//	    from
//	        movies movie0_ 
//	    left outer join
//	        stars artist1_ 
//	            on movie0_.id_director=artist1_.id 
//	    where
//	        movie0_.id=?
	
		var movie = movieRepository.getOne(idMovie);
		var actors = movie.getActors();
		
//		select
//	        actors0_.id_movie as id_movie1_1_0_,
//	        actors0_.id_actor as id_actor2_1_0_,
//	        artist1_.id as id1_2_1_,
//	        artist1_.birthdate as birthdat2_2_1_,
//	        artist1_.deathdate as deathdat3_2_1_,
//	        artist1_.name as name4_2_1_ 
//	    from
//	        play actors0_ 
//	    inner join
//	        stars artist1_ 
//	            on actors0_.id_actor=artist1_.id 
//	    where
//	        actors0_.id_movie=?
		System.out.println("Movie "+movie+" with actors: "+ actors);
		assertEquals(2, actors.size());
	}
	
	@Test
	void testFindMovieWithNoActors() {
		int idMovie = movieA.getId();
		var movie = movieRepository.getOne(idMovie);
		var actors = movie.getActors();
		System.out.println("Movie "+movie+" with actors: "+ actors);
		assertEquals(0, actors.size());
	}
	
	@Test
	void testFindByActor() {
		//given
		String name = "Clint Eastwood";
		//when
//		 select
//	        movie0_.id as id1_0_,
//	        movie0_.id_director as id_direc5_0_,
//	        movie0_.duration as duration2_0_,
//	        movie0_.title as title3_0_,
//	        movie0_.year as year4_0_ 
//	    from
//	        movies movie0_ 
//	    left outer join
//	        play actors1_ 
//	            on movie0_.id=actors1_.id_movie 
//	    left outer join
//	        stars artist2_ 
//	            on actors1_.id_actor=artist2_.id 
//	    where
//	        artist2_.name=?
		List<Movie> moviesFound = movieRepository.findByActorsName(name);
		// assert		
		System.out.println(moviesFound);
		
		//TODO: check found 2 movies all in which Clint plays
		assertEquals(2, moviesFound.size());
//		assertAll(
//				moviesFound.stream().map(Movie::getDirector)
//			.map(Artist::getName)
//			.map(n -> () -> assertEquals(name, n, "director name")));
		for (var m: moviesFound) {
			var actors = m.getActors();
			assertTrue(
					actors.stream()
					.anyMatch(a->a.getName().equals(name))
					, "at least one actor named clint Eastwood");		
			}
		
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
