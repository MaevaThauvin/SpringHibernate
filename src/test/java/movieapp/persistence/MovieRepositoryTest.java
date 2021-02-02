package movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import movieapp.entity.Movie;

@DataJpaTest
class MovieRepositoryTest {

	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Test
	void testFindByTitle() {
		//given
		// 1 - a title of movies to read in the test
		String title = "The man who knew too Much";
		// 2 - writing data in database via the entitymanager
		var moviesDataBase = List.of(
				new Movie(title, 1934, null),
				new Movie(title, 1956, null),
				new Movie("The Man Whoo Knew Too Little", 1997, null));
		//ask to each movie of the list movies to apply persist method from entityManager
		moviesDataBase.forEach(entityManager::persist); // persist method >> SQL : insert for each movie
		entityManager.flush();
		//when : read from the repository
		var moviesFound = movieRepository.findByTitle(title);
		//then
		assertEquals(2, moviesFound.size());
		assertAll(moviesFound.stream().map(
				m -> () -> assertEquals(title, m.getTitle(), "title")
				));
	}
	
	@Test
	void testFindByTitleContainingIgnoreCase() {
		//given
		// 1 - a title of movies to read in the test
		String titlePart = "man";
		// 2 - writing data in database via the entitymanager
		var moviesDataBase = List.of(
				new Movie("The Man Who Knew Too Much", 1934, null),
				new Movie("The invisible Man", 2020, null),
				new Movie("Wonder Woman 1984", 2020, null),
				new Movie("Men In Black", 1997, null));
		//ask to each movie of the list movies to apply persist method from entityManager
		moviesDataBase.forEach(entityManager::persist); // persist method >> SQL : insert for each movie
		entityManager.flush();
		//when : read from the repository
		var moviesFound = movieRepository.findByTitleContainingIgnoreCase(titlePart);
		//then
		assertEquals(3, moviesFound.size());
		assertAll(moviesFound.stream().map(
				m -> () -> assertTrue(
						m.getTitle().toLowerCase().contains(titlePart),
						titlePart+" not in title")
				));
	}
	
	@Test
	void testFindByYearGreaterThan() {
		//given
		// 1 - a year of movie to read in the test
		int year = 2000;
		// 2 - writing data in database via the entity manager
		var moviesDataBase = List.of(
				new Movie("The Man Who Knew Too Much", 1934, null),
				new Movie("The invisible Man", 2001, null),
				new Movie("Wonder Woman 1984", 2100, null),
				new Movie("Men In Black", 2020, null));
		//ask to each movie of the list movies to apply persist method from entityManager
		moviesDataBase.forEach(entityManager::persist); // persist method >> SQL : insert for each movie
		entityManager.flush();
		//when : read from the repo
		var moviesFound = movieRepository.findByYearGreaterThanEqual(year);
		//then
		assertEquals(3, moviesFound.size());
		assertAll(moviesFound.stream().map(
					m -> () -> assertTrue(m.getYear() >= year)));
		
	}
	
	@Test
	void testFindByYearBetween() {
		//given
		// 1 - a year of movie to read in the test
		int min = 2000;
		int max = 2009;
		// 2 - writing data in database via the entity manager
		var moviesDataBase = List.of(
				new Movie("The Man Who Knew Too Much", 1934, null),
				new Movie("The invisible Man", 2000, null),
				new Movie("Wonder Woman 1984", 2009, null),
				new Movie("Men In Black", 2020, null),
				new Movie("Men In Black", 2004, null));
		//ask to each movie of the list movies to apply persist method from entityManager
		moviesDataBase.forEach(entityManager::persist); // persist method >> SQL : insert for each movie
		entityManager.flush();
		//when : read from the repo
		var moviesFound = movieRepository.findByYearBetween(min, max);
		//then
		assertEquals(3, moviesFound.size());
		assertAll(moviesFound.stream().map(
					m -> () -> assertTrue(m.getYear() >= min)));
		assertAll(moviesFound.stream().map(
				m -> () -> assertTrue(m.getYear() <= max)));
		
	}
	
