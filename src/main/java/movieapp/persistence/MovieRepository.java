package movieapp.persistence;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import movieapp.entity.Movie;

// paramètres de généricité :
//	T = Movie : objets gérés par le répository
//	ID= Integer : type de la clé primaire
// By Default use database in Memory H2
public interface MovieRepository extends JpaRepository<Movie, Integer>{
	// gifts : save/findAll/findById/...
	
//	select movie0_.id as id1_0_, movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as year4_0_ 
//	from movie movie0_ 
//	where movie0_.title=?
	List<Movie> findByTitle(String title);
	
//	select movie0_.id as id1_0_, movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as year4_0_ 
//	from movie movie0_ where upper(movie0_.title) 
//	like upper(?) escape ?
	List<Movie> findByTitleContainingIgnoreCase(String title);
	
	List<Movie> findByYearOrderByTitle(int year);
	
//	where year >= 2000
	List<Movie> findByYearGreaterThanEqual(int year);
	
//	where year between 2000 and 2009
	List<Movie> findByYearBetweenOrderByYear(int min, int max);
	List<Movie> findByYearBetween(int min, int max, Sort sort);

	
//	where title = 'The Lion King' and year = '1994'
	List<Movie> findByTitleIgnoreCaseAndYearEquals(String title, int year);
	
//	where duration is null
	List<Movie> findByDurationIsNull();
	
	List<Movie> findByYearLessThanEqual(int max);
	
	List<Movie>findByDirectorName(String director);

}
