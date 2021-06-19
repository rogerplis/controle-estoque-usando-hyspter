package com.roger.service.mapper;

import com.roger.domain.*;
import com.roger.service.dto.WorkingHoursDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkingHours} and its DTO {@link WorkingHoursDTO}.
 */
@Mapper(componentModel = "spring", uses = { JourneyMapper.class })
public interface WorkingHoursMapper extends EntityMapper<WorkingHoursDTO, WorkingHours> {
    @Mapping(target = "journeys", source = "journeys", qualifiedByName = "idSet")
    WorkingHoursDTO toDto(WorkingHours s);

    @Mapping(target = "removeJourney", ignore = true)
    WorkingHours toEntity(WorkingHoursDTO workingHoursDTO);
}
