package movieapp.persistence.mongodb;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import movieapp.persistence.document.Artist;
import movieapp.persistence.document.Movie;

@DataMongoTest
class MovieRepositoryTest {
	
	@Autowired
	MovieRepository movieRepository;
	
	int i =0;
	static int year=1977;

	@Test
	void testCreateBuilder() {
		var director = Artist.of("Martin Campbell");
		var movie = Movie.builder()
				.title("Casino Royal")
				.year(2006)
				.director(director)
				.build();
		// System.out.println(movie);
		// save in repo mongodb
		movieRepository.save(movie);
	}
	
	@Test
	void testFindAll() {
		movieRepository.findAll().forEach(System.out::println);
	}
	
	@ParameterizedTest
	@ValueSource(ints={0,1, 19})
	void testFindAllPagination(int numPage) {
		int size= 5;
		var movie = movieRepository.findAll(PageRequest.of(numPage, size))
				.map(Movie::getTitle)
				.toList();
		System.out.println(movie);
	}
	
	static IntStream numberMovie(){
		// return Stream.of(1, 2, 3, 4, 5, 6, 7);
		return IntStream.rangeClosed(1, 100);
		
	}
	
	@Test //After each
	void testDeleteAll() {
		movieRepository.deleteAll();
	}
	
	
	@ParameterizedTest
	@MethodSource("numberMovie")
	void testCeateFranchise(int number) {
		var movie = Movie.of("Star Wars "+ number, year);
		year++;
		movieRepository.save(movie);
		System.out.println("After saving: "+movie);
	}
	
	@Test
	void testFindByDirectorName() {
		String name = "Martin Campbell";
		var movies = movieRepository.findByDirectorName(name)
				.collect(Collectors.toList());
		System.out.println(movies);
		
	}
	
	@ParameterizedTest
	@ValueSource(ints= {0,1,19})
	void TestFindByYearGreaterThanOrderByYear(int numPage) {
		int yearMin = 2000;
		int size = 5;
		var movies = movieRepository.findByYearGreaterThanOrderByYear(yearMin, PageRequest.of(numPage, size))
				.map(Movie::getYear)
				.toList();
		System.out.println(movies);
				
		
	}
}
