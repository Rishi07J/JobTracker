package com.rishi.jobtracker.dto;

public class ResumeResponseDTO {

    private String filename;

    public ResumeResponseDTO() {
    }

    public ResumeResponseDTO(
            String filename) {

        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(
            String filename) {

        this.filename = filename;
    }
}