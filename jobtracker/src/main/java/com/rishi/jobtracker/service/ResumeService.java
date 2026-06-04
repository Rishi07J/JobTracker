package com.rishi.jobtracker.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rishi.jobtracker.dto.ResumeResponseDTO;
import com.rishi.jobtracker.entity.JobApplication;
import com.rishi.jobtracker.entity.Resume;
import com.rishi.jobtracker.repository.JobApplicationRepository;
import com.rishi.jobtracker.repository.ResumeRepository;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private final JobApplicationRepository jobApplicationRepository;

    public ResumeService(
            ResumeRepository resumeRepository,
            JobApplicationRepository jobApplicationRepository) {

        this.resumeRepository = resumeRepository;
        this.jobApplicationRepository =
                jobApplicationRepository;
    }

    public String uploadResume(
            Long jobId,
            MultipartFile file)
            throws IOException {

        JobApplication job =
                jobApplicationRepository
                        .findById(jobId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"));

        String fileName =
                System.currentTimeMillis()
                        + "_"
                        + file.getOriginalFilename();

        Path uploadPath =
                Paths.get("uploads");

        if (!Files.exists(uploadPath)) {

            Files.createDirectories(
                    uploadPath);
        }

        Path filePath =
                uploadPath.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath
        );

        Resume existingResume =
                resumeRepository
                        .findByJob(job)
                        .orElse(null);

        if (existingResume != null) {

            resumeRepository.delete(
                    existingResume);

            resumeRepository.flush();
        }

        Resume resume =
                new Resume();

        resume.setFilename(fileName);

        resume.setFilepath(
                filePath.toString());

        resume.setJob(job);

        resumeRepository.save(
                resume);

        return "Resume uploaded successfully";
    }

    public ResumeResponseDTO getResume(
            Long jobId) {

        JobApplication job =
                jobApplicationRepository
                        .findById(jobId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"));

        Resume resume =
                resumeRepository
                        .findByJob(job)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Resume not found"));

        return new ResumeResponseDTO(
                resume.getFilename()
        );
    }

    public Resume getResumeEntity(
            Long jobId) {

        JobApplication job =
                jobApplicationRepository
                        .findById(jobId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"));

        return resumeRepository
                .findByJob(job)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Resume not found"));
    }
}