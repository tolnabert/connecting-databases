package org.homework.app.model.dto;

import java.util.Map;

public record AverageScoreResponse(Long studentId, String studentName, Map<String, Double> averages) {
}