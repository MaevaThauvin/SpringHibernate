package movieapp.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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
		List<Movie> movies = query.getResultList(); //execute and conert resultset as liste
		System.out.println(movies.size());
	}

}
