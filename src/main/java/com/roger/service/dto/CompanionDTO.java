package com.roger.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.roger.domain.Companion} entity.
 */
public class CompanionDTO implements Serializable {

    private Long id;

    private String companyName;

    private String cnpj;

    private LocationDTO location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanionDTO)) {
            return false;
        }

        CompanionDTO companionDTO = (CompanionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanionDTO{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", location=" + getLocation() +
            "}";
    }
}
