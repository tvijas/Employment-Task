package com.employment.task.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
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
    @Valid @NotBlank(message = "birth_date must not be blank")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$",message = "birth_date must be \"yyyy-MM-dd\"")
    @JsonProperty("birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String birthDate;
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
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

}
