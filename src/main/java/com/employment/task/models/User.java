package com.employment.task.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    @Valid @NotBlank(message = "email must not be blank")
    @JsonProperty("email")
    private String email;
    @Valid @NotBlank(message = "first_name must not be blank")
    @JsonProperty("first_name")
    private String firstName;
    @Valid @NotBlank(message = "last_name must not be blank")
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @JsonProperty("address")
    private String address;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonCreator
    public User(@JsonProperty("email") String email,
                @JsonProperty("first_name") String firstName,
                @JsonProperty("last_name") String lastName,
                @JsonProperty("birth_date") String birthDate,
                @JsonProperty("address") String address,
                @JsonProperty("phone_number") String phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = LocalDate.parse(birthDate);
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
