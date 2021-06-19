package com.roger.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.roger.domain.Journey} entity.
 */
public class JourneyDTO implements Serializable {

    private Long id;

    private String journeyName;

    private Integer tolerance;

    private Instant startJourney;

    private Instant endJourney;

    private LocalDate dayOut;

    private LocalDate startDate;

    private LocalDate endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJourneyName() {
        return journeyName;
    }

    public void setJourneyName(String journeyName) {
        this.journeyName = journeyName;
    }

    public Integer getTolerance() {
        return tolerance;
    }

    public void setTolerance(Integer tolerance) {
        this.tolerance = tolerance;
    }

    public Instant getStartJourney() {
        return startJourney;
    }

    public void setStartJourney(Instant startJourney) {
        this.startJourney = startJourney;
    }

    public Instant getEndJourney() {
        return endJourney;
    }

    public void setEndJourney(Instant endJourney) {
        this.endJourney = endJourney;
    }

    public LocalDate getDayOut() {
        return dayOut;
    }

    public void setDayOut(LocalDate dayOut) {
        this.dayOut = dayOut;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JourneyDTO)) {
            return false;
        }

        JourneyDTO journeyDTO = (JourneyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, journeyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JourneyDTO{" +
            "id=" + getId() +
            ", journeyName='" + getJourneyName() + "'" +
            ", tolerance=" + getTolerance() +
            ", startJourney='" + getStartJourney() + "'" +
            ", endJourney='" + getEndJourney() + "'" +
            ", dayOut='" + getDayOut() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
