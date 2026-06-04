package com.rishi.jobtracker.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rishi.jobtracker.dto.JobApplicationRequestDTO;
import com.rishi.jobtracker.dto.JobApplicationResponseDTO;
import com.rishi.jobtracker.entity.JobApplication;
import com.rishi.jobtracker.entity.User;
import com.rishi.jobtracker.repository.JobApplicationRepository;
import com.rishi.jobtracker.repository.UserRepository;

@Service
public class JobApplicationService {

    private final JobApplicationRepository repository;

    private final UserRepository userRepository;

    public JobApplicationService(
            JobApplicationRepository repository,
            UserRepository userRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
    }

    private JobApplicationResponseDTO mapToResponseDTO(
            JobApplication job) {

        return new JobApplicationResponseDTO(
                job.getId(),
                job.getCompany(),
                job.getRole(),
                job.getStatus()
        );
    }

    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));
    }

    public JobApplicationResponseDTO createJob(
            JobApplicationRequestDTO request) {

        User currentUser = getCurrentUser();

        JobApplication job = new JobApplication();

        job.setCompany(request.getCompany());
        job.setRole(request.getRole());
        job.setStatus(request.getStatus());

        job.setUser(currentUser);

        JobApplication savedJob =
                repository.save(job);

        return mapToResponseDTO(savedJob);
    }

    public List<JobApplicationResponseDTO>
    getAllJobs() {

        User currentUser = getCurrentUser();

        return repository.findByUser(currentUser)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public JobApplicationResponseDTO
    getJobById(Long id) {

        User currentUser = getCurrentUser();

        JobApplication job =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"));

        if (!job.getUser().getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        return mapToResponseDTO(job);
    }

    public JobApplicationResponseDTO updateJob(
            Long id,
            JobApplicationRequestDTO request) {

        User currentUser = getCurrentUser();

        JobApplication existingJob =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"));

        if (!existingJob.getUser().getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        existingJob.setCompany(
                request.getCompany());

        existingJob.setRole(
                request.getRole());

        existingJob.setStatus(
                request.getStatus());

        JobApplication updatedJob =
                repository.save(existingJob);

        return mapToResponseDTO(updatedJob);
    }

    public void deleteJob(Long id) {

        User currentUser = getCurrentUser();

        JobApplication existingJob =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"));

        if (!existingJob.getUser().getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        repository.delete(existingJob);
    }
}