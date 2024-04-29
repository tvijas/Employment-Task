package com.employment.task;

import com.employment.task.properties.DataBaseProps;
import com.employment.task.properties.UserConstraints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({DataBaseProps.class, UserConstraints.class})
public class TaskApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}

}
