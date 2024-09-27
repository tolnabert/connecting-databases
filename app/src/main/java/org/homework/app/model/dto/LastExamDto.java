package org.homework.app.model.dto;

import java.util.List;

public record LastExamDto(
        Long examId,
        String module,
        Long mentorId,
        String mentorName,
        Long studentId,
        String studentName,
        String date,
        boolean cancelled,
        boolean success,
        String comment,
        List<ExamResultDto> results
) {
}