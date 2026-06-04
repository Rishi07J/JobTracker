package com.rishi.jobtracker.dto;

public class JobApplicationResponseDTO {

    private Long id;
    private String company;
    private String role;
    private String status;

    public JobApplicationResponseDTO() {
    }

    public JobApplicationResponseDTO(
            Long id,
            String company,
            String role,
            String status) {

        this.id = id;
        this.company = company;
        this.role = role;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}