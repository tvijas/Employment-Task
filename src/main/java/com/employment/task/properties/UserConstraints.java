package com.employment.task.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties
public class UserConstraints {
    @Value("${user.constraints.min.age}")
    private int minAge;
}
