package movieapp.dto;

import movieapp.entity.Artist;

public class ArtistCountMinMax {
	
	Artist artist;
	long countT;
	int min;
	int max;
		
	public ArtistCountMinMax(Artist artist, long countT, int min, int max) {
		super();
		this.artist = artist;
		this.countT = countT;
		this.min = min;
		this.max = max;
	}
	
	public ArtistCountMinMax(int idArtist, String nameArtist, long countT, int min, int max) {
		super();
		artist = new Artist();
		artist.setId(idArtist);
		artist.setName(nameArtist);
		this.countT = countT;
		this.min = min;
		this.max = max;
	}
	
	public Artist getActor() {
		return artist;
	}
	public void setActor(Artist artist) {
		this.artist = artist;
	}
	public long getCountT() {
		return countT;
	}
	public void setCountT(long countT) {
		this.countT = countT;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	
	
	
}
