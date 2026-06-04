package com.rishi.jobtracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rishi.jobtracker.entity.JobApplication;
import com.rishi.jobtracker.entity.Resume;

public interface ResumeRepository
        extends JpaRepository<Resume, Long> {

    Optional<Resume> findByJob(
            JobApplication job);

    void deleteByJob(
            JobApplication job);
}