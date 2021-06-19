package com.roger.service.mapper;

import com.roger.domain.*;
import com.roger.service.dto.CompanionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Companion} and its DTO {@link CompanionDTO}.
 */
@Mapper(componentModel = "spring", uses = { LocationMapper.class })
public interface CompanionMapper extends EntityMapper<CompanionDTO, Companion> {
    @Mapping(target = "location", source = "location", qualifiedByName = "id")
    CompanionDTO toDto(Companion s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanionDTO toDtoId(Companion companion);
}
