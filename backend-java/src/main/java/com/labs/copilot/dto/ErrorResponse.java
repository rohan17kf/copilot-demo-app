package com.labs.copilot.dto;

/**
 * Error response format as per API specification.
 */
public class ErrorResponse {
    private String error;
    private String code;
    private Object details;
    private String timestamp;
    private String path;

    public ErrorResponse() {
    }

    public ErrorResponse(String error, String code, String path) {
        this.error = error;
        this.code = code;
        this.path = path;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    public ErrorResponse(String error, String code, Object details, String path) {
        this(error, code, path);
        this.details = details;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
