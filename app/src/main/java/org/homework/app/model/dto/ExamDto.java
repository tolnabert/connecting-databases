package org.homework.app.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public record ExamDto(
        String module,

        @JsonProperty("mentor")
        String mentorEmail,
        @JsonProperty("student")
        String studentEmail,
        String email,
        String date,
        @JsonProperty("cancelled")
        boolean isCancelled,
        @JsonProperty("success")
        boolean isSuccess,
        String comment,
        List<ExamResult> results
) {
    public List<ExamResult> results() {
        return results != null ? results : Collections.emptyList();
    }

    public record ExamResult(String dimension, int result) {
    }
}