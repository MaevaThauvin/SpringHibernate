package movieapp.dto;

public class MoviesCountYear {
	
	private Long count;
	private Integer year;
	
	public MoviesCountYear(Long count, Integer year) {
		super();
		this.count = count;
		this.year = year;
	}

	public MoviesCountYear() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	
	
}