	@Test
	void testFindByTitleIgnoreCaseAndYearEquals() {
		//given
		// 1 - a year of movie to read in the test
		String title = "The Lion King";
		int year = 1994;
		// 2 - writing data in database via the entity manager
		var moviesDataBase = List.of(
				new Movie("The Man Who Knew Too Much", 1934, null),
				new Movie("The invisible Man", 2000, null),
				new Movie("THE LION KING", 1994, null),
				new Movie("The Lion King", 1994, null),
				new Movie("The Lion King ", 2020, null));
		//ask to each movie of the list movies to apply persist method from entityManager
		moviesDataBase.forEach(entityManager::persist); // persist method >> SQL : insert for each movie
		entityManager.flush();
		//when : read from the repo
		var moviesFound = movieRepository.findByTitleIgnoreCaseAndYearEquals(title, year);
		//then
		assertEquals(2, moviesFound.size());
		assertAll(moviesFound.stream().map(
					m -> () -> assertTrue(m.getTitle().toLowerCase().equals(title.toLowerCase()))));
		assertAll(moviesFound.stream().map(
				m -> () -> assertTrue(m.getYear().equals(year))));
		
	}
	
	@Test
	void testFindByDurationIsNull() {
		// 1- given null duration
		// 2 - writing data in database via the entity manager
		var moviesDataBase = List.of(
				new Movie("The Man Who Knew Too Much", 1934, null),
				new Movie("The invisible Man", 2000, 178),
				new Movie("THE LION KING", 1994, 60),
				new Movie("The Lion King", 1994, null),
				new Movie("The Lion King ", 2020, null));
		//ask to each movie of the list movies to apply persist method from entityManager
		moviesDataBase.forEach(entityManager::persist); // persist method >> SQL : insert for each movie
		entityManager.flush();
		//when : read from the repo
		var moviesFound = movieRepository.findByDurationIsNull();
		//then
		assertEquals(3, moviesFound.size());
		assertAll(moviesFound.stream().map(
					m -> () -> assertTrue(m.getDuration()==null)));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Z", 
			"Blade Runner", 
			"Night of the Day of the Dawn of the Son of the Bride of the Return of the Revenge of the Terror of the Attack of the Evil Mutant Hellbound Flesh Eating Crawling Alien Zombified Subhumanoid Living Dead, Part 5"})
	void testSaveTitle(String title) {
		// given
		int year = 1982;
		int duration = 173;
		// when + then
		saveAssertMovie(title, year, duration);
	}
	

	@Test
	void testSaveTitleEmptyNOK() {
		String title = null;
		int year = 1982;
		int duration = 173;
		assertThrows(DataIntegrityViolationException.class, 
				() -> saveAssertMovie(title, year, duration));
	}
	
	
	@ParameterizedTest
	@ValueSource(ints = { 1888, 1982, Integer.MAX_VALUE })
	void testSaveYear(int year) {
		// given
		String title = "Blade Runner";
		int duration = 173;
		// when + then
		saveAssertMovie(title, year, duration);
	}
	
	@Test
	void testSaveYearNullNOK() {
		String title = "Blade runner";
		Integer year = null;
		int duration = 173;
		assertThrows(DataIntegrityViolationException.class, 
				() -> saveAssertMovie(title, year, duration));
	}
	
	@ParameterizedTest
	@ValueSource(ints = { 1888, 1982, Integer.MAX_VALUE })
	@NullSource
	void testSaveDuration(Integer duration) {
		// given
		String title = "Blade Runner";
		int year = 1982;
		// when + then
		saveAssertMovie(title, year, duration);
	}
	

	private void saveAssertMovie(String title, Integer year, Integer duration) {
		Movie movie = new Movie(title, year, duration);
		// when
		movieRepository.save(movie);
		// then
		var idMovie = movie.getId();
		assertNotNull(idMovie, "id generated by database");
//		var optMovieFromRepo = movieRepository.findById(idMovie);
//		System.out.println(optMovieFromRepo);
//		var movieFromRepo = optMovieFromRepo.get();
//		assertEquals(movie, movieFromRepo);
		// NB : following test only checks that object read is the same as object written (cache)
		movieRepository.findById(idMovie)
			.ifPresent(m -> assertEquals(movie, m));
		// debug purpose only
		//System.out.println(movie);
	}

}
