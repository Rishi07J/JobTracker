package com.rishi.jobtracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.jobtracker.dto.ApiResponse;
import com.rishi.jobtracker.dto.JobApplicationRequestDTO;
import com.rishi.jobtracker.dto.JobApplicationResponseDTO;
import com.rishi.jobtracker.service.JobApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/jobs")
public class JobApplicationController {

    private final JobApplicationService service;

    public JobApplicationController(
            JobApplicationService service) {

        this.service = service;
    }

    @PostMapping
    public JobApplicationResponseDTO createJob(
            @Valid @RequestBody
            JobApplicationRequestDTO request) {

        return service.createJob(request);
    }

    @GetMapping
    public List<JobApplicationResponseDTO> getAllJobs() {

        return service.getAllJobs();
    }

    @GetMapping("/{id}")
    public JobApplicationResponseDTO getJobById(
            @PathVariable Long id) {

        return service.getJobById(id);
    }

    @PutMapping("/{id}")
    public JobApplicationResponseDTO updateJob(
            @PathVariable Long id,
            @Valid @RequestBody
            JobApplicationRequestDTO request) {

        return service.updateJob(id, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteJob(
            @PathVariable Long id) {

        service.deleteJob(id);

        return new ApiResponse(
                "Job deleted successfully",
                "1.0",
                "success"
        );
    }

    
}