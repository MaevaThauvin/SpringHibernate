package movieapp.service;

import java.util.Optional;

import movieapp.dto.ArtistSimple;

public interface IArtistService {
	
	Optional<ArtistSimple> getById(int id);

	ArtistSimple add(ArtistSimple artistDto);
	ArtistSimple getByName(String name);
}
