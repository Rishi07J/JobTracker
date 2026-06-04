package com.rishi.jobtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rishi.jobtracker.entity.JobApplication;
import com.rishi.jobtracker.entity.User;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByUser(User user);
}