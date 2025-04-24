package com.idsd.conference.mapper; // Correct package

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import com.idsd.conference.dto.ConferenceDTO;       // Correct import
import com.idsd.conference.dto.CreateConferenceDTO; // Correct import
import com.idsd.conference.entity.Conference;      // Correct import

// Tell MapStruct to use ReviewMapper for mapping the 'reviews' list
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ReviewMapper.class})
public interface ConferenceMapper {

    ConferenceDTO toDto(Conference conference);

    // Ignore fields automatically set or managed elsewhere for creation
    @Mapping(target="id", ignore=true)
    @Mapping(target="reviews", ignore=true) // Reviews added separately
    @Mapping(target="nombreInscrits", ignore = true) // Defaults to 0
    @Mapping(target="score", ignore = true) // Defaults to 0.0
    Conference createDtoToEntity(CreateConferenceDTO createDto);

    // Optional: Mapper from full DTO back to entity (less common)
    // Conference toEntity(ConferenceDTO dto);
}