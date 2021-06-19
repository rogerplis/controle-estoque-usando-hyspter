package com.roger.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.roger.domain.enumeration.Days;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkingHours.
 */
@Entity
@Table(name = "working_hours")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkingHours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "entry")
    private LocalDate entry;

    @Column(name = "leaving_work")
    private Instant leavingWork;

    @Column(name = "extra_time")
    private Instant extraTime;

    @Column(name = "extra_time_2")
    private Instant extraTime2;

    @Column(name = "entry_rest")
    private Instant entryRest;

    @Column(name = "return_rest")
    private Instant returnRest;

    @Column(name = "day_week")
    private LocalDate dayWeek;

    @Enumerated(EnumType.STRING)
    @Column(name = "day")
    private Days day;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_working_hours__journey",
        joinColumns = @JoinColumn(name = "working_hours_id"),
        inverseJoinColumns = @JoinColumn(name = "journey_id")
    )
    @JsonIgnoreProperties(value = { "employees", "workinghours" }, allowSetters = true)
    private Set<Journey> journeys = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkingHours id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getEntry() {
        return this.entry;
    }

    public WorkingHours entry(LocalDate entry) {
        this.entry = entry;
        return this;
    }

    public void setEntry(LocalDate entry) {
        this.entry = entry;
    }

    public Instant getLeavingWork() {
        return this.leavingWork;
    }

    public WorkingHours leavingWork(Instant leavingWork) {
        this.leavingWork = leavingWork;
        return this;
    }

    public void setLeavingWork(Instant leavingWork) {
        this.leavingWork = leavingWork;
    }

    public Instant getExtraTime() {
        return this.extraTime;
    }

    public WorkingHours extraTime(Instant extraTime) {
        this.extraTime = extraTime;
        return this;
    }

    public void setExtraTime(Instant extraTime) {
        this.extraTime = extraTime;
    }

    public Instant getExtraTime2() {
        return this.extraTime2;
    }

    public WorkingHours extraTime2(Instant extraTime2) {
        this.extraTime2 = extraTime2;
        return this;
    }

    public void setExtraTime2(Instant extraTime2) {
        this.extraTime2 = extraTime2;
    }

    public Instant getEntryRest() {
        return this.entryRest;
    }

    public WorkingHours entryRest(Instant entryRest) {
        this.entryRest = entryRest;
        return this;
    }

    public void setEntryRest(Instant entryRest) {
        this.entryRest = entryRest;
    }

    public Instant getReturnRest() {
        return this.returnRest;
    }

    public WorkingHours returnRest(Instant returnRest) {
        this.returnRest = returnRest;
        return this;
    }

    public void setReturnRest(Instant returnRest) {
        this.returnRest = returnRest;
    }

    public LocalDate getDayWeek() {
        return this.dayWeek;
    }

    public WorkingHours dayWeek(LocalDate dayWeek) {
        this.dayWeek = dayWeek;
        return this;
    }

    public void setDayWeek(LocalDate dayWeek) {
        this.dayWeek = dayWeek;
    }

    public Days getDay() {
        return this.day;
    }

    public WorkingHours day(Days day) {
        this.day = day;
        return this;
    }

    public void setDay(Days day) {
        this.day = day;
    }

    public Set<Journey> getJourneys() {
        return this.journeys;
    }

    public WorkingHours journeys(Set<Journey> journeys) {
        this.setJourneys(journeys);
        return this;
    }

    public WorkingHours addJourney(Journey journey) {
        this.journeys.add(journey);
        journey.getWorkinghours().add(this);
        return this;
    }

    public WorkingHours removeJourney(Journey journey) {
        this.journeys.remove(journey);
        journey.getWorkinghours().remove(this);
        return this;
    }

    public void setJourneys(Set<Journey> journeys) {
        this.journeys = journeys;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkingHours)) {
            return false;
        }
        return id != null && id.equals(((WorkingHours) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkingHours{" +
            "id=" + getId() +
            ", entry='" + getEntry() + "'" +
            ", leavingWork='" + getLeavingWork() + "'" +
            ", extraTime='" + getExtraTime() + "'" +
            ", extraTime2='" + getExtraTime2() + "'" +
            ", entryRest='" + getEntryRest() + "'" +
            ", returnRest='" + getReturnRest() + "'" +
            ", dayWeek='" + getDayWeek() + "'" +
            ", day='" + getDay() + "'" +
            "}";
    }
}
