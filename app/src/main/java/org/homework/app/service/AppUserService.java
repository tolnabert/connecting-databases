package org.homework.app.service;

import org.homework.app.model.entity.appuser.AppUser;
import org.homework.app.model.entity.appuser.Mentor;
import org.homework.app.model.entity.appuser.Student;
import org.homework.app.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public Optional<AppUser> getUserById(Long userId) {
        return appUserRepository.findById(userId);
    }

}