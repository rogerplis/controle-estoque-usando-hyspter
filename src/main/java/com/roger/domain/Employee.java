package com.roger.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Employee entity.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * The firstname attribute.
     */
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "pis")
    private String pis;

    @Column(name = "ctps")
    private String ctps;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tasks", "employee" }, allowSetters = true)
    private Set<Job> jobs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "jobs", "manager", "journeys", "department" }, allowSetters = true)
    private Employee manager;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_employee__journey",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "journey_id")
    )
    @JsonIgnoreProperties(value = { "employees", "workinghours" }, allowSetters = true)
    private Set<Journey> journeys = new HashSet<>();

    /**
     * Another side of the same relationship
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "employees", "companion" }, allowSetters = true)
    private Department department;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee id(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Employee firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Employee lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Employee email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Employee phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Employee cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPis() {
        return this.pis;
    }

    public Employee pis(String pis) {
        this.pis = pis;
        return this;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getCtps() {
        return this.ctps;
    }

    public Employee ctps(String ctps) {
        this.ctps = ctps;
        return this;
    }

    public void setCtps(String ctps) {
        this.ctps = ctps;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Employee birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Set<Job> getJobs() {
        return this.jobs;
    }

    public Employee jobs(Set<Job> jobs) {
        this.setJobs(jobs);
        return this;
    }

    public Employee addJob(Job job) {
        this.jobs.add(job);
        job.setEmployee(this);
        return this;
    }

    public Employee removeJob(Job job) {
        this.jobs.remove(job);
        job.setEmployee(null);
        return this;
    }

    public void setJobs(Set<Job> jobs) {
        if (this.jobs != null) {
            this.jobs.forEach(i -> i.setEmployee(null));
        }
        if (jobs != null) {
            jobs.forEach(i -> i.setEmployee(this));
        }
        this.jobs = jobs;
    }

    public Employee getManager() {
        return this.manager;
    }

    public Employee manager(Employee employee) {
        this.setManager(employee);
        return this;
    }

    public void setManager(Employee employee) {
        this.manager = employee;
    }

    public Set<Journey> getJourneys() {
        return this.journeys;
    }

    public Employee journeys(Set<Journey> journeys) {
        this.setJourneys(journeys);
        return this;
    }

    public Employee addJourney(Journey journey) {
        this.journeys.add(journey);
        journey.getEmployees().add(this);
        return this;
    }

    public Employee removeJourney(Journey journey) {
        this.journeys.remove(journey);
        journey.getEmployees().remove(this);
        return this;
    }

    public void setJourneys(Set<Journey> journeys) {
        this.journeys = journeys;
    }

    public Department getDepartment() {
        return this.department;
    }

    public Employee department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", pis='" + getPis() + "'" +
            ", ctps='" + getCtps() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            "}";
    }
}
