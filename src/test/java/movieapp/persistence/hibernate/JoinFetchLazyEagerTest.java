package movieapp.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import movieapp.entity.Movie;
import movieapp.persistence.MovieRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JoinFetchLazyEagerTest {
	
	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	EntityManager entityManager;

	@Test
	void testReadMovieWithAssociatedObjects() { // Eager : Default on ManyToOne
		// Fetch on association director : Eager
		
		// SCENARIO 1 : Fetch Eager
//		select movie0_.id as id1_0_0_, movie0_.id_director as id_direc5_0_0_, 
//			movie0_.duration as duration2_0_0_, movie0_.title as title3_0_0_, 
//			movie0_.year as year4_0_0_, artist1_.id as id1_2_1_, artist1_.birthdate as birthdat2_2_1_, 
//			artist1_.deathdate as deathdat3_2_1_, artist1_.name as name4_2_1_ 
//		from movies movie0_ 
//		left outer join stars artist1_ on movie0_.id_director=artist1_.id 
//		where movie0_.id=?
		
		//SCENARIO 2
//		select movie0_.id as id1_0_0_, movie0_.id_director as id_direc5_0_0_,
//		movie0_.duration as duration2_0_0_, movie0_.title as title3_0_0_,
//		movie0_.year as year4_0_0_ 
//		from movies movie0_ 
//		where movie0_.id=?
				
		var movie = movieRepository.findById(88247).get();
		System.out.println(movie);
		// SCENARIO 1 :  director is already fetched: no extra query
		//SCENARIO 2 : 
//		select artist0_.id as id1_2_0_, artist0_.birthdate as birthdat2_2_0_,
//		artist0_.deathdate as deathdat3_2_0_, artist0_.name as name4_2_0_ 
//		from stars artist0_ 
//		where artist0_.id=?

		System.out.println(movie.getDirector());
	}
	
	@Test
	void testReadMovieWithAssociatedObjects2() { // Lazy : implemented in ManyToOne Movie entity > Fetch: Lazy
		// Fetch on association director : Eager
		
		//SCENARIO 1
//		Q1 : select movie0_.id as id1_0_, movie0_.id_director as id_direc5_0_,
//			movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as year4_0_ 
//			from movies movie0_ 
//			where movie0_.title=?

		
		//SCENARIO 2 : Fetch Lazy		
//		select movie0_.id as id1_0_, movie0_.id_director as id_direc5_0_,
//		movie0_.duration as duration2_0_, movie0_.title as title3_0_,
//		movie0_.year as year4_0_ 
//		from movies movie0_ 
//		where movie0_.title=?
		
		var movie = movieRepository.findByTitle("The Terminator").get(0);
		System.out.println(movie);
		// SCENARIO 1
//		Q2 : select artist0_.id as id1_2_0_, artist0_.birthdate as birthdat2_2_0_,
//		artist0_.deathdate as deathdat3_2_0_, artist0_.name as name4_2_0_ 
//		from stars artist0_ where artist0_.id=?
		
		
		// SCENARIO 2 :
//		select artist0_.id as id1_2_0_, artist0_.birthdate as birthdat2_2_0_,
//		artist0_.deathdate as deathdat3_2_0_, artist0_.name as name4_2_0_ 
//		from stars artist0_ 
//		where artist0_.id=?
		System.out.println(movie.getDirector());
	}
	
	
	@Test
	void test_moviesWithDirector() {
		// 1+N queries in lazy mode with lazy + query by default
		// => 1 query with fetch join
		var movies = movieRepository.findByTitleContainingIgnoreCase("terminator");
		for (var movie: movies) {
			System.out.println(movie+" directed by: "+movie.getDirector());
		}
	}

}
