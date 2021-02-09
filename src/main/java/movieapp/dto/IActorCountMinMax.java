package movieapp.dto;

import movieapp.entity.Artist;

public interface IActorCountMinMax {
	
	Long getCountT();
	Artist getActor();
	Integer getMin();
	Integer getMax();
}
