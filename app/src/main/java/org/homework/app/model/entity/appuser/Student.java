package org.homework.app.model.entity.appuser;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("STUDENT")
public class Student extends AppUser {

    public Student(String email, String name, String birthDate) {
        super(email, name, birthDate, AppUserRole.STUDENT);
    }

}
