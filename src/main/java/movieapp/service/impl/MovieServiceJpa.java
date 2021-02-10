package movieapp.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import movieapp.dto.MovieSimple;
import movieapp.entity.Movie;
import movieapp.persistence.MovieRepository;
import movieapp.service.IMovieService;

@Service
@Transactional
public class MovieServiceJpa implements IMovieService {
	
	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public MovieSimple add(MovieSimple movie) {
		//récupération du movie Dto en movie Entity
		Movie movieEntityIn = modelMapper.map(movie, Movie.class);
		//Persistence du movieEntity
		Movie movieEntityOut = movieRepository.save(movieEntityIn);
		//Récupération du movie Dto
		MovieSimple movieDtoRes = modelMapper.map(movieEntityOut, MovieSimple.class);
		// return of movie Dto
		return movieDtoRes;
	}

	@Override
	public List<MovieSimple> getAll() {
		//récupérationn de la liste MovieEntity
		List<Movie> moviesEntity = movieRepository.findAll();
		// Récupération de la liste movie Dto
		List<MovieSimple> moviesDto = moviesEntity.stream().map(me -> modelMapper.map(me, MovieSimple.class))
				.collect(Collectors.toList());
		return moviesDto;
	}

	@Override
	public Optional<MovieSimple> getById(Integer id) {
		//Get moviesEntity by id
		Optional<Movie> optMovieEntity = movieRepository.findById(id);
		//Get movieDto list
		Optional<MovieSimple> optMovieDto = optMovieEntity.map(me -> modelMapper.map(me, MovieSimple.class));
		return optMovieDto;
	}

	@Override
	public List<MovieSimple> getByTitle(String title) {
		List<Movie> movieEntity = movieRepository.findByTitle(title);
		List<MovieSimple> movieDto = movieEntity.stream().map(me -> modelMapper.map(me, MovieSimple.class))
				.collect(Collectors.toList());
		return movieDto;
	}

	@Override
	public List<MovieSimple> getByTitleIgnoreCaseAndYearEquals(String title, int year) {
		List<Movie> movieEntity = movieRepository.findByTitleIgnoreCaseAndYearEquals(title, year);
		List<MovieSimple> movieDto = movieEntity.stream().map(me -> modelMapper.map(me, MovieSimple.class))
				.collect(Collectors.toList());
		return movieDto;
	}

	@Override
	public List<MovieSimple> getByYearBetweenOrderByYear(int min, int max) {
		List<Movie> movieEntity = movieRepository.findByYearBetweenOrderByYear(min, max);
		List<MovieSimple> movieDto = movieEntity.stream().map(me -> modelMapper.map(me, MovieSimple.class))
				.collect(Collectors.toList());
		return movieDto;
	}

	@Override
	public List<MovieSimple> getByYearGreaterThanEqual(int min) {
		List<Movie> movieEntity = movieRepository.findByYearGreaterThanEqual(min);
		List<MovieSimple> movieDto = movieEntity.stream().map(me -> modelMapper.map(me, MovieSimple.class))
				.collect(Collectors.toList());
		return movieDto;
	}

	@Override
	public List<MovieSimple> getByYearLessThanEqual(int max) {
		List<Movie> movieEntity = movieRepository.findByYearLessThanEqual(max);
		List<MovieSimple> movieDto = movieEntity.stream().map(me -> modelMapper.map(me, MovieSimple.class))
				.collect(Collectors.toList());
		return movieDto;
	}
	
	

}
