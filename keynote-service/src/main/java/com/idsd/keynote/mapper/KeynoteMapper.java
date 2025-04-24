package com.idsd.keynote.mapper; // Correct package

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import com.idsd.keynote.dto.KeynoteDTO;   // Correct import
import com.idsd.keynote.entity.Keynote; // Correct import

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface KeynoteMapper {
    KeynoteDTO toDto(Keynote keynote);
    Keynote toEntity(KeynoteDTO dto);
}