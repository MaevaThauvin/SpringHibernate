package movieapp.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import movieapp.dto.ArtistSimple;
import movieapp.persistence.ArtistRepository;
import movieapp.service.IArtistService;

@Service
public class ArtistServiceJpa implements IArtistService {
	
	@Autowired
	ArtistRepository artistRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Optional<ArtistSimple> getById(int id) {
		return artistRepository.findById(id) //fetch opt entity artist
			.map(artistEntity -> modelMapper.map(artistEntity, ArtistSimple.class)); //convert entity -> dto
	}

}
