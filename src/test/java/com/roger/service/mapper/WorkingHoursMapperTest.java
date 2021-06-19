package com.roger.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkingHoursMapperTest {

    private WorkingHoursMapper workingHoursMapper;

    @BeforeEach
    public void setUp() {
        workingHoursMapper = new WorkingHoursMapperImpl();
    }
}
