package com.cardyapp.Models;

import java.io.Serializable;

public class ConnectionDTO implements Serializable {

    private String name;
    private String designation;
    private String companyName;
    private String previousOrganization;
    private String location;
    private String sector;

    public ConnectionDTO() {

    }

    public ConnectionDTO(String name, String designation, String companyName, String previousOrganization, String location, String sector) {
        this.name = name;
        this.designation = designation;
        this.companyName = companyName;
        this.previousOrganization = previousOrganization;
        this.location = location;
        this.sector = sector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPreviousOrganization() {
        return previousOrganization;
    }

    public void setPreviousOrganization(String previousOrganization) {
        this.previousOrganization = previousOrganization;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}
