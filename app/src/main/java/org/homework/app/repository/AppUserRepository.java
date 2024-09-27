package org.homework.app.repository;

import org.homework.app.model.entity.appuser.AppUser;
import org.homework.app.model.entity.appuser.Mentor;
import org.homework.app.model.entity.appuser.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    @Query("SELECT a.id FROM AppUser a WHERE a.email = :email AND a.role = 'MENTOR'")
    Optional<Long> findMentorIdByEmail(@Param("email") String email);

    @Query("SELECT a.id FROM AppUser a WHERE a.email = :email AND a.role = 'STUDENT'")
    Optional<Long> findStudentIdByEmail(@Param("email") String email);

    Optional<Mentor> findMentorById(Long id);

    Optional<Student> findStudentById(Long id);

}
