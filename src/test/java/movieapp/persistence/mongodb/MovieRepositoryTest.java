package movieapp.persistence.mongodb;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import movieapp.persistence.document.Artist;
import movieapp.persistence.document.Movie;

@DataMongoTest
class MovieRepositoryTest {
	
	@Autowired
	MovieRepository movieRepository;

	@Test
	void testCreateBuilder() {
		var director = Artist.of("Martin Campbell");
		var movie = Movie.builder()
				.title("Casino Royal")
				.year(2006)
				.director(director)
				.build();
		System.out.println(movie);
		// save in repo mongodb
		movieRepository.save(movie);
		movieRepository.findAll().forEach(System.out::println);
	}

}
