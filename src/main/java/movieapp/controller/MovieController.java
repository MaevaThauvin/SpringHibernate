package movieapp.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import movieapp.entity.Movie;
import movieapp.persistence.MovieRepository;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
	
	@Autowired
	private MovieRepository movieRepository;
	
	/**
	 * url /api/movies
	 * @return
	 */
	@GetMapping 
	@ResponseBody
	public List<Movie> movies() {
//		return List.of(
//				new Movie("Blade Runner", 1982, 117), 
//				new Movie("Kabir Singh", 2019, 173));
		return movieRepository.findAll();
	}
	
	/**
	 * url /api/movies/un
	 * @return
	 */
	@GetMapping("/{id}")
	@ResponseBody
	public Optional<Movie> movie(@PathVariable("id") int id) {
		// return new Movie("Kabir Singh", 2019, 173);
		return movieRepository.findById(id);
	}
	
	@PostMapping
	@ResponseBody
	public Movie addMovie(@RequestBody Movie movie) {
		return movieRepository.save(movie); // insert movie
	}
	
	@PutMapping
	public Optional<Movie> updateMovie(@RequestBody Movie movie) {
		//read movie grom database/repository
		Optional<Movie> optMovieDb = movieRepository.findById(movie.getId());
		 optMovieDb.ifPresent(m -> {
			 m.setTitle(movie.getTitle());
			 m.setYear(movie.getYear());
			 m.setDuration(movie.getDuration());
			 movieRepository.flush();
		 });
		// TODO persist modified object
		return optMovieDb;
	}
	
	@DeleteMapping
	public Movie deleteMovie(@RequestBody Movie movie) {
		//TODO persist delete object
		return movie;
	}
	
}
