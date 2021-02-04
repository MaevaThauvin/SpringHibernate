package movieapp.controller;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import movieapp.entity.Artist;
import movieapp.entity.Movie;
import movieapp.persistence.ArtistRepository;
import movieapp.persistence.MovieRepository;

@Transactional
@RestController
@RequestMapping("/api/movies")
public class MovieController {
	
	@Autowired
	private MovieRepository movieRepository;	
	
	@Autowired
	private ArtistRepository artistRepository;
	
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
	@GetMapping("/{id}") //a utiliser sur des valeurs numériques
	@ResponseBody
	public Optional<Movie> movie(@PathVariable("id") int id) {
		// return new Movie("Kabir Singh", 2019, 173);
		return movieRepository.findById(id);
	}
	
	/**
	 * path /api/movies/byTitle?t=Spectre
	 * @param title
	 * @return
	 */
	@GetMapping("/byTitle")
	public List<Movie> moviesByTitle(@RequestParam("t") String title){
		return movieRepository.findByTitle(title);
	}
	/**
	 * path /api/movies/byTitleYear?t=Spectre&y=2015
	 * @param title
	 * @param year
	 * @return
	 */
	
	@GetMapping("/byTitleYear")
	public List<Movie> moviesByTitleYear(@RequestParam("t") String title,
										@RequestParam(value="y", required=false) Integer year){
		if(Objects.isNull(year)) {
			return movieRepository.findByTitle(title);
		}
		else {
			return movieRepository.findByTitleIgnoreCaseAndYearEquals(title, year);
		}
	}
	
	/**
	 * path api/movies/byYear?min=2000&max=2005
	 * @param min
	 * @param max
	 * @return
	 */
	@GetMapping("/byYear")
	public List<Movie> moviesByYearBetweenMinMax(@RequestParam(value="min", required=false) Integer min, 
			@RequestParam(value="max", required=false) Integer max){
		if(Objects.nonNull(min)) {
			if(Objects.nonNull(max)) {
				return movieRepository.findByYearBetweenOrderByYear(min, max);
			}
			else {
				return movieRepository.findByYearGreaterThanEqual(min);
			}
		}
		if(Objects.nonNull(max)) {
				return movieRepository.findByYearLessThanEqual(max);
		}
		else {
			return List.of();
		}
				
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
//			 movieRepository.flush();
		 });
		// TODO persist modified object
		return optMovieDb;
	}
	
	/**
	 * programmation impératif
	 * path /api/movies.director?mid=1&did=3
	 * @param <Optional>
	 * @param idDirector
	 * @param idMovie
	 * @return
	 */
	@PutMapping("/director")
	public Optional<Movie> setDirector(@RequestParam("mid") int idMovie,
			@RequestParam("did") int idDirector) {
		Optional<Movie> optMovieRead = movieRepository.findById(idMovie);
		Optional<Artist> optDirectorRead = artistRepository.findById(idDirector);
		
		if(optMovieRead.isEmpty() || optDirectorRead.isEmpty()) {
			return Optional.empty();
		}
		Artist directorRead = optDirectorRead.get();
		Movie movieRead = optMovieRead.get();
		
		movieRead.setDirector(directorRead);
		return Optional.of(movieRead);

		
//		var directorRead = artistRepository.findById(idDirector).map(d->d.getId(); return d;});
		
//		return movieRepository.findById(idMovie).map(m->{m.setDirector(directorRead);return m;});
		
	}
	/**
	 * same thing in functional programming
	 * @param idMovie
	 * @param idDirector
	 * @return
	 */
	
	@PutMapping("/director2")
	public Optional<Movie> setDirector2(@RequestParam("mid") int idMovie,
			@RequestParam("did") int idDirector) {
				
		
		return movieRepository.findById(idMovie)
				.flatMap(m-> artistRepository.findById(idDirector)
						.map(a-> {m.setDirector(a);return m;}));
		//{m.setDirector(directorRead);return m;}
	}
	
	@DeleteMapping
	public Optional<Movie> deleteMovie(@RequestBody Movie movie) {
		//TODO persist delete object
		return deleteMovieById(movie.getId());
	}
	
	/**
	 * url /api/movies/1
	 */
	
	@DeleteMapping("/{id}")
	public Optional<Movie> deleteMovieById(@PathVariable("id") int id){
		Optional<Movie> optMovieDb = movieRepository.findById(id);
		optMovieDb.ifPresent(m -> movieRepository.deleteById(m.getId()));
		return optMovieDb;
	}
	
}
