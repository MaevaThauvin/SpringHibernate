package movieapp.persistence;

import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import movieapp.dto.ArtistCountMinMax;
import movieapp.dto.IActorCountMinMax;
import movieapp.dto.IDirectorCountMinMax;
import movieapp.dto.IMoviesCountYear;
import movieapp.dto.INameYearTitle;
import movieapp.dto.NameYearTitle;
import movieapp.entity.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Integer>{
	
	Set<Artist> findByNameIgnoreCase(String name);
	
	Stream<Artist> findByNameEndingWithIgnoreCase(String name);
	
	@Query("select a from Artist a where extract(year from a.birthdate) = :year")
	Stream<Artist> findByBirthdateYear(int year);


//	@Query("select new movieapp.dto.NameYearTitle(a.name, m.year, m.title) from Movie m join m.actors a where a.name like :name order by m.year")
//	Stream<NameYearTitle> filmographyActor(String name);
	
//	@Query("select a.name as name, m.year as year, m.title as title from Movie m join m.actors a where a.name like %:name order by m.year")
//	Stream<INameYearTitle> filmographyActor(String name);
	
	@Query("select count(*) as countT, m.year as year from Movie m where m.year >= :year group by m.year having count(*) >= :countT order by m.year desc")
	Stream<IMoviesCountYear> countMoviesByYear(Long countT, Integer year);
	
	@Query("select d as director, count(*) as countT, min(m.year) as minYear, max(m.year) as maxYear from Movie m join m.director d group by d order by count(*) desc")
	Stream<IDirectorCountMinMax> directorCountMinMax();
	
//	@Query("select a as actor, count(*) as countT, min(year) as min, max(year) as max from Movie m join m.actors a group by a order by count(*) desc")
//	Stream<IActorCountMinMax> artistCountMinMax();
	
//	@Query("select new movieapp.dto.ArtistCountMinMax(a.id as id, a.name as name, count(*) as countT, min(year) as min, max(year) as max) from Movie m join m.actors a group by a order by count(*) desc")
//	Stream<ArtistCountMinMax> artistCountMinMax();
}
