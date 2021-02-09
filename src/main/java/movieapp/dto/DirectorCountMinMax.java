package movieapp.dto;

import movieapp.entity.Artist;

public class DirectorCountMinMax {
	
	private Artist director;
	private Long count;
	private Integer minYear;
	private Integer maxYear;
	
	public DirectorCountMinMax(Artist director, Long count, Integer minYear, Integer maxYear) {
		super();
		this.director = director;
		this.count = count;
		this.minYear = minYear;
		this.maxYear = maxYear;
	}

	public DirectorCountMinMax() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Artist getDirector() {
		return director;
	}

	public void setDirector(Artist director) {
		this.director = director;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Integer getMinYear() {
		return minYear;
	}

	public void setMinYear(Integer minYear) {
		this.minYear = minYear;
	}

	public Integer getMaxYear() {
		return maxYear;
	}

	public void setMaxYear(Integer maxYear) {
		this.maxYear = maxYear;
	}
	
	
	

}
