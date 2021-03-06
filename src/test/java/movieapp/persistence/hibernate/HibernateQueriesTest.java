package movieapp.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import movieapp.dto.ArtistCountMinMax;
import movieapp.dto.DirectorCountMinMax;
import movieapp.dto.MovieStat;
import movieapp.dto.MoviesCountYear;
import movieapp.dto.NameYearTitle;
import movieapp.entity.Artist;
import movieapp.entity.Movie;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) //don't replace DB of ap with H2
class HibernateQueriesTest {
	
	@Autowired
	//TestEntityManager entityManager;
	EntityManager entityManager;

	@Test
	void testJPQL_select_all_as_list() {
		TypedQuery<Movie> query = entityManager.createQuery(
				//"from Movie",
				"select m from Movie m", //JPQL request
				Movie.class); //result will be adapted in 0, 1 ou * object Movie
		// select movie0_.id as id1_0_, movie0_.id_director as id_direc5_0_, movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as year4_0_ from movies movie0_
		List<Movie> movies = query.getResultList(); //execute and conert resultset as list
		System.out.println(movies.size());
	}
	
	@Test
	void testJPQL_select_all_as_stream() {
		entityManager.createQuery(
				"select m from Movie m", //JPQL request
				Movie.class) //result will be adapted in 0, 1 ou * object Movie
		.getResultStream() //execute and conert resultset as Stream
		.limit(10)
		.forEach(System.out::println);
	}
	
	@ParameterizedTest
	@ValueSource(ints = {2019, 2020, 2021})
	void test_select_predicate(int year) {
		System.out.println("Movies from year: "+ year);
		entityManager.createQuery(
				"select m from Movie m where m.year= :year", //JPQL request with parameter (year)
				Movie.class) //result will be adapted in 0, 1 ou * object Movie
		.setParameter("year", year)
		.getResultStream() //execute and conert resultset as Stream
		.forEach(System.out::println);
	}
	
	@ParameterizedTest
	 @CsvSource({ "1934, The Man Who Knew Too Much", 
		 		"1956, The Man Who Knew Too Much"})
	void test_select_where_title_year(int year, String title) {
		System.out.println("Movies with title, year: " );
		entityManager.createQuery(
				"select m from Movie m where m.year= :year and m.title = :title", //JPQL request with parameter (year)
				Movie.class) //result will be adapted in 0, 1 ou * object Movie
		.setParameter("year", year)
		.setParameter("title", title)
		.getResultStream() //execute and conert resultset as Stream
		.forEach(System.out::println);
	}
	
	@Test
	void test_select_where_year_birthdate() {
		entityManager.createQuery(
				"select a from Artist a where extract(year from birthdate) = :year",
				Artist.class
				).setParameter("year", 1930)
				.getResultStream()
				.limit(10)
				.forEach(System.out::println);
		
	}
	
	//artist of age 30
	
	@Test
	void test_select_where_age() {
		entityManager.createQuery(
				"select a from Artist a where extract(year from current_date)-extract(year from a.birthdate) = :year",
				Artist.class
				).setParameter("year", 30)
				.getResultStream()
				.forEach(System.out::println);
	}
	
	@Test
	void test_select_movie_with_director_named() {
		entityManager.createQuery(
				"select m from Movie m join m.director a where a.name = :name",
				Movie.class)
		//select movie0_.id as id1_0_, movie0_.id_director as id_direc5_0_, movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as year4_0_ from movies movie0_ inner join stars artist1_ on movie0_.id_director=artist1_.id where artist1_.name=?
		.setParameter("name", "Clint Eastwood")
		.getResultStream()
		.forEach(System.out::println);
		
	}
	
	@Test
	void test_select_movie_with_actor_named() {
//		select movie0_.id as id1_0_, movie0_.id_director as id_direc5_0_, movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as year4_0_
//		from movies movie0_
//		inner join play actors1_ on movie0_.id=actors1_.id_movie
//		inner join stars artist2_ on actors1_.id_actor=artist2_.id 
//		where artist2_.name=?
		entityManager.createQuery(
				"select m from Movie m join m.actors a where a.name = :name",
				Movie.class) // renvoie des movie car Movie.class
		.setParameter("name", "Clint Eastwood")
		.getResultStream()
		.forEach(System.out::println);
		
	}
	
	@Test
	void test_movie_one_stat() {
		//count(*)
		TypedQuery<Long> query = entityManager.createQuery(
//				"select count(*) from Movie m",
				"select count(m) from Movie m",
				Long.class);
		Long nb_movies = query.getSingleResult();
		System.out.println(nb_movies);
		//min(year)
		int minyear = entityManager.createQuery("select min(m.year) from Movie m", Integer.class).getSingleResult();
		System.out.println("year of first movie:" +minyear);
		//sum(duration) between year1 and year2
		Optional<Integer> min_duration = Optional.ofNullable(entityManager.createQuery(
				"select min(m.duration) from Movie m where m.year between :year1 and :year2",
				Integer.class
				).setParameter("year1",2020 ).setParameter("year2", 2029)
		.getSingleResult());
		System.out.println("Min duration : "+min_duration);
		
	}
	
