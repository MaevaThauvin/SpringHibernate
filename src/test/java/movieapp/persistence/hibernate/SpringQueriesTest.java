package movieapp.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import movieapp.persistence.ArtistRepository;
import movieapp.persistence.MovieRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class SpringQueriesTest {
	
	@Autowired
	ArtistRepository artistRepository;
	
	@Autowired
	MovieRepository movieRepository;

	@Test
	void test_artists_by_birthdate_year() {
		int year = 1930;
		artistRepository.findByBirthdateYear(year).limit(10).forEach(System.out::println);
	}
	
	
	@ParameterizedTest
//	@CsvSource({"2010, 2019,",
//			"2030, 2039"})
	@MethodSource("rangeYearSource")
	void test_total_duration_range_year(int yearMin, int yearMax) {
//		int yearMin = 2010;
//		int yearMax = 2019;
		var res = movieRepository.totalDuration(yearMin, yearMax);
		System.out.println(res);
	}
	
	@ParameterizedTest
//	@CsvSource({"2010, 2019,",
//			"2030, 2039"})
	@MethodSource("rangeYearSource")
	void test_average_duration_range_year(int yearMin, int yearMax) {
//		int yearMin = 2010;
//		int yearMax = 2019;
		var res = movieRepository.averageDuration(yearMin, yearMax);
		System.out.println(res);
	}
	
	static Stream<Arguments> rangeYearSource() {
		return Stream.of(Arguments.arguments(2010, 2019),
		Arguments.arguments(2030, 2039));
	}
	
	@Test 
	void test_statistics() {
		var stats = movieRepository.statistics();
		long nb_movies = stats.getCount();
		int min_year = stats.getMin_year();
		int max_year = stats.getMax_year();
		System.out.println("Nb: "+ nb_movies+" ; min: " + min_year+" ; max: "+max_year);
	}
	
//	@ParameterizedTest
//	@ValueSource(strings = {
//			"Clint Eastwood", 
//			"Tarantino"})
//	void test_filmography(String name) {
//		//String name = "Clint Eastwood";
//		artistRepository.filmographyActor(name).forEach(
//				nyt -> System.out.println(nyt.getName()
//				+" ; " + nyt.getYear()
//				+" ; " + nyt.getTitle()
//				+ " ; " + nyt.getClass()));
//	}
	
	@Test
	void test_countMoviesByYear() {
		Long countT = 10L;
		Integer year = 2019;
		artistRepository.countMoviesByYear(countT, year).forEach( msby -> System.out.println("Year : "+msby.getYear()+" ; Count :" + msby.getCountT()));
	}
	
	@Test
	void test_directorCountMinMax() {
		artistRepository.directorCountMinMax().limit(40).forEach(a->System.out.println("Director : "+a.getDirector()+" ; nb Movies : "+ a.getCountT()+
				" ; Year min : "+ a.getMinYear()+" ; Year max : "+ a.getMaxYear()));
	}
	
//	@Test
//	void test_actorCountMinMax() {
//		artistRepository.actorCountMinMax().forEach(a -> System.out.println("Actor : "+a.getActor()+" ; Count : "+a.getCountT()+" ; Min Year : "+a.getMin()+" ; Max Year : "+a.getMax()));
//		}
	

//	@Test
//	void test_artistCountMinMax() {
//		artistRepository.artistCountMinMax().forEach(a -> System.out.println("Actor id : "+a.getActor().getId()+" name: "+a.getActor().getName()+" ; Count : "+a.getCountT()+" ; Min Year : "+a.getMin()+" ; Max Year : "+a.getMax()));
//		}
	
}
