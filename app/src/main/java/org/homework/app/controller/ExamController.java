package org.homework.app.controller;


import org.homework.app.model.dto.ExamDto;
import org.homework.app.model.dto.LastExamDto;
import org.homework.app.model.entity.Exam;
import org.homework.app.model.entity.appuser.AppUser;
import org.homework.app.service.AppUserService;
import org.homework.app.service.ExamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/exams")
public class ExamController {

    private final ExamService examService;
    private final AppUserService appUserService;

    @Autowired
    public ExamController(ExamService examService, AppUserService appUserService) {
        this.examService = examService;
        this.appUserService = appUserService;
    }

    @GetMapping("/last/student/{studentId}/module/{module}")
    public ResponseEntity<?> getLastExamByStudentAndModule(@PathVariable Long studentId, @PathVariable String module) {
        Optional<AppUser> studentOptional = appUserService.getUserById(studentId);

        if (studentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found with ID: " + studentId);
        }

        Optional<LastExamDto> lastExam = examService.getLastExamByStudentAndModule(studentOptional.get(), module);

        if (lastExam.isPresent()) {
            return ResponseEntity.ok(lastExam.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No exams found for student with ID: " + studentId + " in module: " + module);
        }
    }

    @PostMapping("/take/{examId}/student/{studentId}")
    public ResponseEntity<Void> takeExam(
            @PathVariable Long studentId,
            @PathVariable Long examId,
            @RequestBody ExamDto examDto) {
        try {
            examService.takeExam(studentId, examId, examDto.results(), examDto.isCancelled(), examDto.isSuccess(), examDto.comment());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
