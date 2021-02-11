package movieapp.service;

import java.util.List;
import java.util.Optional;

import movieapp.dto.MovieSimple;

public interface IMovieService {
	//CREATE
	MovieSimple add(MovieSimple movie);
	//UPDATE
	Optional<MovieSimple> update(MovieSimple movie);
	
	//READ
	List<MovieSimple> getAll();
	Optional<MovieSimple> getById(Integer id);
	List<MovieSimple> getByTitle(String title);
	List<MovieSimple> getByTitleIgnoreCaseAndYearEquals(String title,int year);
	List<MovieSimple> getByYearBetweenOrderByYear(int min, int max);
	List<MovieSimple> getByYearGreaterThanEqual(int min);
	List<MovieSimple> getByYearLessThanEqual(int max);
}
