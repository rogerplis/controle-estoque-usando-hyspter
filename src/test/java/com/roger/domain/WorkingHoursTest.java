package com.roger.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.roger.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkingHoursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkingHours.class);
        WorkingHours workingHours1 = new WorkingHours();
        workingHours1.setId(1L);
        WorkingHours workingHours2 = new WorkingHours();
        workingHours2.setId(workingHours1.getId());
        assertThat(workingHours1).isEqualTo(workingHours2);
        workingHours2.setId(2L);
        assertThat(workingHours1).isNotEqualTo(workingHours2);
        workingHours1.setId(null);
        assertThat(workingHours1).isNotEqualTo(workingHours2);
    }
}
