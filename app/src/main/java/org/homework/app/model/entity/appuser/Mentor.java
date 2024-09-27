package org.homework.app.model.entity.appuser;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("MENTOR")
public class Mentor extends AppUser implements MentorInterface {

    public Mentor(String email, String name, String birthDate) {
        super(email, name, birthDate, AppUserRole.MENTOR);
    }

}