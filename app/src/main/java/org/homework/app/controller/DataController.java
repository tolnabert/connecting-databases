package org.homework.app.controller;


import org.homework.app.model.dto.AverageScoreResponse;
import org.homework.app.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/data")
public class DataController {

    private final ExamService examService;

    public DataController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importNewExams() {
        try {
            examService.importNewExams();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/averages/{studentId}")
    public ResponseEntity<AverageScoreResponse> getAveragesForStudent(@PathVariable Long studentId) {
        try {
            AverageScoreResponse averages = examService.calculateAverageScore(studentId);
            return ResponseEntity.ok(averages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}