package movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import movieapp.entity.Artist;
import movieapp.entity.Movie;

@DataJpaTest
class MovieRepositoryDirectorUpdatingTest {

	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	ArtistRepository artistRepository;
	
	@Autowired
	TestEntityManager entityManager;	
	//EntityManager entityManager;	

	
	@Test
	void testSaveMovieWithDirector() {
		Artist clint = new Artist("Clint Eastwood", LocalDate.of(1930, 5, 31));
		artistRepository.save(clint); // no necessary if persist in cascade ie >> @ManyToOne(cascade = CascadeType.PERSIST) on getter foreign key
		Movie movie = new Movie("Unforgiven", 1992, 130);
		movie.setDirector(clint);
		movieRepository.save(movie); //eventually persist director if cascade set
		System.out.println("movie :"+movie+" with director : "+ movie.getDirector());
		//vider le cash de la mémoire vive
		entityManager.clear();
//		select
//        movie0_.id as id1_1_0_,
//        movie0_.id_director as id_direc5_1_0_,
//        movie0_.duration as duration2_1_0_,
//        movie0_.title as title3_1_0_,
//        movie0_.year as year4_1_0_,
//        artist1_.id as id1_0_1_,
//        artist1_.birthdate as birthdat2_0_1_,
//        artist1_.deathdate as deathdat3_0_1_,
//        artist1_.name as name4_0_1_ 
//    from
//        movie movie0_ 
//    left outer join
//        artist artist1_ 
//            on movie0_.id_director=artist1_.id 
//    where
//        movie0_.id=?
		Movie movieRead = entityManager.find(Movie.class, movie.getId());
		System.out.println(movieRead+" with director: "+movieRead.getDirector());
		assertNotNull(movieRead.getDirector());
	}

}
