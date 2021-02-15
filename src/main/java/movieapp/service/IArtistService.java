package movieapp.service;

import java.util.Optional;
import java.util.Set;

import movieapp.dto.ArtistSimple;

public interface IArtistService {
	// READ
	Optional<ArtistSimple> getById(int id);
	
	// CREATE
	ArtistSimple add(ArtistSimple artistDto);
	Set<ArtistSimple> getByName(String name);

	// UPDATE
	Optional<ArtistSimple> update(ArtistSimple artist);
}
