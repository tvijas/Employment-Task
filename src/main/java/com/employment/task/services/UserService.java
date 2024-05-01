package com.employment.task.services;

import com.employment.task.properties.UserConstraints;
import com.employment.task.utils.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {
    private final EmailValidator emailValidator;
    private final UserConstraints userConstraints;
    public boolean isUserAdult  (LocalDate birthDate){
        LocalDate currentDate = LocalDate.now();
        LocalDate adultDate = birthDate.plusYears(userConstraints.getMinAge());
        return currentDate.isAfter(adultDate);
    }
        @Transactional
        public boolean isEmailValid (String email,Errors errors){
            emailValidator.validate(email,errors);
            return !errors.hasErrors();
        }
}
