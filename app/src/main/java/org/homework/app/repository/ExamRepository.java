package org.homework.app.repository;

import org.homework.app.model.entity.Exam;
import org.homework.app.model.entity.appuser.AppUser;
import org.homework.app.model.entity.appuser.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    @Query("SELECT e FROM Exam e WHERE e.student = :student AND e.module = :module ORDER BY e.date DESC")
    List<Exam> findExamsByStudentAndModule(@Param("student") AppUser student, @Param("module") String module);

    boolean existsByModuleAndDateAndStudentIdAndMentorId(String module, String date, Long studentId, Long mentorId);

    @Query("SELECT e FROM Exam e WHERE e.student = :student AND e.cancelled = false ORDER BY e.date DESC")
    List<Exam> findValidExamsByStudent(Student student);

}
