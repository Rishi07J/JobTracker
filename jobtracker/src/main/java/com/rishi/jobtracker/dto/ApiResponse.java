package com.rishi.jobtracker.dto;

public class ApiResponse {

    private String message;
    private String version;
    private String status;

    public ApiResponse() {
    }

    public ApiResponse(String message, String version, String status) {
        this.message = message;
        this.version = version;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}