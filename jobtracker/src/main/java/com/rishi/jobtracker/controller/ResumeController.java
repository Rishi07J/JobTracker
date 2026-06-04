package com.rishi.jobtracker.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rishi.jobtracker.dto.ResumeResponseDTO;
import com.rishi.jobtracker.entity.Resume;
import com.rishi.jobtracker.service.ResumeService;

@RestController
@RequestMapping("/jobs")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(
            ResumeService resumeService) {

        this.resumeService = resumeService;
    }

    @PostMapping("/{jobId}/resume")
    public ResponseEntity<String> uploadResume(

            @PathVariable Long jobId,

            @RequestParam("file")
            MultipartFile file)

            throws IOException {

        String message =
                resumeService.uploadResume(
                        jobId,
                        file
                );

        return ResponseEntity.ok(
                message
        );
    }

    @GetMapping("/{jobId}/resume")
    public ResponseEntity<ResumeResponseDTO>
    getResume(

            @PathVariable Long jobId) {

        return ResponseEntity.ok(
                resumeService.getResume(
                        jobId
                )
        );
    }

    @GetMapping("/{jobId}/resume/download")
    public ResponseEntity<Resource>
    downloadResume(
            @PathVariable Long jobId)
            throws Exception {

        Resume resume =
                resumeService
                        .getResumeEntity(
                                jobId);

        Path path =
                Paths.get(
                        resume.getFilepath());

        Resource resource =
                new UrlResource(
                        path.toUri());

        return ResponseEntity.ok()

        .contentType(
                MediaType.APPLICATION_PDF
        )

        .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\""
                        + resume.getFilename()
                        + "\"")

        .body(resource);
    }

    @GetMapping("/resume/test")
    public String test() {

        return "Resume Controller Working";
    }
}