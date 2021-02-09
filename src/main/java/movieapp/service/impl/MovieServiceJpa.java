package movieapp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import movieapp.dto.MovieSimple;
import movieapp.service.IMovieService;

@Service
public class MovieServiceJpa implements IMovieService {

	@Override
	public MovieSimple add(MovieSimple movie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MovieSimple> getAll() {
		var movie = new MovieSimple();
		movie.setTitle("Blade Runner");
		// TODO Auto-generated method stub
		return List.of(movie);
	}

}
