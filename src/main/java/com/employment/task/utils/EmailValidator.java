package com.employment.task.utils;

import com.employment.task.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class EmailValidator implements Validator {
    private final UserRepository userRepository;
    private static final String EMAIL_PATTERN = "^\\S+@\\S+$";

    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!(target instanceof String email)) {
            errors.reject("email", "target is not String");
            return;
        }
        if (!isValidEmail(email)) {
            errors.rejectValue("email", "", "Invalid email format");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            errors.rejectValue("email", "", "Email is already in use");
        }
    }

    public boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
