package movieapp.dto;

import movieapp.entity.Artist;

public interface IDirectorCountMinMax {
	
	Artist getDirector();
	Integer getMinYear();
	Integer getMaxYear();
	Long getCountT();
}
