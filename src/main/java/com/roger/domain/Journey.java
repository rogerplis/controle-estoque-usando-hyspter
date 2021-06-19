package com.roger.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Journey.
 */
@Entity
@Table(name = "journey")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Journey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "journey_name")
    private String journeyName;

    @Column(name = "tolerance")
    private Integer tolerance;

    @Column(name = "start_journey")
    private Instant startJourney;

    @Column(name = "end_journey")
    private Instant endJourney;

    @Column(name = "day_out")
    private LocalDate dayOut;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToMany(mappedBy = "journeys")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jobs", "manager", "journeys", "department" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    @ManyToMany(mappedBy = "journeys")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "journeys" }, allowSetters = true)
    private Set<WorkingHours> workinghours = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Journey id(Long id) {
        this.id = id;
        return this;
    }

    public String getJourneyName() {
        return this.journeyName;
    }

    public Journey journeyName(String journeyName) {
        this.journeyName = journeyName;
        return this;
    }

    public void setJourneyName(String journeyName) {
        this.journeyName = journeyName;
    }

    public Integer getTolerance() {
        return this.tolerance;
    }

    public Journey tolerance(Integer tolerance) {
        this.tolerance = tolerance;
        return this;
    }

    public void setTolerance(Integer tolerance) {
        this.tolerance = tolerance;
    }

    public Instant getStartJourney() {
        return this.startJourney;
    }

    public Journey startJourney(Instant startJourney) {
        this.startJourney = startJourney;
        return this;
    }

    public void setStartJourney(Instant startJourney) {
        this.startJourney = startJourney;
    }

    public Instant getEndJourney() {
        return this.endJourney;
    }

    public Journey endJourney(Instant endJourney) {
        this.endJourney = endJourney;
        return this;
    }

    public void setEndJourney(Instant endJourney) {
        this.endJourney = endJourney;
    }

    public LocalDate getDayOut() {
        return this.dayOut;
    }

    public Journey dayOut(LocalDate dayOut) {
        this.dayOut = dayOut;
        return this;
    }

    public void setDayOut(LocalDate dayOut) {
        this.dayOut = dayOut;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Journey startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Journey endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public Journey employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Journey addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.getJourneys().add(this);
        return this;
    }

    public Journey removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getJourneys().remove(this);
        return this;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.removeJourney(this));
        }
        if (employees != null) {
            employees.forEach(i -> i.addJourney(this));
        }
        this.employees = employees;
    }

    public Set<WorkingHours> getWorkinghours() {
        return this.workinghours;
    }

    public Journey workinghours(Set<WorkingHours> workingHours) {
        this.setWorkinghours(workingHours);
        return this;
    }

    public Journey addWorkinghours(WorkingHours workingHours) {
        this.workinghours.add(workingHours);
        workingHours.getJourneys().add(this);
        return this;
    }

    public Journey removeWorkinghours(WorkingHours workingHours) {
        this.workinghours.remove(workingHours);
        workingHours.getJourneys().remove(this);
        return this;
    }

    public void setWorkinghours(Set<WorkingHours> workingHours) {
        if (this.workinghours != null) {
            this.workinghours.forEach(i -> i.removeJourney(this));
        }
        if (workingHours != null) {
            workingHours.forEach(i -> i.addJourney(this));
        }
        this.workinghours = workingHours;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Journey)) {
            return false;
        }
        return id != null && id.equals(((Journey) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Journey{" +
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
