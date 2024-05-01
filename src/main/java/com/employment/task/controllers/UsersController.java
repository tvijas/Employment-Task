package com.employment.task.controllers;

import com.employment.task.exceptions.InvalidRequestException;
import com.employment.task.exceptions.ResourceNotFoundException;
import com.employment.task.models.User;
import com.employment.task.models.entities.UserEntity;
import com.employment.task.properties.UserConstraints;
import com.employment.task.repositories.UserRepository;
import com.employment.task.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserConstraints userConstraints;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Validated User user, BindingResult bindingResult, Errors errors) {
        List<String> bindingErrors= checkForErrors(bindingResult);
        if(!bindingErrors.isEmpty())
            throw new InvalidRequestException(String.join("\n", bindingErrors));
        if (!userService.isUserAdult(user.getBirthDate()))
            throw new InvalidRequestException("User must be at least " + userConstraints.getMinAge() + " years old");
        if (!userService.isEmailValid(user.getEmail(), errors))
            throw new InvalidRequestException(errors.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining("; ")));
        userRepository.saveAndFlush(new UserEntity(user));
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateUserField(@PathVariable("id") int id,
                                             @RequestBody Map<String, Object> fields, Errors errors) {
        //Validation and preparing to save
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        fields.forEach((key, value) -> {
                    switch (key) {
                        case "email" -> {
                            if (!userService.isEmailValid((String) value, errors))
                                throw new InvalidRequestException(errors.getFieldErrors().stream()
                                        .map(FieldError::getDefaultMessage)
                                        .collect(Collectors.joining("; ")));
                            userEntity.setEmail((String) value);
                        }
                        case "first_name" -> userEntity.setFirstName((String) value);
                        case "last_name" -> userEntity.setLastName((String) value);
                        case "birth_date" -> {
                            if (!userService.isUserAdult(LocalDate.parse((CharSequence) value)))
                                throw new InvalidRequestException("User must be at least " + userConstraints.getMinAge() + " years old");
                            userEntity.setBirthDate(LocalDate.parse((CharSequence) value));
                        }
                        case "address" -> userEntity.setAddress((String) value);
                        case "phone_number" -> userEntity.setPhoneNumber((String) value);
                        default -> throw new InvalidRequestException("Invalid field name");
                    }
                });
        //Updating user field
        userRepository.saveAndFlush(userEntity);
        return ResponseEntity.ok().body("User updated successfully");
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") int id, @RequestBody User user, Errors errors) {
        //Validation
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        if (!userService.isUserAdult(user.getBirthDate()))
            throw new InvalidRequestException("User must be at least " + userConstraints.getMinAge() + " years old");
        if (!userService.isEmailValid(user.getEmail(), errors))
            throw new InvalidRequestException(errors.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining("; ")));
        //Updating user
        userEntity.setUserFields(user);
        userRepository.saveAndFlush(userEntity);
        return ResponseEntity.ok().body("User updated successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        //Validation
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        //Deleting user
        userRepository.delete(userEntity);
        return ResponseEntity.ok().body("User deleted successfully");
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchUsersByBirthDateRange(@RequestParam("from") LocalDate from,
                                                         @RequestParam("to") LocalDate to) {
        //Validation
        if (from.isAfter(to))
            throw new InvalidRequestException("From date must be before To date");
        //Returning users list
        List<UserEntity> users = userRepository.findByBirthDateBetween(from, to);
        return ResponseEntity.ok().body(users);
    }
    @ExceptionHandler({ResourceNotFoundException.class, InvalidRequestException.class})
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    public List<String> checkForErrors(BindingResult bindingResult) {
        List <String>errors = new LinkedList<>();
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                String errorMessage = error.getDefaultMessage();
                errors.add( errorMessage);
            }
        }
        return errors;
    }
}
