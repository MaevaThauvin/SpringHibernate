package movieapp.entity;

import java.time.LocalDate;
//import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="stars")
public class Artist {
	private Integer id;
	private String name; //Required
	private LocalDate birthdate;
	private LocalDate deathdate;
	
	
	//bidirectionnal
	private List<Movie> directedMovies;
	private List<Movie> playedMovies;
	
	public Artist() {
		super();
		this.directedMovies = new ArrayList<>();
		this.playedMovies = new ArrayList<>();
	}
	
	public Artist(String name) {
		this(name, null, null);
	}
	
	public Artist(String name, LocalDate birthdate) {
		this(name, birthdate, null);
	}
	
	public Artist(String name, LocalDate birthdate, LocalDate deathdate) {
		this();
		this.name = name;
		this.birthdate = birthdate;
		this.deathdate = deathdate;
	}
	
	@ManyToMany(mappedBy="actors") // mapping bidirectionnal mapping configured in Movie property actors
	public List<Movie> getPlayedMovies() {
		return playedMovies;
	}

	public void setPlayedMovies(List<Movie> playedMovies) {
		this.playedMovies = playedMovies;
	}

	@OneToMany(mappedBy="director")
	public List<Movie> getDirectedMovies() {
		return directedMovies;
	}

	public void setDirectedMovies(List<Movie> directedMovies) {
		this.directedMovies = directedMovies;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(nullable = false, length=150)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	//@Temporal(TemporalType.DATE) //if types java.utile.Date or java.utile.Calendar used
	@Column(nullable = true)
	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	@Column(nullable = true)
	public LocalDate getDeathdate() {
		return deathdate;
	}

	public void setDeathdate(LocalDate deathdate) {
		this.deathdate = deathdate;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		return builder.append(name)
		.append("(")
		.append(birthdate)
		.append(", ")
		.append(deathdate)
		.append(")#")
		.append(id)
		.toString();
	}
	
	
	
	
}
