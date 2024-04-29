package com.employment.task.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Data
@ConfigurationProperties
public class UserConstraints {
    @Value("${user.constraints.min.age}")
    private int minAge;
}
