package com.sv.examen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.sv.examen")
@EnableJpaRepositories(basePackages = "com.sv.examen.repository")
public class ExamenApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamenApplication.class, args);
	}

}
