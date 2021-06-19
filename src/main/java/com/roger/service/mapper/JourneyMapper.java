package com.roger.service.mapper;

import com.roger.domain.*;
import com.roger.service.dto.JourneyDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Journey} and its DTO {@link JourneyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JourneyMapper extends EntityMapper<JourneyDTO, Journey> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<JourneyDTO> toDtoIdSet(Set<Journey> journey);
}
