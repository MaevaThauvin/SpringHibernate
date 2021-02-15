package movieapp.persistence.document;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MovieTest {

	@Test
	void testCreateDefault() {
		var movie = new Movie();
		movie.setTitle("Casino Royal");
		movie.setYear(2006);
		movie.setDuration(144);
		System.out.println(movie);
	}
	
	@Test
	void testCreateAllArgs() {
		var movie = new Movie("a", "Casino Royal", 2006, 144, null, null);
	}
	
	@Test
	void testCreateRequriedArgConstructor() {
		var movie = Movie.of("Casino Royal", 2006);
	}
	
	
	@Test
	void testCreateBuilder() {
		var director = Artist.of("Martin Campbell");
		var movie = Movie.builder()
				.title("Casino Royal")
				.year(2006)
				.director(director)
				.build();
		System.out.println(movie);
	}
	
}
