package movieapp.dto;

//DTO: Data Transfert Object
public class MovieStat {
	private long count;
	private Integer min_year;
	private Integer max_year;
	public MovieStat() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MovieStat(long count, Integer min_year, Integer max_year) {
		super();
		this.count = count;
		this.min_year = min_year;
		this.max_year = max_year;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public Integer getMin_year() {
		return min_year;
	}
	public void setMin_year(Integer min_year) {
		this.min_year = min_year;
	}
	public Integer getMax_year() {
		return max_year;
	}
	public void setMax_year(Integer max_year) {
		this.max_year = max_year;
	}
	
	
}
