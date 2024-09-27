package org.homework.app.service;

import org.homework.app.model.dto.AverageScoreResponse;
import org.homework.app.model.dto.ExamDto;
import org.homework.app.model.dto.ExamResultDto;
import org.homework.app.model.dto.LastExamDto;
import org.homework.app.model.entity.Exam;
import org.homework.app.model.entity.ExamResult;
import org.homework.app.model.entity.appuser.AppUser;
import org.homework.app.model.entity.appuser.Student;
import org.homework.app.repository.AppUserRepository;
import org.homework.app.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final AppUserRepository appUserRepository;
    private final JSONInputReader jsonReader;

    @Autowired
    public ExamService(ExamRepository examRepository, AppUserRepository appUserRepository, JSONInputReader jsonReader) {
        this.examRepository = examRepository;
        this.appUserRepository = appUserRepository;
        this.jsonReader = jsonReader;
    }

    public void importNewExams() throws Exception {
        List<ExamDto> newExamDtos = fetchNewExamData();
        newExamDtos.forEach(this::processExamDto);
    }

    private void processExamDto(ExamDto examDto) {
        Long mentorId = appUserRepository.findMentorIdByEmail(examDto.mentorEmail())
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found with email: " + examDto.mentorEmail()));

        Long studentId = appUserRepository.findStudentIdByEmail(examDto.studentEmail())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with email: " + examDto.studentEmail()));

        if (!examRepository.existsByModuleAndDateAndStudentIdAndMentorId(
                examDto.module(), examDto.date(), studentId, mentorId)) {

            Exam exam = createExamFromDto(examDto, mentorId, studentId);
            saveExam(exam, examDto.results());
        }
    }

    private Exam createExamFromDto(ExamDto examDto, Long mentorId, Long studentId) {
        Exam exam = new Exam();
        exam.setModule(examDto.module());
        exam.setDate(examDto.date());
        exam.setCancelled(examDto.isCancelled());
        exam.setSuccess(examDto.isSuccess());
        exam.setComment(examDto.comment());

        appUserRepository.findMentorById(mentorId).ifPresent(exam::setMentor);
        appUserRepository.findStudentById(studentId).ifPresent(exam::setStudent);

        return exam;
    }

    private void saveExam(Exam exam, List<ExamDto.ExamResult> results) {
        List<ExamResult> examResults = results.stream()
                .map(resultDto -> {
                    ExamResult examResult = new ExamResult();
                    examResult.setDimension(resultDto.dimension());
                    examResult.setScore(resultDto.result());
                    examResult.setExam(exam);
                    return examResult;
                })
                .toList();

        exam.getResults().addAll(examResults);
        examRepository.save(exam);
    }

    private List<ExamDto> fetchNewExamData() throws Exception {
        try (InputStream inputStream = getClass().getResourceAsStream("/exam_data.json")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found!");
            }
            return jsonReader.readJsonData(inputStream);
        }
    }

    public Optional<LastExamDto> getLastExamByStudentAndModule(AppUser student, String module) {
        List<Exam> exams = examRepository.findExamsByStudentAndModule(student, module);
        if (!exams.isEmpty()) {
            Exam lastExam = exams.get(0);
            List<ExamResultDto> resultDtos = lastExam.getResults().stream()
                    .map(result -> new ExamResultDto(result.getDimension(), result.getScore()))
                    .toList();

            return Optional.of(new LastExamDto(
                    lastExam.getId(),
                    lastExam.getModule(),
                    lastExam.getMentor().getId(),
                    lastExam.getMentor().getName(),
                    lastExam.getStudent().getId(),
                    lastExam.getStudent().getName(),
                    lastExam.getDate(),
                    lastExam.isCancelled(),
                    lastExam.isSuccess(),
                    lastExam.getComment(),
                    resultDtos
            ));
        }
        return Optional.empty();
    }

    public void takeExam(Long studentId, Long examId, List<ExamDto.ExamResult> results, boolean isCancelled, boolean isSuccess, String comment) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid exam ID"));

        appUserRepository.findStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid studentEmail ID"));

        exam.setCancelled(isCancelled);
        exam.setSuccess(isSuccess);
        exam.setComment(comment);
        exam.setDate(LocalDate.now().toString());

        saveExam(exam, results);
    }

    public AverageScoreResponse calculateAverageScore(Long studentId) {
        Student student = appUserRepository.findStudentById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Exam> latestExams = examRepository.findValidExamsByStudent(student);
        Map<String, List<Integer>> scoresByDimension = new HashMap<>();

        for (Exam exam : latestExams) {
            exam.getResults().forEach(result -> {
                scoresByDimension
                        .computeIfAbsent(result.getDimension(), k -> new ArrayList<>())
                        .add(result.getScore());
            });
        }

        Map<String, Double> averages = new HashMap<>();

        for (Map.Entry<String, List<Integer>> entry : scoresByDimension.entrySet()) {
            String dimension = entry.getKey();
            List<Integer> scores = entry.getValue();

            if (!scores.isEmpty()) {
                double average = scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                averages.put(dimension, average);
            } else {
                averages.put(dimension, 0.0);
            }
        }

        return new AverageScoreResponse(student.getId(), student.getName(), averages);
    }
}