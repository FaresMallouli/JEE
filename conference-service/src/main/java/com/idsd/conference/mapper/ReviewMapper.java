package com.idsd.conference.mapper; // Correct package

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import com.idsd.conference.dto.ReviewDTO;          // Correct import
import com.idsd.conference.dto.CreateReviewDTO;    // Correct import
import com.idsd.conference.entity.Review;         // Correct import

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {

    ReviewDTO toDto(Review review);

    // Ignore fields automatically set or set manually in service
    @Mapping(target="id", ignore=true)
    @Mapping(target="date", ignore=true)
    @Mapping(target="conference", ignore=true)
    Review createDtoToEntity(CreateReviewDTO createDto);
}