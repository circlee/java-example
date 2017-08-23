package com.example.model;

import com.google.gson.annotations.SerializedName;

public class ExceptionBody {

    @SerializedName("error")
    private String error;
    @SerializedName("timestamp")
    private Long timestamp;
    @SerializedName("duration")
    private Long duration;
    @SerializedName("exception")
    private String exception;
    @SerializedName("error_description")
    private String errorDescription;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
