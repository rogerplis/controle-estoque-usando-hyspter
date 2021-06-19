package com.roger.service.mapper;

import com.roger.domain.*;
import com.roger.service.dto.EmployeeDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring", uses = { JourneyMapper.class, DepartmentMapper.class })
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
    @Mapping(target = "manager", source = "manager", qualifiedByName = "id")
    @Mapping(target = "journeys", source = "journeys", qualifiedByName = "idSet")
    @Mapping(target = "department", source = "department", qualifiedByName = "id")
    EmployeeDTO toDto(Employee s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoId(Employee employee);

    @Mapping(target = "removeJourney", ignore = true)
    Employee toEntity(EmployeeDTO employeeDTO);
}
