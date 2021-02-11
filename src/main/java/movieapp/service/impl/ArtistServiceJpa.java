package movieapp.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import movieapp.dto.ArtistSimple;
import movieapp.dto.MovieSimple;
import movieapp.entity.Artist;
import movieapp.entity.Movie;
import movieapp.persistence.ArtistRepository;
import movieapp.service.IArtistService;

@Service
@Transactional
public class ArtistServiceJpa implements IArtistService {
	
	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Optional<ArtistSimple> getById(int id) {
		return artistRepository.findById(id) //fetch opt entity artist
			.map(artistEntity -> modelMapper.map(artistEntity, ArtistSimple.class)); //convert entity -> dto
	}

	@Override
	public ArtistSimple add(ArtistSimple artistDto) {
		Artist artistEntityFromRepo = artistRepository.save(
				modelMapper.map(artistDto, Artist.class)); //convert dto param to entity
		return modelMapper.map(artistEntityFromRepo, ArtistSimple.class); // convert entity to dto result
	}

	@Override
	public Set<ArtistSimple> getByName(String name) {
		return artistRepository.findByNameIgnoreCase(name)
			.stream().map(
					artistEntity -> modelMapper.map(artistEntity, ArtistSimple.class))
					.collect(Collectors.toSet());
	}
	
	//TODO: To be implemented
	@Override
	public Optional<ArtistSimple> update(ArtistSimple artistSimple) {
//		
//		Optional<Artist> artistEntity2 = Optional.of(modelMapper.map(artistRepository.findById(artistSimple.getId()), Artist.class));
//		artistEntity2.stream().map(ae -> ae.setName(artistSimple.getName()));
//
//		Optional<ArtistSimple> artistSimple = Optional.of(modelMapper.map(artistEntity, ArtistSimple.class));
		return Optional.of(artistSimple);
	}

}
