package movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import movieapp.entity.ImageColor;
import movieapp.entity.Movie;

@DataJpaTest
class MovieCollectionTest {
	
	@Autowired
	MovieRepository movieRepository;

	@Test
	void test() {
		Movie movieK = new Movie("Kingsman: The Secret Servic e", 2014, 129);
		movieK.setGenres((List.of("Action", "Adventure", "Comedy")));
		movieRepository.save(movieK);
		movieK.setImageColor(ImageColor.COLOR);
		Movie movieX = new Movie("xXx", 202, 124);
		movieX.setGenres((List.of("Action", "Adventure", "Thriller")));
		movieX.setImageColor(ImageColor.BLACKANDWHITE);
		movieRepository.save(movieX);
		movieRepository.flush();
	}

}
