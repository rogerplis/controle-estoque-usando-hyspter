package com.roger.service.dto;

import com.roger.domain.enumeration.Days;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.roger.domain.WorkingHours} entity.
 */
public class WorkingHoursDTO implements Serializable {

    private Long id;

    private LocalDate entry;

    private Instant leavingWork;

    private Instant extraTime;

    private Instant extraTime2;

    private Instant entryRest;

    private Instant returnRest;

    private LocalDate dayWeek;

    private Days day;

    private Set<JourneyDTO> journeys = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEntry() {
        return entry;
    }

    public void setEntry(LocalDate entry) {
        this.entry = entry;
    }

    public Instant getLeavingWork() {
        return leavingWork;
    }

    public void setLeavingWork(Instant leavingWork) {
        this.leavingWork = leavingWork;
    }

    public Instant getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(Instant extraTime) {
        this.extraTime = extraTime;
    }

    public Instant getExtraTime2() {
        return extraTime2;
    }

    public void setExtraTime2(Instant extraTime2) {
        this.extraTime2 = extraTime2;
    }

    public Instant getEntryRest() {
        return entryRest;
    }

    public void setEntryRest(Instant entryRest) {
        this.entryRest = entryRest;
    }

    public Instant getReturnRest() {
        return returnRest;
    }

    public void setReturnRest(Instant returnRest) {
        this.returnRest = returnRest;
    }

    public LocalDate getDayWeek() {
        return dayWeek;
    }

    public void setDayWeek(LocalDate dayWeek) {
        this.dayWeek = dayWeek;
    }

    public Days getDay() {
        return day;
    }

    public void setDay(Days day) {
        this.day = day;
    }

    public Set<JourneyDTO> getJourneys() {
        return journeys;
    }

    public void setJourneys(Set<JourneyDTO> journeys) {
        this.journeys = journeys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkingHoursDTO)) {
            return false;
        }

        WorkingHoursDTO workingHoursDTO = (WorkingHoursDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workingHoursDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkingHoursDTO{" +
            "id=" + getId() +
            ", entry='" + getEntry() + "'" +
            ", leavingWork='" + getLeavingWork() + "'" +
            ", extraTime='" + getExtraTime() + "'" +
            ", extraTime2='" + getExtraTime2() + "'" +
            ", entryRest='" + getEntryRest() + "'" +
            ", returnRest='" + getReturnRest() + "'" +
            ", dayWeek='" + getDayWeek() + "'" +
            ", day='" + getDay() + "'" +
            ", journeys=" + getJourneys() +
            "}";
    }
}
