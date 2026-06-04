package com.rishi.jobtracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filepath;

    @OneToOne
    @JoinColumn(
            name = "job_id",
            nullable = false,
            unique = true)
    private JobApplication job;

    public Resume() {
    }

    public Resume(
            Long id,
            String filename,
            String filepath,
            JobApplication job) {

        this.id = id;
        this.filename = filename;
        this.filepath = filepath;
        this.job = job;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(
            String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(
            String filepath) {
        this.filepath = filepath;
    }

    public JobApplication getJob() {
        return job;
    }

    public void setJob(
            JobApplication job) {
        this.job = job;
    }
}