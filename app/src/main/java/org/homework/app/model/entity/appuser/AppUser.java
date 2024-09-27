package org.homework.app.model.entity.appuser;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
public abstract class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String birthDate;

    @Enumerated(EnumType.STRING)
    @NonNull
    private AppUserRole role;

    public AppUser(String email, String name, String birthDate, AppUserRole role) {
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.role = role;
    }
}