	@Test
	void test_movie_several_stats() {
		var res = entityManager.createQuery(
				"select count(*), min(m.year), max(m.year) from Movie m",
				Object[].class
				).getSingleResult();
		
		System.out.println("Movie stats: " + Arrays.toString(res) ); // "( " + res.getClass() + " )");
		long nb_movies = (long)res[0];
		int min_year = (int)res[1];
		int max_year = (int)res[2];
		System.out.println("Nb: "+ nb_movies+" ; min: " + min_year+" ; max: "+max_year);
	}
	
	@Test
	void test_movie_deveral_stats_bis() {
		var res = entityManager.createQuery(
				"select count(*), min(m.year), max(m.year) from Movie m",
				Tuple.class
				).getSingleResult();
		
		System.out.println("Movie stats: " + res ); // "( " + res.getClass() + " )");
		long nb_movies = res.get(0, Long.class);
		int min_year = res.get(1, Integer.class);
		int max_year = res.get(2, Integer.class);;
		System.out.println("Nb: "+ nb_movies+" ; min: " + min_year+" ; max: "+max_year);
	}
	
	@Test
	void test_movie_deveral_stats_bis_with_DTO() {
		var res = entityManager.createQuery(
				"select new movieapp.dto.MovieStat(count(*), min(m.year), max(m.year)) from Movie m",
				MovieStat.class
				).getSingleResult();
		
		System.out.println("Movie stats: " + res ); // "( " + res.getClass() + " )");
		long nb_movies = res.getCount();
		int min_year = res.getMin_year();
		int max_year = res.getMax_year();
		System.out.println("Nb: "+ nb_movies+" ; min: " + min_year+" ; max: "+max_year);
	}
	
	@Test
	void test_movie_projection() {
		List<NameYearTitle> res = entityManager.createQuery(
				"select new movieapp.dto.NameYearTitle(a.name, m.year, m.title) from Movie m join m.actors a where a.name like :name order by m.year",
				NameYearTitle.class
				)
		.setParameter("name", "John Wayne")
		.getResultStream()
		.limit(10)
		.collect(Collectors.toList());
		res.forEach(nyt -> System.out.println(nyt.getName()
				+" ; " + nyt.getYear()
				+" ; " + nyt.getTitle()));
	}
	
	//nb movies by year (params: thresholdCount, thresholdYear) order by year/count desc
	@Test
	void test_movie_stats_by_year() {
		int year = 2019;
		long countT = 10;
		var res = entityManager.createQuery(
				"select new movieapp.dto.MoviesCountYear(count(*), m.year) from Movie m where m.year >= :year group by m.year having count(*) >= :countT order by m.year desc",
				//"select count(*), m.year from Movie m where m.year >= :year group by m.year having count(*) >= :countT order by m.year desc",
				//Object[].class
				MoviesCountYear.class
				)
			.setParameter("year", year)
			.setParameter("countT", countT)
			.getResultStream()
			.collect(Collectors.toList());
		 res.forEach( msby -> System.out.println("Year : "+msby.getYear()+" ; Count :" + msby.getCount()));
		//res.forEach( msby -> System.out.println(Arrays.toString(msby)));
		
	}
	
	//stats by director (count, min(year), max(year)) order by count desc
	@Test
	void test_movie_stats_by_director() {
		entityManager.createQuery(
				//"select d, count(*), min(m.year), max(m.year) "
				"select new movieapp.dto.DirectorCountMinMax(d, count(*), min(m.year), max(m.year)) "
				+ "from Movie m join m.director d group by d order by count(*) desc",
				//DirectorCountMinMax.class
				//Object[].class
				DirectorCountMinMax.class
				) 
		.getResultStream()
		.limit(40)
		.collect(Collectors.toList())
		//.forEach(a -> System.out.println(Arrays.toString(a)));
		.forEach(a->System.out.println("Director : "+a.getDirector()+" ; nb Movies : "+ a.getCount()+
				" ; Year min : "+ a.getMinYear()+" ; Year max : "+ a.getMaxYear()));
		
	}
	
	//stats by actor (count, min(year), max(year)) order by count desc
	@Test
	void test_movie_stats_by_actor() {
		entityManager.createQuery("select new movieapp.dto.ActorCountMinMax(a, count(*), min(year), max(year)) from Movie m join m.actors a group by a order by count(*) desc",
				ArtistCountMinMax.class)
		.getResultStream()
		.limit(40)
		.forEach(a -> System.out.println("Actor : "+a.getActor()+" ; Count : "+a.getCountT()+" ; Min Year : "+a.getMin()+" ; Max Year : "+a.getMax()));
	}
	
	// stats by actor (count, min(year), max(year) order by count desc
	@Test
	void test_movies_recent() {
		int deltaYear = 2;
		var res = entityManager.createQuery(
				"select m from Movie m where EXTRACT(YEAR FROM CURRENT_DATE) - m.year <= :deltaYear",
				Movie.class)
				.setParameter("deltaYear", deltaYear)
				.getResultList();
		System.out.println(res);
	}
	

}
